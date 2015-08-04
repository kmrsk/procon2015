package puyo.client;

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

	private Puyo puyo;

	public PuyoPuyoAIImpl() {
		puyo = Puyo.getRandom();
	}

	@Override
	public Action createAction(Game game, User user) {
		System.out.println("createAction AI");
		Box box = game.selectBox(user.getName());
		Action action = new Action();
		PuyoState puyoState = new PuyoState(
				box.getCurrentPuyo().getCluster(),
				box.getCurrentPuyo().getRotate(),
				box.getCurrentPuyo().getRow(),
				box.getCurrentPuyo().getRank());
		Cluster cluster = puyoState.getCluster();
		if (cluster.getFirst() == puyo && cluster.getSecond() == puyo) {
			puyoState.setRow(0);
			puyoState.setRank(puyoState.getRank() - 1);
			puyoState.setRotate(Rotate.R90);
			action.setState(puyoState);
		} else if (cluster.getSecond() == puyo) {
			puyoState.setRow(1);
			puyoState.setRank(puyoState.getRank() - 1);
			puyoState.setRotate(Rotate.R180);
			action.setState(puyoState);
		} else if (cluster.getFirst() == puyo) {
			puyoState.setRow(0);
			puyoState.setRank(puyoState.getRank() - 1);
			puyoState.setRotate(Rotate.R0);
			action.setState(puyoState);
		} else {
			puyoState.setRow(Box.ROW - 2);
			puyoState.setRank(puyoState.getRank() - 1);
			puyoState.setRotate(Rotate.R0);
			action.setState(puyoState);
		}
		int[] pos1 = puyoState.getPuyoPosition(0);
		int[] pos2 = puyoState.getPuyoPosition(1);
		if (box.isExists(pos1[0], pos1[1]) || box.isExists(pos2[0], pos2[1])) {
			puyoState = box.getCurrentPuyo();
			action.setState(puyoState);
		}
		action.setSequence(box.getSequence());
		action.setFallSpeed(FallSpeed.FAST);
		return action;
	}

}
