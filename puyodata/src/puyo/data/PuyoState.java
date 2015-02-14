package puyo.data;

public class PuyoState {
	public enum Rotate {
		R0, R90, R180, R270
	}

	private Cluster cluster;
	private Rotate rotate;
	private int row;
	private int rank;

	public PuyoState() {
	}

	public PuyoState(Cluster cluster, Rotate rotate, int row, int rank) {
		super();
		this.cluster = cluster;
		this.rotate = rotate;
		this.row = row;
		this.rank = rank;
	}

	public Cluster getCluster() {
		return cluster;
	}

	public void setCluster(Cluster cluster) {
		this.cluster = cluster;
	}

	public Rotate getRotate() {
		return rotate;
	}

	public void setRotate(Rotate rotate) {
		this.rotate = rotate;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int[] getPuyoPosition(int i) {
		if (i == 0) {
			return new int[] { row, rank };
		}
		switch (rotate) {
		case R0:
			return new int[] { row + 1, rank };
		case R90:
			return new int[] { row, rank - 1 };
		case R180:
			return new int[] { row - 1, rank };
		case R270:
			return new int[] { row, rank + 1 };
		default:
			break;
		}
		return null;
	}
}
