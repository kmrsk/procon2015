package puyo.client.human;


import puyo.data.Action;
import puyo.data.Box;
import puyo.data.FallSpeed;
import puyo.data.Game;
import puyo.data.PuyoState;
import puyo.data.User;
import puyo.data.PuyoState.Rotate;

public class PuyoPuyoAIImpl implements PuyoPuyoAI {
	
	public PuyoPuyoAIImpl() {
	}

	@Override
	public Action createAction(Game game, User user) {
		System.out.println("createAction AI");
		Box box = game.selectBox(user.getName());
		Action action = new Action();
		
		PuyoState puyoState = box.getCurrentPuyo();
		action.setSequence(box.getSequence());
		
		int row = puyoState.getRow();
		if (MainFrame.keyState[MainFrame.LEFT] && row > 0) {
			puyoState.setRow(row - 1);
		} else if (MainFrame.keyState[MainFrame.RIGHT] && row < Box.ROW - 1) {
			puyoState.setRow(row + 1);
		} else if (MainFrame.keyState[MainFrame.DOWN]) {
			action.setFallSpeed(FallSpeed.FAST);
		} else if (MainFrame.keyState[MainFrame.ROTATE]) {
			Rotate rotate = puyoState.getRotate();
			switch (rotate) {
			case R0:
				rotate = Rotate.R90;
				break;
			case R90:
				rotate = Rotate.R180;
				break;
			case R180:
				rotate = Rotate.R270;
				break;
			case R270:
				rotate = Rotate.R0;
				break;
			}
			puyoState.setRotate(rotate);
		} else if (MainFrame.keyState[MainFrame.RROTATE]) {
			Rotate rotate = puyoState.getRotate();
			switch (rotate) {
			case R0:
				rotate = Rotate.R270;
				break;
			case R90:
				rotate = Rotate.R0;
				break;
			case R180:
				rotate = Rotate.R90;
				break;
			case R270:
				rotate = Rotate.R180;
				break;
			}
			puyoState.setRotate(rotate);
		}

		action.setState(puyoState);
		return action;
	}
}
