package puyo.client3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

import puyo.data.Action;
import puyo.data.Box;
import puyo.data.Cluster;
import puyo.data.FallSpeed;
import puyo.data.Game;
import puyo.data.Puyo;
import puyo.data.PuyoState;
import puyo.data.PuyoState.Rotate;
import puyo.data.User;

public class PuyoPuyoAIImpl implements PuyoPuyoAI {
	static private Gson gson = new Gson();

	static private Rotate[] ALL_ROTATE = {Rotate.R0, Rotate.R90, Rotate.R180, Rotate.R270};

	public static final int[] CHAIN_BONUS = { 0, 8, 16, 32, 64, 96, 128, 160, 192, 224, 256, 288, 320, 352, 384, 416, 448, 480, 512 };
	public static final int[] CONNECT_BONUS = { 0, 0, 0, 0, 0, 2, 3, 4, 5, 6, 7, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10 };
	public static final int[] COLOR_BONUS = { 0, 0, 3, 6, 12, 24 };

	// 積まれたぷよの高さに対するペナルティ(位置ごと)
	static private int heightPenalty[][] = {
			{0, 0, 0, 0, 0, 0},
			{0, 1, 5, 5, 1, 0},
			{0, 2, 10, 10, 2, 0},
			{0, 3, 15, 15, 3, 0},
			{0, 4, 20, 20, 4, 0},
			{0, 5, 25, 25, 5, 0},
			{0, 6, 30, 30, 6, 0},
			{0, 7, 100, 100, 7, 0},
			{0, 8, 200, 200, 8, 0},
			{0, 9, 500, 500, 9, 0},
			{0, 10, 1000, 1000, 10, 0},
			{0, 20, 10000, 10000, 20, 0},
			};

	public PuyoPuyoAIImpl() {
	}

	@Override
	public Action createAction(Game game, User user) {
		System.out.println("createAction AI");
		Box box = game.selectBox(user.getName());
		Action action = new Action();
		Puyo[][] arr = new Puyo[Box.ROW][Box.RANK];
		box.createPuyoArray(arr);

		Cluster currentPuyo = box.getCurrentPuyo().getCluster();
		Cluster nextPuyo = box.getNextPuyo();
		
		// 探索
		PuyoState puyoState = detectPutState(arr, currentPuyo, nextPuyo);
		
		if (puyoState == null) {
			puyoState = box.getCurrentPuyo();
		}

		action.setState(puyoState);
		action.setSequence(box.getSequence());
		action.setFallSpeed(FallSpeed.FAST);
		return action;
	}

	// 探索
	private PuyoState detectPutState(Puyo[][] arr, Cluster currentPuyo, Cluster nextPuyo) {
		PuyoState maxPuyoState = null;
		int maxPoint = Integer.MIN_VALUE;

		// 置ける個所それぞれについて
		ArrayList<PuyoState> puyoStateList = enumulatePutablePosition(arr, currentPuyo);
		for (PuyoState p : puyoStateList) {
			Puyo[][] nextArr = copyPuyoArr(arr);

			// ぷよを置く
			int point = putPuyo(nextArr, p);

			// 次を探索
			point += detectNext(nextArr, nextPuyo);
			
			//System.out.printf("%d,%d,%s:%d\n", p.getRow(), p.getRank(), p.getRotate(), point);

			// 最大の評価値を記録
			if (point > maxPoint) {
				maxPuyoState = p;
				maxPoint = point;
			}
		}
		
		//System.out.printf("%d,%d,%s:%d\n", maxPuyoState.getRow(), maxPuyoState.getRank(), maxPuyoState.getRotate(), maxPoint);
		return maxPuyoState;
	}

	// 次を探索
	private int detectNext(Puyo[][] arr, Cluster p) {
		int maxPoint = Integer.MIN_VALUE;

		// 置ける個所それぞれについて
		ArrayList<PuyoState> nextPuyoStateList = enumulatePutablePosition(arr, p);
		for (PuyoState nextPuyoState : nextPuyoStateList) {
			Puyo[][] nextArr = copyPuyoArr(arr);
			
			// ぷよを置く
			int point = putPuyo(nextArr, nextPuyoState);

			// 評価関数
			point += eval(nextArr);
			
			if (point > maxPoint) {
				maxPoint = point;
			}
		}

		// 最大値を返す
		return maxPoint;
	}
	
	
	private int eval(Puyo[][] arr) {
		int point = 0;

		// 末端の場を評価

		// 積まれたぷよの高さの評価
		point += evalHeight(arr) * 100;
		
		// 連結の評価
		point += evalRenketsu(arr) * 100;

		return point;
	}

