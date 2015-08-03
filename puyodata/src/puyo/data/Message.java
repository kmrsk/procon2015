package puyo.data;

public class Message {
	public enum MessageId {
		LOGIN, LOGIN_OK, LOGIN_NG,
		START,
		UPDATE,
		ACTION, ACTION_OK, ACTION_NG,
		GET,
		END
	}

	private MessageId id;
	private Game game;
	private Action action;
	private User user;

	public Message() {
	}

	public Message(MessageId id) {
		super();
		this.id = id;
	}

	public MessageId getId() {
		return id;
	}

	public void setId(MessageId id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
