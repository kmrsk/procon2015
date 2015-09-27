package puyo.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

import puyo.data.Box;
import puyo.data.Puyo;

public class AIUtil {
	enum Direction {
		RIGHT, LEFT, UP, DOWN;
		public static final Direction[] ALL = new Direction[] { RIGHT, LEFT, UP, DOWN };
	}

	public AIUtil() {
	}

	private static Puyo getNearPuyo(Puyo[][] puyoArray, int row, int rank, Direction d) {
		if (row >= 0 && row < Box.ROW && rank >= 0 && rank < Box.RANK) {
			switch (d) {
			case DOWN:
				if (rank > 0) {
					return puyoArray[row][rank - 1];
				}
				break;
			case LEFT:
				if (row > 0) {
					return puyoArray[row - 1][rank];
				}
				break;
			case RIGHT:
				if (row < Box.ROW - 1) {
					return puyoArray[row + 1][rank];
				}
				break;
			case UP:
				if (rank < Box.RANK - 1) {
					return puyoArray[row][rank + 1];
				}
				break;
			default:
				break;
			}
		}

		return Puyo.NONE;
	}

	public static int countChainPotential1(Puyo[][] puyoArray) {
		return countChainPotential(puyoArray, AIUtil::countChain);
	}

	public static int countChainPotential2(Puyo[][] puyoArray) {
		return countChainPotential(puyoArray, AIUtil::countChainPotential1);
	}

	public static int countChainPotential3(Puyo[][] puyoArray) {
		return countChainPotential(puyoArray, AIUtil::countChainPotential2);
	}

