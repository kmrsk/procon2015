package puyo.client3;

import java.util.ArrayList;

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

	int DEPTH = 3; // 深読みするぷよ列の長さ
	int MONTECARLO_NUM = 10; // ランダムに生成するぷよ列の数
	
	// 積まれたぷよの高さに対するペナルティ(rowの位置ごと)
	static private int heightPenalty[] = {0, 1, 2, 2, 1, 0};

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

	private PuyoState detectPutState(Puyo[][] arr, Cluster currentPuyo, Cluster nextPuyo) {
		PuyoState maxPuyoState = null;
		int maxPoint = Integer.MIN_VALUE;

		// 置ける個所それぞれについて
		ArrayList<PuyoState> puyoStateList = enumulatePutablePosition(arr, currentPuyo);
		for (PuyoState p : puyoStateList) {
			Puyo[][] nextArr = copyPuyoArr(arr);

			// ぷよを置く
			int point = putPuyo(nextArr, p);

			// 置ける個所それぞれについて
			int sumPoint = 0;
			ArrayList<PuyoState> nextPuyoStateList = enumulatePutablePosition(nextArr, nextPuyo);
			for (PuyoState nextPuyoState : nextPuyoStateList) {
				// ランダムに生成
				for (int i = 0; i < MONTECARLO_NUM; i++) {
					// 評価関数
					sumPoint += eval(nextArr, nextPuyoState, new Cluster(Puyo.getRandom(), Puyo.getRandom()), 2);
				}
			}
			if (nextPuyoStateList.size() > 0) {
				point += sumPoint / nextPuyoStateList.size();
			}
			
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
	
	
	private int eval(Puyo[][] arr, PuyoState p, Cluster nextPuyo, int depth) {
		Puyo[][] nextArr = copyPuyoArr(arr);
		
		// ぷよを置く
		int point = putPuyo(nextArr, p);
		
		// 深読み
		if (depth < DEPTH) {
			// 置ける個所それぞれについて
			int sumPoint = 0;
			ArrayList<PuyoState> puyoStateList = enumulatePutablePosition(nextArr, nextPuyo);
			for (PuyoState nextPuyoState : puyoStateList) {
				// 再帰
				sumPoint += eval(nextArr, nextPuyoState, new Cluster(Puyo.getRandom(), Puyo.getRandom()), depth + 1);
			}
			if (puyoStateList.size() > 0) {
				point += sumPoint / puyoStateList.size();
			}
		} else {
			// 末端の場を評価

			// 積まれたぷよの高さの評価
			point += evalHeight(nextArr);
			
			// 連結の評価
			point += evalRenketsu(nextArr) * 10;
		}

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
					penalty += heightPenalty[row];
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
	private int evalRenketsu(Puyo[][] nextArr) {
		int renketsu = 0;
		boolean checked[][] = new boolean[Box.ROW][Box.RANK];
		for (int row = 0; row < Box.ROW; row++) {
			for (int rank = 0; rank < Box.RANK; rank++) {
				renketsu += checkErase(nextArr, checked, nextArr[row][rank], row, rank, 0);
			}
		}
		return renketsu;
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
				
				if (rotate == Rotate.R180) {
					if (row == 0 || arr[row - 1][Box.RANK - 3] != Puyo.NONE) {
						continue;
					}
				} else if (rotate == Rotate.R0) {
					if (row == 5 || arr[row + 1][Box.RANK - 3] != Puyo.NONE) {
						continue;
					}
				}
				
				PuyoState puyoState = new PuyoState(currentPuyo, rotate, row, Box.RANK - 3);
				
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
					
					// 連結数ボーナス
					int bonusRenketsu = 0;
					if (cnt >= 5) {
						bonusRenketsu = cnt - 3;
					}
					point += cnt * 10 * (rensa * 8 + bonusRenketsu);
				}
				privColor = arr[row][rank];
			}
		}
		
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
		int cnt = checkErase(arr, checked, color, row, rank, 0);
		
		if (cnt >= 4) {
			// 消去
			eraseInner(arr, checked, fallPosList);
		}
		
		return cnt;
	}
	
	// 消去チェック(再帰用)
	//	return : つながったぷよの数
	private int checkErase(Puyo[][] arr, boolean[][] checked, Puyo color, int row, int rank, int cnt) {
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
			cnt = checkErase(arr, checked, color, row, rank + 1, cnt);
		}
		// 下
		if (rank - 1 > 0 && checked[row][rank - 1] == false) {
			cnt = checkErase(arr, checked, color, row, rank - 1, cnt);
		}
		// 右
		if (row + 1 < Box.ROW && checked[row + 1][rank] == false) {
			cnt = checkErase(arr, checked, color, row + 1, rank, cnt);
		}
		// 左
		if (row - 1 > 0 && checked[row - 1][rank] == false) {
			cnt = checkErase(arr, checked, color, row - 1, rank, cnt);
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
