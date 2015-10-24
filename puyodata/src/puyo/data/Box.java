package puyo.data;

import java.util.ArrayList;
import java.util.List;

public class Box {

	public static final int ROW = 6;
	public static final int RANK = 13;

	public enum BoxState {
		READY, DOWN, EFFECT, END
	}

	private String name;
	private BoxState state;
	private int sequence;
	private PuyoState currentPuyo;
	private int score;
	private FallSpeed fallSpeed;
	private int ojmCount;
	private int storageOjmCount;
	private int fallCount;
	private int effectCount;
	private int chainCount;
	private boolean ojmFall;
	private Cluster nextPuyo;
	private List<Puyo> stack;
	private List<Integer> ojms;

	public Box() {
		stack = new ArrayList<>();
		ojms = new ArrayList<>();
		fallSpeed = FallSpeed.NORMAL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BoxState getState() {
		return state;
	}

	public void setState(BoxState state) {
		this.state = state;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public PuyoState getCurrentPuyo() {
		return currentPuyo;
	}

	public void setCurrentPuyo(PuyoState currentPuyo) {
		this.currentPuyo = currentPuyo;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public FallSpeed getFallSpeed() {
		return fallSpeed;
	}

	public void setFallSpeed(FallSpeed fallSpeed) {
		this.fallSpeed = fallSpeed;
	}

	public int getOjmCount() {
		return ojmCount;
	}

	public void setOjmCount(int ojmCount) {
		this.ojmCount = ojmCount;
	}

	public int getStorageOjmCount() {
		return storageOjmCount;
	}

	public void setStorageOjmCount(int storageOjmCount) {
		this.storageOjmCount = storageOjmCount;
	}

	public int getFallCount() {
		return fallCount;
	}

	public void setFallCount(int fallCount) {
		this.fallCount = fallCount;
	}

	public int getChainCount() {
		return chainCount;
	}

	public void setChainCount(int chainCount) {
		this.chainCount = chainCount;
	}

	public boolean isOjmFall() {
		return ojmFall;
	}

	public void setOjmFall(boolean ojmFall) {
		this.ojmFall = ojmFall;
	}

	public int getEffectCount() {
		return effectCount;
	}

	public void setEffectCount(int effectCount) {
		this.effectCount = effectCount;
	}

	public Cluster getNextPuyo() {
		return nextPuyo;
	}

	public void setNextPuyo(Cluster nextPuyo) {
		this.nextPuyo = nextPuyo;
	}

	public List<Puyo> getStack() {
		return stack;
	}

	public void setStack(List<Puyo> stack) {
		this.stack = stack;
	}

	public List<Integer> getOjms() {
		return ojms;
	}

	public void setOjms(List<Integer> ojms) {
		this.ojms = ojms;
	}

	public Puyo getPuyo(int row, int rank) {
		int index = rank * ROW + row;
		if (stack.size() <= index || index < 0) {
			return Puyo.NONE;
		}
		return stack.get(index);
	}

	public void setPuyo(int row, int rank, Puyo puyo) {
		int index = rank * ROW + row;
		stack.set(index, puyo);
	}

	public boolean isExists(int row, int rank) {
		return (getPuyo(row, rank) != Puyo.NONE);
	}

	public void createPuyoArray(Puyo[][] arr) {
		// arrは、ROW×RANKであること
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < RANK; j++) {
				int index = j * ROW + i;
				arr[i][j] = stack.get(index);
			}
		}
	}

	public List<int[]> getOJMPos() {
		List<int[]> list = new ArrayList<>();
		for (int index : ojms) {
			int rank = index / ROW;
			int row = index % ROW;
			int[] a = new int[] { row, rank };
			list.add(a);
		}
		return list;
	}
}
