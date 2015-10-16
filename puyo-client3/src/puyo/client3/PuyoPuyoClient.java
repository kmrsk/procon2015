package puyo.client3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import puyo.data.Action;
import puyo.data.Box;
import puyo.data.Box.BoxState;
import puyo.data.Game;
import puyo.data.Message;
import puyo.data.Message.MessageId;
import puyo.data.User;

public class PuyoPuyoClient {

	private enum State {
		READY, LOGIN, GAMING, GAMEEND
	}

	private List<Message> receivedBox;
	private List<Message> sendBox;
	private Game game;
	private State state;
	private User user;
	private Box box;
	private PuyoPuyoAI ai;

	public PuyoPuyoClient() {
		receivedBox = Collections.synchronizedList(new ArrayList<>());
		sendBox = Collections.synchronizedList(new ArrayList<>());
		state = State.READY;
		ai = new PuyoPuyoAIImpl();
	}

	public void update() {
		synchronized (receivedBox) {
			receivedBox.forEach(this::receive);
			receivedBox.clear();
		}
		switch (state) {
		case GAMING:
			break;
		case LOGIN:
			break;
		case READY:
			sendLogin(user);
			break;
		case GAMEEND:
			break;
		default:
			break;
		}
	}

	public void receive(Message message) {
		if (message.getGame() != null) {
			game = message.getGame();
		}
		switch (message.getId()) {
		case LOGIN_OK:
			state = State.LOGIN;
			user = message.getUser();
			break;
		case LOGIN_NG:
			break;
		case ACTION_OK:
			break;
		case ACTION_NG:
			createAction(message);
			break;
		case START:
			receiveUpdate(message);
			break;
		case UPDATE:
			receiveUpdate(message);
			break;
		case END:
			state = State.GAMEEND;
			System.exit(0);
			break;
		default:
			break;
		}
	}

	private void receiveUpdate(Message message) {
		if (state != State.GAMEEND) {
			state = State.GAMING;
			if (game != null) {
				Box tmpbox = game.selectBox(user.getName());
				if (box == null || tmpbox.getSequence() != box.getSequence()) {
					box = tmpbox;
					createAction(message);
				}
			}
		}
	}

	private void createAction(Message message) {
		Box box = game.selectBox(user.getName());
		if (box.getState() == BoxState.DOWN) {
			new Thread(() -> {
				Action action = ai.createAction(game, user);
				sendAction(action);
			}).start();
		}
	}

	private void send(Message message) {
		sendBox.add(message);
	}

	private void sendLogin(User user) {
		Message response = new Message(MessageId.LOGIN);
		response.setUser(user);
		send(response);
	}

	protected void sendAction(Action action) {
		if (state != State.GAMEEND) {
			Message response = new Message(MessageId.ACTION);
			response.setUser(user);
			response.setAction(action);
			send(response);
		}
	}

	public List<Message> getReceivedBox() {
		return receivedBox;
	}

	public List<Message> getSendBox() {
		return sendBox;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
