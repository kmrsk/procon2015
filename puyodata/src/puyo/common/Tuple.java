package puyo.common;

public class Tuple<K, V, W> {

	private K first;
	private V second;
	private W third;

	public Tuple() {
	}

	public Tuple(K first, V second, W third) {
		super();
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public K getFirst() {
		return first;
	}

	public void setFirst(K first) {
		this.first = first;
	}

	public V getSecond() {
		return second;
	}

	public void setSecond(V second) {
		this.second = second;
	}

	public W getThird() {
		return third;
	}

	public void setThird(W third) {
		this.third = third;
	}
}