	public static int countChainPotential(Puyo[][] puyoArray, Function<Puyo[][], Integer> func) {
		int potential = countChain(puyoArray);
		if (0 < potential) {
			// 連鎖が発動する場合は、ポテンシャルが0になると考える
			return 0;
		}
		Puyo[][] dest = new Puyo[Box.ROW][Box.RANK];
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				if (puyoArray[i][j] != Puyo.NONE) {
					continue;
				}
				for (Direction d : Direction.ALL) {
					Puyo p = getNearPuyo(puyoArray, i, j, d);
					if (p != Puyo.NONE) {
						copyArray(puyoArray, dest);
						dest[i][j] = p;
						int tmpp = func.apply(dest);
						if (tmpp > potential) {
							potential = tmpp;
						}
					}
				}
			}
		}
		return potential;
	}

	public static int countChain(Puyo[][] puyoArray) {
		Puyo[][] dest = new Puyo[Box.ROW][Box.RANK];
		copyArray(puyoArray, dest);
		fall(dest);
		int count = 0;
		while (true) {
			int e = erase1(dest);
			if (e <= 0) {
				break;
			}
			count++;
			fall(dest);
		}
		return count;
	}

	// スコア
	public static int erase1(Puyo[][] puyoArray) {
		// 消去処理

		// 計算用の配列作成
		Puyo[] tmparr = new Puyo[Box.ROW * Box.RANK];
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				if (puyoArray[i][j] == Puyo.OJM) {
					tmparr[i + j * Box.ROW] = Puyo.NONE;
				} else {
					tmparr[i + j * Box.ROW] = puyoArray[i][j];
				}
			}
		}
		int index = 0;
		// 消したぷよの総数
		int count = 0;
		// 色数
		Set<Puyo> set = new HashSet<>();
		// 連結ボーナス
		int connBonus = 0;
		while (index < Box.ROW * Box.RANK) {
			Puyo p = tmparr[index];
			if (p == Puyo.NONE) {
			} else {
				Set<Integer> founded = search(index, tmparr);
				if (founded.size() >= 4) {
					for (int i : founded) {
						count++;
						set.add(tmparr[i]);
						tmparr[i] = Puyo.NONE;
						puyoArray[i % Box.ROW][i / Box.ROW] = Puyo.NONE;
						// 消したぷよの周りにおじゃまぷよがいたら削除
						eraseOJM(puyoArray, i % Box.ROW, i / Box.ROW);
					}
				}
			}
			index++;
		}
		return count;
	}

	private static void eraseOJM(Puyo[][] puyoArray, int row, int rank) {
		List<int[]> checks = new ArrayList<>();
		if (0 < row) {
			checks.add(new int[] { row - 1, rank });
		}
		if (0 < rank) {
			checks.add(new int[] { row, rank - 1 });
		}
		if (row < Box.ROW - 1) {
			checks.add(new int[] { row + 1, rank });
		}
		if (rank < Box.RANK - 1) {
			checks.add(new int[] { row, rank + 1 });
		}
		for (int[] pos : checks) {
			if (puyoArray[pos[0]][pos[1]] == Puyo.OJM) {
				puyoArray[pos[0]][pos[1]] = Puyo.NONE;
			}
		}
	}

	private static Set<Integer> search(int index, Puyo[] tmparr) {
		Puyo p = tmparr[index];
		// 探索結果indexセット
		Set<Integer> founded = new HashSet<>();
		// 探索済みindexセット
		Set<Integer> searched = new HashSet<>();
		// 探索候補キュー
		Queue<Integer> que = new LinkedList<>();
		// 幅優先探索を行う
		que.offer(index);
		searched.add(index);
		while (!que.isEmpty()) {
			int i = que.poll();
			if (tmparr[i] != p) {
				continue;
			}
			founded.add(i);
			int row = i % Box.ROW;
			int rank = i / Box.ROW;
			List<Integer> near = new ArrayList<>();
			// 右隣(右端以外の場合)
			if (row < Box.ROW - 1) {
				near.add(i + 1);
			}
			// 左隣(左端以外の場合)
			if (row > 0) {
				near.add(i - 1);
			}
			// 上隣(上端以外の場合)
			if (rank < Box.RANK - 1) {
				near.add(i + Box.ROW);
			}
			// 下隣(下端以外の場合)
			if (rank > 0) {
				near.add(i - Box.ROW);
			}
			for (int n : near) {
				// 探索済みでない場合
				if (!searched.contains(n)) {
					que.offer(n);
					searched.add(n);
				}
			}
		}
		return founded;
	}

	public static void fall(Puyo[][] puyoArray) {
		while (fall1(puyoArray))
			;
	}

	private static boolean fall1(Puyo[][] puyoArray) {
		boolean flag = false;
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 1; j < Box.RANK; j++) {
				if (puyoArray[i][j - 1] == Puyo.NONE && puyoArray[i][j] != Puyo.NONE) {
					puyoArray[i][j - 1] = puyoArray[i][j];
					puyoArray[i][j] = Puyo.NONE;
					flag = true;
				}
			}
		}
		return flag;
	}

	public static int[] countConnext2(Puyo[][] puyoArray) {
		// 消去処理
		// 計算用の配列作成
		Puyo[][] tmparr = new Puyo[Box.ROW + 2][Box.RANK + 2];
		for (int i = 0; i < Box.ROW + 2; i++) {
			for (int j = 0; j < Box.RANK + 2; j++) {
				if (i == 0 || i == Box.ROW + 1) {
					tmparr[i][j] = Puyo.NONE;
				} else if (j == 0 || j == Box.RANK + 1) {
					tmparr[i][j] = Puyo.NONE;
				} else {
					tmparr[i][j] = puyoArray[i - 1][j - 1];
				}
			}
		}
		// カウンタ配列
		int[][] coun = new int[Box.ROW + 2][Box.RANK + 2];
		// 以下の規則に従って値を設定
		// Puyo.NONE の場合、0
		// Puyo.NONE 以外の場合、隣接する同色ぷよの数+1
		for (int i = 0; i < Box.ROW + 2; i++) {
			for (int j = 0; j < Box.RANK + 2; j++) {
				if (tmparr[i][j] == Puyo.NONE) {
					coun[i][j] = 0;
				} else {
					coun[i][j] = 1;
					coun[i][j] += (tmparr[i + 1][j] == tmparr[i][j] ? 1 : 0);
					coun[i][j] += (tmparr[i - 1][j] == tmparr[i][j] ? 1 : 0);
					coun[i][j] += (tmparr[i][j + 1] == tmparr[i][j] ? 1 : 0);
					coun[i][j] += (tmparr[i][j - 1] == tmparr[i][j] ? 1 : 0);
				}
			}
		}
		int count2 = 0;
		int count3 = 0;
		for (int i = 1; i < Box.ROW + 1; i++) {
			for (int j = 1; j < Box.RANK + 1; j++) {
				if (coun[i][j] == 2) {
					count2++;
				}
				if (coun[i][j] == 3) {
					count3++;
				}
			}
		}
		return new int[] { count2 / 2, count3 / 3 };
	}

	private static void initArray(Puyo[][] array) {
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				array[i][j] = Puyo.NONE;
			}
		}
	}

	private static void copyArray(Puyo[][] src, Puyo[][] dest) {
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				dest[i][j] = src[i][j];
			}
		}
	}
}
