package puyo.data;

public class Action {

	private int sequence;
	private PuyoState state;

	public Action() {
	}

	public Action(int sequence, PuyoState state) {
		super();
		this.sequence = sequence;
		this.state = state;
	}

	public PuyoState getState() {
		return state;
	}

	public void setState(PuyoState state) {
		this.state = state;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
