package puyo.data;

public class Action {

	private int sequence;
	private PuyoState state;
	private FallSpeed fallSpeed;

	public Action() {
		fallSpeed = FallSpeed.NORMAL;
	}

	public Action(int sequence, PuyoState state) {
		super();
		this.sequence = sequence;
		this.state = state;
		fallSpeed = FallSpeed.NORMAL;
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

	public FallSpeed getFallSpeed() {
		return fallSpeed;
	}

	public void setFallSpeed(FallSpeed fallSpeed) {
		this.fallSpeed = fallSpeed;
	}

}
