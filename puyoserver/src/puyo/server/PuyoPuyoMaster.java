package puyo.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import puyo.common.Pair;
import puyo.common.Tuple;
import puyo.data.Action;
import puyo.data.Box;
import puyo.data.Box.BoxState;
import puyo.data.Cluster;
import puyo.data.FallSpeed;
import puyo.data.Game;
import puyo.data.Message;
import puyo.data.Message.MessageId;
import puyo.data.Puyo;
import puyo.data.PuyoState;
import puyo.data.PuyoState.Rotate;
import puyo.data.User;

public class PuyoPuyoMaster {

	public static final int OJM_MAX = 30;
	public static/*final*/int FALL_MAX = 32;
	private static final int EFFECT_MAX = 10;
	public static final int OJM_RATE = 70;

	public static final int[] CHAIN_BONUS = { 0, 8, 16, 32, 64, 96, 128, 160, 192, 224, 256, 288, 320, 352, 384, 416,
			448,
			480, 512 };
	public static final int[] CONNECT_BONUS = { 0, 0, 0, 0, 0, 2, 3, 4, 5, 6, 7, 10, 10, 10, 10, 10, 10, 10, 10, 10,
			10, 10, 10, 10, 10, 10, 10 };
	public static final int[] COLOR_BONUS = { 0, 0, 3, 6, 12, 24 };

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
	private int frameMax = 0;
	private int frameCount = 0;

	public PuyoPuyoMaster() {
		init();
	}

