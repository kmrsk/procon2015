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
		//System.out.println("createAction AI");
		Box box = game.selectBox(user.getName());
		Action action = new Action();
		
		PuyoState puyoState = box.getCurrentPuyo();
		action.setSequence(box.getSequence());
		
		int row = puyoState.getRow();
		if (MainFrame.keyState[MainFrame.LEFT] && row > 0) {
			int rowLower = 1;
			if (puyoState.getRotate() == Rotate.R180) {
				rowLower++;
			}
			if (row >= rowLower) {
				puyoState.setRow(row - 1);
			}
			MainFrame.keyState[MainFrame.LEFT] = false;
		} else if (MainFrame.keyState[MainFrame.RIGHT]) {
			int rowUpper = Box.ROW - 2;
			if (puyoState.getRotate() == Rotate.R0) {
				rowUpper--;
			}
			if (row <= rowUpper) {
				puyoState.setRow(row + 1);
			}
			MainFrame.keyState[MainFrame.RIGHT] = false;
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
			if (rotate == Rotate.R0 && row == Box.ROW - 1) {
				// 何もしない
			} else if (rotate == Rotate.R180 && row == 0) {
				// 何もしない
			} else {
				puyoState.setRotate(rotate);
			}
			MainFrame.keyState[MainFrame.ROTATE] = false;
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
			if (rotate == Rotate.R0 && row == Box.ROW - 1) {
				// 何もしない
			} else if (rotate == Rotate.R180 && row == 0) {
				// 何もしない
			} else {
				puyoState.setRotate(rotate);
			}
			MainFrame.keyState[MainFrame.RROTATE] = false;
		} else {
			return null;
		}

		action.setState(puyoState);
		return action;
	}
}
