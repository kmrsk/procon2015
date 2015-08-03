package puyo.data;

public enum Puyo {
	NONE, RED, BLUE, YELLOW, GREEN, PURPLE, OJM;

	private static final Puyo[] ALL = { RED, BLUE, YELLOW, GREEN, PURPLE };

	public static Puyo getRandom() {
		int i = (int) Math.floor(Math.random() * 10000) % ALL.length;
		return ALL[i];
	}
}
