package puyo.client;

import java.util.ArrayList;
import java.util.List;

import puyo.data.Action;
import puyo.data.Box;
import puyo.data.Cluster;
import puyo.data.FallSpeed;
import puyo.data.Game;
import puyo.data.Puyo;
import puyo.data.PuyoState;
import puyo.data.PuyoState.Rotate;
import puyo.data.User;

public class PuyoPuyoAIImpl2 implements PuyoPuyoAI {

	private Rotate[] rotates = { Rotate.R0, Rotate.R90, Rotate.R180, Rotate.R270 };

	public PuyoPuyoAIImpl2() {
	}

	private void initArray(Puyo[][] array) {
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				array[i][j] = Puyo.NONE;
			}
		}
	}

	private int evaluation(Box box, PuyoState state) {
		Puyo[][] array = new Puyo[Box.ROW][Box.RANK];
		initArray(array);
		box.createPuyoArray(array);
		int basePotential = AIUtil.countChainPotential1(array);
		int[] pos1 = state.getPuyoPosition(0);
		int[] pos2 = state.getPuyoPosition(1);
		// 落下位置を決める
		array[pos1[0]][pos1[1]] = state.getCluster().getFirst();
		array[pos2[0]][pos2[1]] = state.getCluster().getSecond();
		AIUtil.fall(array);

		int chain = AIUtil.countChain(array);
		if (chain >= 5) {
			return chain * 100;
		}
		int j = Box.RANK - 1;
		for (j = Box.RANK - 1; j >= 9; j--) {
			if (array[2][j] != Puyo.NONE || array[3][j] != Puyo.NONE) {
				break;
			}
		}
		if (j >= 9) {
			return -10;
		}
		int potential = AIUtil.countChainPotential1(array);
		if (basePotential < potential) {
			System.out.println("basePotential:" + basePotential + " potential:" + potential);
			return potential * 100;
		}
		// 連結数の計算
		int[] cnn = AIUtil.countConnext2(array);
		return cnn[0] + cnn[1] * 3;
	}

	private List<PuyoState> getCandidates(Box box) {
		Cluster cl = box.getCurrentPuyo().getCluster();
		List<PuyoState> list = new ArrayList<>();
		for (int i = 0; i < Box.ROW; i++) {
			for (Rotate r : rotates) {
				PuyoState state = new PuyoState(cl, r, i, Box.RANK - 2);
				if (validState(box, state)) {
					list.add(state);
				}
			}
		}
		return list;
	}

	private boolean validState(Box box, PuyoState state) {
		int[] pos1 = state.getPuyoPosition(0);
		int[] pos2 = state.getPuyoPosition(1);
		if (pos1[0] < 0 || pos1[0] >= Box.ROW) {
			return false;
		}
		if (pos2[0] < 0 || pos2[0] >= Box.ROW) {
			return false;
		}
		if (box.isExists(pos1[0], pos1[1]) || box.isExists(pos2[0], pos2[1])) {
			return false;
		}
		return true;
	}

	@Override
	public Action createAction(Game game, User user) {
		System.out.println("createAction AI");
		Box box = game.selectBox(user.getName());
		Cluster cluster = box.getCurrentPuyo().getCluster();
		List<PuyoState> list = getCandidates(box);
		PuyoState candidate = null;
		List<PuyoState> maxCandidates = new ArrayList<>();
		int evaluation = -9999;
		for (PuyoState state : list) {
			int tmp = evaluation(box, state);
			//System.out.println("row:" + state.getRow() + " rotate:" + state.getRotate() + " [" + tmp + "]");
			if (evaluation < tmp) {
				evaluation = tmp;
				maxCandidates.clear();
				maxCandidates.add(state);
			} else if (evaluation == tmp) {
				maxCandidates.add(state);
			}
		}
		int i = (int) Math.floor(Math.random() * 10000) % maxCandidates.size();
		candidate = maxCandidates.get(i);
		Action action = new Action();
		action.setState(candidate);
		action.setSequence(box.getSequence());
		action.setFallSpeed(FallSpeed.FAST);
		return action;
	}
}
