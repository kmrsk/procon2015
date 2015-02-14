package puyo.data;

public class Cluster {

	private Puyo first;
	private Puyo second;

	public Cluster() {
	}

	public Cluster(Puyo first, Puyo second) {
		super();
		this.first = first;
		this.second = second;
	}

	public Puyo getFirst() {
		return first;
	}

	public void setFirst(Puyo first) {
		this.first = first;
	}

	public Puyo getSecond() {
		return second;
	}

	public void setSecond(Puyo second) {
		this.second = second;
	}

}
