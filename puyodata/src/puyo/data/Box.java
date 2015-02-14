package puyo.data;

import java.util.ArrayList;
import java.util.List;

public class Box {

	public static final int ROW = 6;
	public static final int RANK = 12;

	public enum BoxState {
		READY, DOWN, EFFECT, END
	}

	private String name;
	private BoxState state;
	private int sequence;
	private PuyoState currentPuyo;
	private int fallCount;
	private int effectCount;
	private int chainCount;
	private Cluster nextPuyo;
	private List<Puyo> stack;

	public Box() {
		stack = new ArrayList<>();
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
}
