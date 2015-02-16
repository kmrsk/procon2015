package puyo.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import puyo.common.Pair;
import puyo.common.Tuple;
import puyo.data.Action;
import puyo.data.Box;
import puyo.data.Box.BoxState;
import puyo.data.Cluster;
import puyo.data.Game;
import puyo.data.Message;
import puyo.data.Message.MessageId;
import puyo.data.Puyo;
import puyo.data.PuyoState;
import puyo.data.PuyoState.Rotate;
import puyo.data.User;

public class PuyoPuyoMaster {

	public static final int FALL_MAX = 30;
	private static final int EFFECT_MAX = 10;

	private enum State {
		READY, GAMING, END
	}

	enum PuyoType {
		STATIC, FALL
	}

	class PuyoEx {
		public Puyo puyo;
		public PuyoType type;

		public PuyoEx(Puyo puyo, PuyoType type) {
			super();
			this.puyo = puyo;
			this.type = type;
		}
	}

	private State state;
	private Game game;
	private List<User> users;
	private List<Pair<String, Message>> receivedBox;
	private List<Tuple<String, Integer, Message>> sendBox;
	private Map<String, Action> actionMap;
	private List<Cluster> puyolist;
	private PuyoEx[][] puyoArray1;
	private PuyoEx[][] puyoArray2;
	private String loser;

	public PuyoPuyoMaster() {
		init();
	}

	public void init() {
		state = State.READY;
		loser = null;
		users = new ArrayList<>();
		receivedBox = Collections.synchronizedList(new ArrayList<>());
		sendBox = Collections.synchronizedList(new ArrayList<>());
		actionMap = Collections.synchronizedMap(new HashMap<>());
		puyolist = Collections.synchronizedList(new ArrayList<>());
		game = new Game();
		game.setBox1(new Box());
		game.setBox2(new Box());
		puyoArray1 = new PuyoEx[Box.ROW][Box.RANK];
		puyoArray2 = new PuyoEx[Box.ROW][Box.RANK];
		initPuyoArray(puyoArray1);
		initPuyoArray(puyoArray2);
	}

