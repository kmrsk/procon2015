package puyo.data;

public class Game {

	private Box box1;
	private Box box2;

	public Game() {
	}

	public Box getBox1() {
		return box1;
	}

	public void setBox1(Box box1) {
		this.box1 = box1;
	}

	public Box getBox2() {
		return box2;
	}

	public void setBox2(Box box2) {
		this.box2 = box2;
	}

	public Box selectBox(String name) {
		if (name.equals(box1.getName())) {
			return box1;
		} else {
			return box2;
		}
	}
}
