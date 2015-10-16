package puyo.client3;

import static mockit.Deencapsulation.invoke;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;

import puyo.data.Box;
import puyo.data.Cluster;
import puyo.data.Puyo;
import puyo.data.PuyoState;
import puyo.data.PuyoState.Rotate;

public class PuyoPuyoAIImplTest {
	
	private static Gson gson = new Gson();
	
	private PuyoState callDetectPutState(Puyo[][] arr, Cluster currentPuyo, Cluster nextPuyo) {
		PuyoPuyoAIImpl ai = new PuyoPuyoAIImpl();

		Box box = new Box();
		List<Puyo> stack = new ArrayList<>();
		for (int rank = 0; rank < Box.RANK; rank++) {
			for (int row = 0; row < Box.ROW; row++) {
				stack.add(arr[row][rank]);
			}
		}
		box.setStack(stack);
		box.setCurrentPuyo(new PuyoState(currentPuyo, Rotate.R0, 2, Box.RANK - 1));
		box.setNextPuyo(nextPuyo);

		long start = System.currentTimeMillis();
		PuyoState puyoState = invoke(ai, "detectPutState", box);
		long end = System.currentTimeMillis();
		
		System.out.println((end - start)  + "ms");
		return puyoState;
	}


	@Test
	public void test1() {
		Puyo[][] arr = gson.fromJson("[[\"GREEN\",\"YELLOW\",\"BLUE\",\"PURPLE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"YELLOW\",\"YELLOW\",\"PURPLE\",\"YELLOW\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"GREEN\",\"RED\",\"YELLOW\",\"YELLOW\",\"GREEN\",\"PURPLE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"GREEN\",\"GREEN\",\"RED\",\"RED\",\"RED\",\"GREEN\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"RED\",\"BLUE\",\"YELLOW\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]]", Puyo[][].class);
		Cluster currentPuyo = new Cluster(Puyo.GREEN, Puyo.BLUE);
		Cluster nextPuyo = new Cluster(Puyo.PURPLE, Puyo.BLUE);
		
		PuyoState puyoState = callDetectPutState(arr, currentPuyo, nextPuyo);

		assertEquals(4, puyoState.getRow());
	}

	@Test
	public void test2() {
		Puyo[][] arr = gson.fromJson("[[\"YELLOW\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"GREEN\",\"PURPLE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"GREEN\",\"PURPLE\",\"PURPLE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"RED\",\"RED\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"YELLOW\",\"RED\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"YELLOW\",\"YELLOW\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]]", Puyo[][].class);
		Cluster currentPuyo = new Cluster(Puyo.YELLOW, Puyo.PURPLE);
		Cluster nextPuyo = new Cluster(Puyo.GREEN, Puyo.PURPLE);

		PuyoState puyoState = callDetectPutState(arr, currentPuyo, nextPuyo);

		assertEquals(4, puyoState.getRow());
	}

	@Test
	public void test3() {
		Puyo[][] arr = gson.fromJson("[[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"PURPLE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"BLUE\",\"BLUE\",\"RED\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"BLUE\",\"RED\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]]", Puyo[][].class);
		Cluster currentPuyo = new Cluster(Puyo.BLUE, Puyo.GREEN);
		Cluster nextPuyo = new Cluster(Puyo.BLUE, Puyo.BLUE);

		PuyoState puyoState = callDetectPutState(arr, currentPuyo, nextPuyo);

		assertEquals(4, puyoState.getRow());
	}

	@Test
	public void test4() {
		Puyo[][] arr = gson.fromJson("[[\"GREEN\",\"RED\",\"RED\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"PURPLE\",\"PURPLE\",\"RED\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"RED\",\"RED\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]]", Puyo[][].class);
		Cluster currentPuyo = new Cluster(Puyo.BLUE, Puyo.RED);
		Cluster nextPuyo = new Cluster(Puyo.GREEN, Puyo.PURPLE);

		PuyoState puyoState = callDetectPutState(arr, currentPuyo, nextPuyo);

		assertEquals(4, puyoState.getRow());
	}
}