	// 積まれたぷよの高さの評価
	private int evalHeight(Puyo[][] nextArr) {
		int rank;
		int penalty = 0;
		for (rank = Box.RANK - 1; rank > 0; rank--) {
			boolean allNone = true;
			for (int row = 0; row < Box.ROW; row++) {
				if (nextArr[row][rank] != Puyo.NONE) {
					allNone = false;
					penalty += heightPenalty[rank][row];
				}
			}
			
			if (!allNone) {
				break;
			}
		}
		// 中央が高いほどマイナス
		return -(rank - 1) * penalty;
	}

	// 連結の評価
	private int evalRenketsu(Puyo[][] arr) {
		int point = 0;
		boolean checked[][] = new boolean[Box.ROW][Box.RANK];
		for (int row = 0; row < Box.ROW; row++) {
			for (int rank = 0; rank < Box.RANK; rank++) {
				Puyo color = arr[row][rank];
				if (color != Puyo.NONE && color != Puyo.OJM && checked[row][rank] == false) {
					int samecolor = 0;

					// 下方向に同色の連結があるか
					for (int rank2 = rank; rank2 >= 0; rank2--) {
						if (checked[row][rank2] && arr[row][rank2] == color) {
							samecolor++;
							break;
						}
					}
					
					// 左下方向に同色の連結があるか
					if (row > 0) {
						for (int rank2 = rank; rank2 >= 0; rank2--) {
							if (checked[row - 1][rank2] && arr[row - 1][rank2] == color) {
								samecolor++;
								break;
							}
						}
					}

					int renketsu = checkErase(arr, checked, color, row, rank) - 1;
					point += renketsu * 2 + samecolor;
				}
			}
		}
		return point;
	}

	private Puyo[][] copyPuyoArr(Puyo[][] arr) {
		// arrをコピー
		Puyo[][] nextArr = new Puyo[Box.ROW][];
		for (int i = 0; i < Box.ROW; i++) {
			nextArr[i] = arr[i].clone();
		}
		return nextArr;
	}

	// 置ける個所を列挙
	private ArrayList<PuyoState> enumulatePutablePosition(Puyo[][] arr, Cluster currentPuyo) {
		ArrayList<PuyoState> puyoStateList = new ArrayList<PuyoState>();

		int row;
		for (row = 3; row >= 0; row--) {
			if (arr[row][Box.RANK - 3] != Puyo.NONE) {
				break;
			}
		}
		
		if (row >= 2) {
			// 置ける場所がない
			return puyoStateList;
		}
		
		row++;

		// row
		for (; row < 6 && arr[row][Box.RANK - 3] == Puyo.NONE; row++) {
			// 回転パターン
			int rotate_upper_bound = ALL_ROTATE.length;
			// 同色の場合2パターンに制限
			if (currentPuyo.getFirst() == currentPuyo.getSecond()) {
				rotate_upper_bound = 2;
			}
			for (int i = 0; i < rotate_upper_bound; i++) {
				Rotate rotate = ALL_ROTATE[i];
				
				if (rotate == Rotate.R0) {
					if (row == 5 || arr[row + 1][Box.RANK - 3] != Puyo.NONE) {
						continue;
					}
				} else if (rotate == Rotate.R180) {
					if (row == 0 || arr[row - 1][Box.RANK - 3] != Puyo.NONE) {
						continue;
					}
				}
				
				PuyoState puyoState = new PuyoState(currentPuyo, rotate, row, Box.RANK - 2);
				
				// 置ける場所リストに追加
				puyoStateList.add(puyoState);
			}
		}
		
		return puyoStateList;
	}
	
	// ぷよを置く
	//	return : ポイント
	private int putPuyo(Puyo[][] arr, PuyoState p) {
		ArrayList<Integer> fallPosList = new ArrayList<Integer>();
		
		// first
		int row = p.getRow();
		int rank = p.getRank();
		arr[row][rank] = p.getCluster().getFirst();

		//second
		switch (p.getRotate()) {
		case R0:
			arr[row + 1][rank] = p.getCluster().getSecond();
			fallPosList.add(posToInt(row, rank));
			fallPosList.add(posToInt(row + 1, rank));
			break;
		case R90:
			arr[row][rank - 1] = p.getCluster().getSecond();
			fallPosList.add(posToInt(row, rank - 1));
			break;
		case R180:
			arr[row - 1][rank] = p.getCluster().getSecond();
			fallPosList.add(posToInt(row, rank));
			fallPosList.add(posToInt(row - 1, rank));
			break;
		case R270:
			arr[row][rank + 1] = p.getCluster().getSecond();
			fallPosList.add(posToInt(row, rank));
			break;
		}
		
		// 落下させる
		return fallPuyo(arr, fallPosList, 0, 0);
	}
	