	public void init() {
		state = State.READY;
		frameCount = 0;
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
			frameCount++;
			boolean needUpdate = updateBox(game.getBox1(), game.getBox2(), puyoArray1);
			needUpdate |= updateBox(game.getBox2(), game.getBox1(), puyoArray2);
			if (needUpdate) {
				sendUpdate(game);
			}
			if (frameCount >= getFrameMax()) {
				loser = null;
				endGame();
			}
		}
	}

	private void endGame() {
		game.getBox1().setState(BoxState.END);
		game.getBox2().setState(BoxState.END);
		state = State.END;
		sendEnd(game);
	}

	private boolean updateBox(Box box, Box targetBox, PuyoEx[][] puyoArray) {
		if (state == State.END) {
			return false;
		}
		boolean needUpdate = false;
		switch (box.getState()) {
		case DOWN:
			// 落下カウント減らす
			if (box.getFallCount() > 0) {
				int tmp = box.getFallCount();
				if (box.getFallSpeed() == FallSpeed.FAST && box.getFallCount() > 3) {
					box.setFallCount(box.getFallCount() - 4);
				} else {
					box.setFallCount(box.getFallCount() - 1);
				}
				// 5をまたいだら移動
				if (box.getFallCount() % 5 + (tmp - box.getFallCount()) >= 5) {
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
				// 解析用log
				ServerMain.INSTANCE.logging(box);
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
						box.setScore(box.getScore() + e);
						// おじゃまぷよ追加
						// 相殺判定
						if (box.getOjmCount() >= e) {
							box.setOjmCount(box.getOjmCount() - e);
						} else if (box.getOjmCount() > 0) {
							int e2 = e - box.getOjmCount();
							box.setOjmCount(0);
							if (box.getStorageOjmCount() >= e2) {
								box.setStorageOjmCount(box.getStorageOjmCount() - e2);
								e2 = 0;
							} else if (box.getStorageOjmCount() > 0) {
								e2 = e2 - box.getStorageOjmCount();
								box.setStorageOjmCount(0);
							}
							targetBox.setStorageOjmCount(targetBox.getStorageOjmCount() + e2);
							//targetBox.setOjmCount(targetBox.getOjmCount() + e2);
						} else {
							if (box.getStorageOjmCount() >= e) {
								box.setStorageOjmCount(box.getStorageOjmCount() - e);
								e = 0;
							} else if (box.getStorageOjmCount() > 0) {
								e = e - box.getStorageOjmCount();
								box.setStorageOjmCount(0);
							}
							targetBox.setStorageOjmCount(targetBox.getStorageOjmCount() + e);
							//targetBox.setOjmCount(targetBox.getOjmCount() + e);
						}
					} else if (isEnd(box, puyoArray)) {
						loser = box.getName();
						box.setChainCount(0);
						endGame();
						needUpdate = true;
					} else {
						// 消去処理終了後
						// おじゃまぷよの追加判定
						if (box.getOjmCount() >= OJM_RATE && !box.isOjmFall()) {
							createOJM(box, puyoArray);
							box.setOjmFall(true);
							box.setEffectCount(EFFECT_MAX);
						} else {
							box.setOjmFall(false);
							box.setChainCount(0);
							next(box);
							box.setState(BoxState.DOWN);
							box.setFallCount(FALL_MAX);
							box.setEffectCount(0);
						}
						// 相手に送るおじゃまぷよを確定する。
						if (targetBox.getStorageOjmCount() > 0) {
							targetBox.setOjmCount(targetBox.getOjmCount() + targetBox.getStorageOjmCount());
							targetBox.setStorageOjmCount(0);
						}
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
		if (puyoArray[Box.ROW / 2 - 1][Box.RANK - 2].puyo != Puyo.NONE
				&& puyoArray[Box.ROW / 2 - 1][Box.RANK - 2].type == PuyoType.STATIC)
			return true;

		//if (puyoArray[Box.ROW / 2][Box.RANK - 2].puyo != Puyo.NONE
		//		&& puyoArray[Box.ROW / 2][Box.RANK - 2].type == PuyoType.STATIC)
		//	return true;
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
		for (int[] pos : box.getOJMPos()) {
			puyoArray[pos[0]][pos[1]].puyo = Puyo.OJM;
			puyoArray[pos[0]][pos[1]].type = PuyoType.FALL;
		}
	}

	private void updatePuyoFromPuyoArray(Box box, PuyoEx[][] puyoArray) {
		box.getOjms().clear();
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				if (puyoArray[i][j].type == PuyoType.STATIC) {
					box.setPuyo(i, j, puyoArray[i][j].puyo);
				} else if (puyoArray[i][j].puyo == Puyo.OJM) {
					box.getOjms().add(j * Box.ROW + i);
				} else {
					box.setPuyo(i, j, Puyo.NONE);
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
	// おじゃまぷよが残っていれば続行
	private boolean continueEffect(Box box, PuyoEx[][] puyoArray) {
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				PuyoEx puyo = puyoArray[i][j];
				if (puyo.type == PuyoType.FALL) {
					return true;
				}
			}
		}
		// 解析用log
		ServerMain.INSTANCE.logging(box);
		return false;
	}

	private void createOJM(Box box, PuyoEx[][] puyoArray) {
		int ojm = box.getOjmCount();
		// 左端からおじゃまぷよを充填
		// 最大30個まで生成
		int ojmCreated = 0;
		out: for (int j = 1; j <= Box.RANK; j++) {
			for (int i = 0; i < Box.ROW; i++) {
				if (puyoArray[i][Box.RANK - j].puyo == Puyo.NONE
						&& puyoArray[i][Box.RANK - j].type == PuyoType.STATIC) {
					ojm -= OJM_RATE;
					puyoArray[i][Box.RANK - j].puyo = Puyo.OJM;
					puyoArray[i][Box.RANK - j].type = PuyoType.FALL;
				}
				ojmCreated += 1;
				if (ojmCreated >= OJM_MAX) {
					break out;
				}
				if (ojm < OJM_RATE) {
					break out;
				}
			}
		}
		box.setOjmCount(ojm);
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

	// スコア
	private int erase(Box box, PuyoEx[][] puyoArray) {
		// 消去処理

		// 計算用の配列作成
		Puyo[] tmparr = new Puyo[Box.ROW * Box.RANK];
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				if (puyoArray[i][j].puyo == Puyo.OJM) {
					tmparr[i + j * Box.ROW] = Puyo.NONE;
				} else {
					tmparr[i + j * Box.ROW] = puyoArray[i][j].puyo;
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
						puyoArray[i % Box.ROW][i / Box.ROW].puyo = Puyo.NONE;
						// 消したぷよの周りにおじゃまぷよがいたら削除
						eraseOJM(box, puyoArray, i % Box.ROW, i / Box.ROW);
					}
					connBonus += CONNECT_BONUS[founded.size()];
				}
			}
			index++;
		}
		updatePuyoArray(box, puyoArray);

		// スコア計算
		int score = count * (CHAIN_BONUS[box.getChainCount()] + COLOR_BONUS[set.size()] + connBonus);
		if (score <= 0) {
			score = count;
		}
		return score * 10;
	}

	private void eraseOJM(Box box, PuyoEx[][] puyoArray, int row, int rank) {
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
			if (puyoArray[pos[0]][pos[1]].puyo == Puyo.OJM) {
				puyoArray[pos[0]][pos[1]].puyo = Puyo.NONE;
			}
		}
	}

	private Set<Integer> search(int index, Puyo[] tmparr) {
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

	private void next(Box box) {
		// シーケンス番号進める
		box.setSequence(box.getSequence() + 1);
		// 次のぷよを取得
		Cluster np = getCluster(box.getSequence());
		box.setCurrentPuyo(new PuyoState(np, Rotate.R0, Box.ROW / 2 - 1, Box.RANK - 2));
		// 次の次のぷよを取得
		Cluster nnp = getCluster(box.getSequence() + 1);
		box.setNextPuyo(nnp);
		// 落下速度を初期化
		box.setFallSpeed(FallSpeed.NORMAL);
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
		case GET:
			receiveGet(ip, message);
			break;
		default:
			break;
		}
	}

	private boolean isLogined(String name) {
		return users.stream().anyMatch(u -> u.getName().equals(name));
	}

	private String createToken(User user) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] data = user.getName().getBytes();
			md.update(data);
			byte[] digest = md.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < digest.length; i++) {
				int b = (0xFF & digest[i]);
				if (b < 16)
					sb.append("0");
				sb.append(Integer.toHexString(b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public void receiveLogin(String ip, Message message) {
		int port = message.getUser().getPort();
		User user = message.getUser();
		switch (state) {
		case READY:
			if (users.size() < 2) {
				if (!isLogined(user.getName())) {
					user.setIp(ip);
					user.setPassword(createToken(user));
					users.add(user);
				} else {
					final String name = user.getName();
					user = users.stream().filter(u -> u.getName().equals(name)).findFirst().orElse(null);
				}
				Message response = new Message(MessageId.LOGIN_OK);
				response.setUser(user);
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
			if (register(message.getUser())) {
				if (checkAction(message)) {
					User user = message.getUser();
					Action action = message.getAction();
					actionMap.put(user.getName(), action);
					Box box = game.selectBox(user.getName());
					box.setFallSpeed(action.getFallSpeed());
					Message response = new Message(MessageId.ACTION_OK);
					send(ip, port, response);
					return;
				}
			}
			break;
		default:
			break;
		}
		Message response = new Message(MessageId.ACTION_NG);
		send(ip, port, response);
	}

	public void receiveGet(String ip, Message message) {
		User user = message.getUser();
		Message response = new Message(MessageId.UPDATE);
		response.setGame(game);
		send(user.getIp(), user.getPort(), response);
	}

	/**
	 * @return true:認証OK false:認証NG
	 */
	private boolean register(User user) {
		if (user == null) {
			return false;
		}
		if (!users.stream().anyMatch(u -> (
				u.getName().equals(user.getName()) && u.getPassword().equals(user.getPassword())
				))) {
			return false;
		}
		return true;
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

	public int getFrameMax() {
		return frameMax;
	}

	public void setFrameMax(int frameMax) {
		this.frameMax = frameMax;
	}

}
