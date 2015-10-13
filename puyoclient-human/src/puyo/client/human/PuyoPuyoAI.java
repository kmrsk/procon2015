package puyo.client.human;


import puyo.data.Action;
import puyo.data.Game;
import puyo.data.User;

public interface PuyoPuyoAI {
	public Action createAction(Game game, User user);
}