	// 落下させる
	//	return : ポイント
	private int fallPuyo(Puyo[][] arr, ArrayList<Integer> fallPosList, int rensa, int point) {
		for (int i = 0; i < fallPosList.size(); i++) {
			int fallPos = fallPosList.get(i);

			// 落下
			int row = intToRow(fallPos);
			int rankBefor = intToRank(fallPos);
			int rankFall = rankBefor;
			for (; rankFall > 0; rankFall--) {
				if (arr[row][rankFall - 1] != Puyo.NONE) {
					break;
				}
			}
			if (rankBefor != rankFall) {
				// 落下位置更新
				fallPosList.set(i, posToInt(row, rankFall));
				
				int rank = rankBefor;
				int rank2 = rankFall;
				for (; rank < Box.RANK && arr[row][rank] != Puyo.NONE; rank++, rank2++) {
					arr[row][rank2] = arr[row][rank]; 
				}
				for (; rank2 < Box.RANK && rank2 <= rank; rank2++) {
					arr[row][rank2] = Puyo.NONE;
				}
			}
		}
		
		// 消去

		// 新しい落下位置リスト
		ArrayList<Integer> newFallPosList = new ArrayList<Integer>();

		int sumCnt = 0; // 消したぷよの総数
		int connBonus = 0; // 連結ボーナス
		Set<Puyo> set = new HashSet<Puyo>();
		for (Integer fallPos : fallPosList) {
			int row = intToRow(fallPos);
			int rank = intToRank(fallPos);
			Puyo privColor = Puyo.NONE;
			
			// 落下のあった位置より上を調べる
			for (; rank < Box.RANK; rank++) {
				// 前と色が違うとき
				if (arr[row][rank] != Puyo.NONE && arr[row][rank] != privColor) {
					// 消去
					int cnt = erase(arr, row, rank, newFallPosList);
					if (cnt >= 4) {
						sumCnt += cnt;
						connBonus += CONNECT_BONUS[cnt];
						set.add(arr[row][rank]);
					}
				}
				privColor = arr[row][rank];
			}
		}
		
		// 点数計算
		point += sumCnt * 10 * (CHAIN_BONUS[rensa] + connBonus + COLOR_BONUS[set.size()]);
		
		// 連鎖
		if (newFallPosList.size() > 0) {
			point += fallPuyo(arr, newFallPosList, rensa + 1, point);
		}
		
		return point;
	}
	
	// 消去
	//	return : 消えたぷよの数
	private int erase(Puyo[][] arr, int row, int rank, ArrayList<Integer> fallPosList) {
		boolean[][] checked = new boolean[Box.ROW][Box.RANK];
		Puyo color = arr[row][rank];
		
		// 消去チェック
		int cnt = checkErase(arr, checked, color, row, rank);
		
		if (cnt >= 4) {
			// 消去
			eraseInner(arr, checked, fallPosList);
		}
		
		return cnt;
	}
	
	// 消去チェック(再帰用)
	//	return : つながったぷよの数
	private int checkErase(Puyo[][] arr, boolean[][] checked, Puyo color, int row, int rank) {
		int cnt = 0;

		if (arr[row][rank] == Puyo.OJM) {
			// おじゃまぷよの消去
			checked[row][rank] = true;
			return cnt;
		} else if (arr[row][rank] != color) {
			return cnt;
		}

		checked[row][rank] = true;
		cnt++;
		
		// 上
		if (rank + 1 < Box.RANK && checked[row][rank + 1] == false) {
			cnt += checkErase(arr, checked, color, row, rank + 1);
		}
		// 下
		if (rank - 1 >= 0 && checked[row][rank - 1] == false) {
			cnt += checkErase(arr, checked, color, row, rank - 1);
		}
		// 右
		if (row + 1 < Box.ROW && checked[row + 1][rank] == false) {
			cnt += checkErase(arr, checked, color, row + 1, rank);
		}
		// 左
		if (row - 1 >= 0 && checked[row - 1][rank] == false) {
			cnt += checkErase(arr, checked, color, row - 1, rank);
		}
		
		return cnt;
	}
	
	// 消去(内部用)
	private void eraseInner(Puyo[][] arr, boolean[][] checked, ArrayList<Integer> fallPosList) {
		for (int row = 0; row < Box.ROW; row++) {
			int rankFall = 0;
			for (int rank = 0; rank < Box.RANK; rank++) {
				if (checked[row][rank]) {
					arr[row][rank] = Puyo.NONE;
					rankFall = rank + 1;
				}
			}
			
			// 落下位置を追加
			if (rankFall > 0 && rankFall < Box.RANK && arr[row][rankFall] != Puyo.NONE) {
				fallPosList.add(posToInt(row, rankFall));
			}
		}
	}
	
	private int posToInt(int row, int rank) {
		return rank * Box.ROW + row;
	}
	
	private int intToRow(int pos) {
		return pos % Box.ROW;
	}
	
	private int intToRank(int pos) {
		return pos / Box.ROW;
	}
}