	private void initPuyoArray(PuyoEx[][] puyoArray) {
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				PuyoEx pe = new PuyoEx(Puyo.NONE, PuyoType.STATIC);
				puyoArray[i][j] = pe;
			}
		}
	}

	private void startGame() {
		Box box1 = game.getBox1();
		initBox(box1, users.get(0).getName());
		Box box2 = game.getBox2();
		initBox(box2, users.get(1).getName());
	}

	private void initBox(Box box, String name) {
		box.setChainCount(0);
		box.setEffectCount(0);
		box.setFallCount(0);
		box.setName(name);
		box.setState(BoxState.READY);
		box.getStack().clear();
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				box.getStack().add(Puyo.NONE);
			}
		}
		box.setSequence(-1);
		next(box);
	}

	public void update() {
		synchronized (receivedBox) {
			receivedBox.forEach(this::receive);
			receivedBox.clear();
		}
		if (state == State.GAMING) {
			boolean needUpdate = updateBox(game.getBox1(), puyoArray1);
			needUpdate |= updateBox(game.getBox2(), puyoArray2);
			if (needUpdate) {
				sendUpdate(game);
			}
		}
	}

	private boolean updateBox(Box box, PuyoEx[][] puyoArray) {
		if (state == State.END) {
			return false;
		}
		boolean needUpdate = false;
		switch (box.getState()) {
		case DOWN:
			// 落下カウント減らす
			if (box.getFallCount() > 0) {
				box.setFallCount(box.getFallCount() - 1);
				if (box.getFallCount() % 10 == 5) {
					move(box);
					convertPuyoArrayFromBox(box, puyoArray);
				}
			} else {
				fall(box);
				box.setFallCount(FALL_MAX);
				convertPuyoArrayFromBox(box, puyoArray);
				needUpdate = true;
			}
			if (isLanding(box)) {
				box.setState(BoxState.EFFECT);
				box.setEffectCount(EFFECT_MAX);
				box.setFallCount(0);
				landing(box, puyoArray);
				updatePuyoFromPuyoArray(box, puyoArray);
				needUpdate = true;
			}
			break;
		case EFFECT:
			// エフェクトカウント減らす
			if (box.getEffectCount() > 0) {
				box.setEffectCount(box.getEffectCount() - 1);
			} else {
				// エフェクトを継続させるかどうかの判断
				if (continueEffect(box, puyoArray)) {
					// エフェクトを1カウント進める
					stepEffect(box, puyoArray);
					box.setEffectCount(EFFECT_MAX);
				} else {
					// 消去処理
					int e = erase(box, puyoArray);
					if (e > 0) {
						box.setChainCount(box.getChainCount() + 1);
						box.setState(BoxState.EFFECT);
						box.setEffectCount(EFFECT_MAX);
					} else if (isEnd(box, puyoArray)) {
						loser = box.getName();
						box.setChainCount(0);
						game.getBox1().setState(BoxState.END);
						game.getBox2().setState(BoxState.END);
						state = State.END;
						sendEnd(game);
						needUpdate = true;
					} else {
						box.setChainCount(0);
						next(box);
						box.setState(BoxState.DOWN);
						box.setFallCount(FALL_MAX);
						box.setEffectCount(0);
					}
					updatePuyoFromPuyoArray(box, puyoArray);
				}
				needUpdate = true;
			}
			break;
		case END:
			break;
		case READY:
			box.setState(BoxState.DOWN);
			needUpdate = true;
			break;
		default:
			break;
		}
		return needUpdate;
	}

	private boolean isEnd(Box box, PuyoEx[][] puyoArray) {
		if (puyoArray[Box.ROW / 2 - 1][Box.RANK - 1].puyo != Puyo.NONE
				&& puyoArray[Box.ROW / 2 - 1][Box.RANK - 1].type == PuyoType.STATIC)
			return true;

		if (puyoArray[Box.ROW / 2][Box.RANK - 1].puyo != Puyo.NONE
				&& puyoArray[Box.ROW / 2][Box.RANK - 1].type == PuyoType.STATIC)
			return true;
		return false;
	}

	private void move(Box box) {
		Action action = actionMap.get(box.getName());
		if (action == null) {
			return;
		}
		if (box.getSequence() == action.getSequence()) {
			int row = box.getCurrentPuyo().getRow();
			//int rank = box.getCurrentPuyo().getRank();
			int trow = action.getState().getRow();
			//int trank = action.getState().getRank();
			if (row < trow) {
				int[] pos1 = box.getCurrentPuyo().getPuyoPosition(0);
				int[] pos2 = box.getCurrentPuyo().getPuyoPosition(1);
				if (box.isExists(pos1[0] + 1, pos1[1]) || box.isExists(pos2[0] + 1, pos2[1])) {
					actionMap.put(box.getName(), null);
				} else {
					box.getCurrentPuyo().setRow(row + 1);
					if (box.getCurrentPuyo().getRotate() == Rotate.R0 && row >= Box.ROW - 2) {
						box.getCurrentPuyo().setRotate(action.getState().getRotate());
					}
				}
			} else if (row > trow) {
				int[] pos1 = box.getCurrentPuyo().getPuyoPosition(0);
				int[] pos2 = box.getCurrentPuyo().getPuyoPosition(1);
				if (box.isExists(pos1[0] - 1, pos1[1]) || box.isExists(pos2[0] - 1, pos2[1])) {
					actionMap.put(box.getName(), null);
				} else {
					box.getCurrentPuyo().setRow(row - 1);
				}
			} else {
				// 回転を実行
				box.getCurrentPuyo().setRotate(action.getState().getRotate());
				actionMap.put(box.getName(), null);
			}
		} else {
			actionMap.put(box.getName(), null);
		}
	}

	private void fall(Box box) {
		int rank = box.getCurrentPuyo().getRank();
		box.getCurrentPuyo().setRank(rank - 1);
	}

	private void convertPuyoArrayFromBox(Box box, PuyoEx[][] puyoArray) {
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				puyoArray[i][j].puyo = box.getPuyo(i, j);
				puyoArray[i][j].type = PuyoType.STATIC;
			}
		}
		int[] pos1 = box.getCurrentPuyo().getPuyoPosition(0);
		int[] pos2 = box.getCurrentPuyo().getPuyoPosition(1);
		if (pos1[1] < Box.RANK) {
			puyoArray[pos1[0]][pos1[1]].puyo = box.getCurrentPuyo().getCluster().getFirst();
			puyoArray[pos1[0]][pos1[1]].type = PuyoType.FALL;
		}
		if (pos2[1] < Box.RANK) {
			puyoArray[pos2[0]][pos2[1]].puyo = box.getCurrentPuyo().getCluster().getSecond();
			puyoArray[pos2[0]][pos2[1]].type = PuyoType.FALL;
		}
	}

	private void updatePuyoFromPuyoArray(Box box, PuyoEx[][] puyoArray) {
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				if (puyoArray[i][j].type == PuyoType.STATIC) {
					box.setPuyo(i, j, puyoArray[i][j].puyo);
				}
			}
		}
	}

	private void updatePuyoArray(Box box, PuyoEx[][] puyoArray) {
		// 自分の下がNONEならFALL
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 1; j < Box.RANK; j++) {
				if (puyoArray[i][j].puyo != Puyo.NONE && puyoArray[i][j].type == PuyoType.STATIC) {
					if (puyoArray[i][j - 1].puyo == Puyo.NONE && puyoArray[i][j - 1].type == PuyoType.STATIC) {
						puyoArray[i][j].type = PuyoType.FALL;
					} else if (puyoArray[i][j - 1].puyo != Puyo.NONE && puyoArray[i][j - 1].type == PuyoType.FALL) {
						puyoArray[i][j].type = PuyoType.FALL;
					}
				}
			}
		}
	}

	// すべてstaticならエフェクト終了
	private boolean continueEffect(Box box, PuyoEx[][] puyoArray) {
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				PuyoEx puyo = puyoArray[i][j];
				if (puyo.type == PuyoType.FALL) {
					return true;
				}
			}
		}
		return false;
	}

	//
	private void stepEffect(Box box, PuyoEx[][] puyoArray) {
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				PuyoEx puyo = puyoArray[i][j];
				if (puyo.type == PuyoType.FALL) {
					if (j == 0) {
						puyoArray[i][j].type = PuyoType.STATIC;
					} else if (puyoArray[i][j - 1].puyo == Puyo.NONE) {
						puyoArray[i][j - 1].type = PuyoType.FALL;
						puyoArray[i][j - 1].puyo = puyo.puyo;
						puyoArray[i][j].type = PuyoType.STATIC;
						puyoArray[i][j].puyo = Puyo.NONE;
					} else {
						puyoArray[i][j].type = PuyoType.STATIC;
					}
				}
			}
		}
	}

	private int erase(Box box, PuyoEx[][] puyoArray) {
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
					tmparr[i][j] = puyoArray[i - 1][j - 1].puyo;
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
		// 自分に隣接する位置に同色で4以上のものがある→自分と隣接する同色を4に設定
		// 自分が3以上でかつ自分に隣接する位置に同色で3以上のものがある→自分と隣接する同色を4に設定
		for (int i = 0; i < Box.ROW + 2; i++) {
			for (int j = 0; j < Box.RANK + 2; j++) {
				boolean flag = false;
				if (coun[i][j] >= 4) {
					flag = true;
				} else if (coun[i][j] >= 3) {
					flag |= (tmparr[i + 1][j] == tmparr[i][j] && coun[i + 1][j] >= 3);
					flag |= (tmparr[i - 1][j] == tmparr[i][j] && coun[i - 1][j] >= 3);
					flag |= (tmparr[i][j + 1] == tmparr[i][j] && coun[i][j + 1] >= 3);
					flag |= (tmparr[i][j - 1] == tmparr[i][j] && coun[i][j - 1] >= 3);
				} else if (coun[i][j] >= 1) {
					flag |= (tmparr[i + 1][j] == tmparr[i][j] && coun[i + 1][j] >= 4);
					flag |= (tmparr[i - 1][j] == tmparr[i][j] && coun[i - 1][j] >= 4);
					flag |= (tmparr[i][j + 1] == tmparr[i][j] && coun[i][j + 1] >= 4);
					flag |= (tmparr[i][j - 1] == tmparr[i][j] && coun[i][j - 1] >= 4);
				} else {
				}
				if (flag) {
					coun[i][j] = 4;
					if (tmparr[i + 1][j] == tmparr[i][j])
						coun[i + 1][j] = 4;
					if (tmparr[i - 1][j] == tmparr[i][j])
						coun[i - 1][j] = 4;
					if (tmparr[i][j + 1] == tmparr[i][j])
						coun[i][j + 1] = 4;
					if (tmparr[i][j - 1] == tmparr[i][j])
						coun[i][j - 1] = 4;
				}
			}
		}
		int count = 0;
		// 4以上になっているぷよを消去
		for (int i = 1; i < Box.ROW + 1; i++) {
			for (int j = 1; j < Box.RANK + 1; j++) {
				if (coun[i][j] >= 4) {
					puyoArray[i - 1][j - 1].puyo = Puyo.NONE;
					puyoArray[i - 1][j - 1].type = PuyoType.STATIC;
					count++;
				}
			}
		}
		updatePuyoArray(box, puyoArray);
		return count;
	}

	private void next(Box box) {
		// シーケンス番号進める
		box.setSequence(box.getSequence() + 1);
		// 次のぷよを取得
		Cluster np = getCluster(box.getSequence());
		box.setCurrentPuyo(new PuyoState(np, Rotate.R0, Box.ROW / 2 - 1, Box.RANK - 1));
		// 次の次のぷよを取得
		Cluster nnp = getCluster(box.getSequence() + 1);
		box.setNextPuyo(nnp);
	}

	private Cluster getCluster(int index) {
		if (index < puyolist.size()) {
			return puyolist.get(index);
		} else {
			while (index >= puyolist.size()) {
				puyolist.add(new Cluster(Puyo.getRandom(), Puyo.getRandom()));
			}
			return puyolist.get(index);
		}
	}

	private void landing(Box box, PuyoEx[][] puyoArray) {
		// ぷよ1の落下地点を決める
		int[] pos1 = box.getCurrentPuyo().getPuyoPosition(0);
		// ぷよ2の落下地点を決める
		int[] pos2 = box.getCurrentPuyo().getPuyoPosition(1);
		puyoArray[pos1[0]][pos1[1]].puyo = box.getCurrentPuyo().getCluster().getFirst();
		puyoArray[pos1[0]][pos1[1]].type = PuyoType.FALL;
		puyoArray[pos2[0]][pos2[1]].puyo = box.getCurrentPuyo().getCluster().getSecond();
		puyoArray[pos2[0]][pos2[1]].type = PuyoType.FALL;
	}

	private boolean isLanding(Box box) {
		int[] pos1 = box.getCurrentPuyo().getPuyoPosition(0);
		int[] pos2 = box.getCurrentPuyo().getPuyoPosition(1);
		return (pos1[1] == 0 || pos2[1] == 0 || box.isExists(pos1[0], pos1[1] - 1) || box
				.isExists(pos2[0], pos2[1] - 1));
	}

	public void receive(Pair<String, Message> pair) {
		String ip = pair.getFirst();
		Message message = pair.getSecond();
		switch (message.getId()) {
		case ACTION:
			receiveAction(ip, message);
			break;
		case LOGIN:
			receiveLogin(ip, message);
			break;
		default:
			break;
		}
	}

	private boolean isLogined(String name) {
		return users.stream().anyMatch(u -> u.getName().equals(name));
	}

	public void receiveLogin(String ip, Message message) {
		int port = message.getUser().getPort();
		switch (state) {
		case READY:
			if (users.size() < 2) {
				if (!isLogined(message.getUser().getName())) {
					message.getUser().setIp(ip);
					users.add(message.getUser());
				}
				Message response = new Message(MessageId.LOGIN_OK);
				send(ip, port, response);
				if (users.size() == 2) {
					state = State.GAMING;
					startGame();
					sendStart(game);
				}
				return;
			}
			break;
		default:
			break;
		}
		Message response = new Message(MessageId.LOGIN_NG);
		send(ip, port, response);
	}

	public void receiveAction(String ip, Message message) {
		int port = message.getUser().getPort();
		switch (state) {
		case GAMING:
			if (checkAction(message)) {
				User user = message.getUser();
				Action action = message.getAction();
				actionMap.put(user.getName(), action);
				Message response = new Message(MessageId.ACTION_OK);
				send(ip, port, response);
				return;
			}
			break;
		default:
			break;
		}
		Message response = new Message(MessageId.ACTION_NG);
		send(ip, port, response);
	}

	/**
	 * @return true:アクションが有効 false:アクションが無効
	 */
	private boolean checkAction(Message message) {
		User user = message.getUser();
		Action action = message.getAction();
		Box box = game.selectBox(user.getName());
		if (box == null || action == null) {
			System.out.println("check ng1");
			return false;
		}
		// シーケンスが一致しない場合
		if (box.getSequence() != action.getSequence()) {
			System.out.println("check ng2");
			return false;
		}
		// 落下中でない場合
		if (box.getState() != BoxState.DOWN) {
			System.out.println("check ng3");
			return false;
		}
		// ぷよが一致しない場合
		PuyoState ps = box.getCurrentPuyo();
		PuyoState aps = action.getState();
		if (ps.getCluster().getFirst() != aps.getCluster().getFirst()) {
			System.out.println("check ng4");
			return false;
		}
		if (ps.getCluster().getSecond() != aps.getCluster().getSecond()) {
			System.out.println("check ng5");
			return false;
		}
		// 置けない場所を選択した場合(座標が無効,ぷよが邪魔,壁が邪魔)
		// ぷよ1の落下地点
		int[] pos1 = aps.getPuyoPosition(0);
		if (box.isExists(pos1[0], pos1[1])) {
			System.out.println("check ng6");
			return false;
		}
		// ぷよ2の落下地点
		int[] pos2 = aps.getPuyoPosition(1);
		if (box.isExists(pos2[0], pos2[1])) {
			System.out.println("check ng7");
			return false;
		}
		return true;
	}

	private void send(String ip, int port, Message message) {
		sendBox.add(new Tuple<>(ip, port, message));
	}

	private void sendUpdate(Game game) {
		users.forEach(user -> {
			Message response = new Message(MessageId.UPDATE);
			response.setGame(game);
			send(user.getIp(), user.getPort(), response);
		});
	}

	private void sendStart(Game game) {
		users.forEach(user -> {
			Message response = new Message(MessageId.START);
			response.setGame(game);
			send(user.getIp(), user.getPort(), response);
		});
	}

	private void sendEnd(Game game) {
		users.forEach(user -> {
			Message response = new Message(MessageId.END);
			send(user.getIp(), user.getPort(), response);
		});
	}

	public List<Pair<String, Message>> getReceivedBox() {
		return receivedBox;
	}

	public List<Tuple<String, Integer, Message>> getSendBox() {
		return sendBox;
	}

	public String getLoser() {
		return loser;
	}

	public Game getGame() {
		return game;
	}

	public List<User> getUsers() {
		return users;
	}

	public PuyoEx[][] getPuyoArray1() {
		return puyoArray1;
	}

	public PuyoEx[][] getPuyoArray2() {
		return puyoArray2;
	}
}
