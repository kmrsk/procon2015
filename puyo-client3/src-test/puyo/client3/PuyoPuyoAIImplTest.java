package puyo.client3;

import static mockit.Deencapsulation.invoke;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import puyo.data.Box;
import puyo.data.Cluster;
import puyo.data.Puyo;
import puyo.data.PuyoState;
import puyo.data.PuyoState.Rotate;

public class PuyoPuyoAIImplTest {
	
	private static Gson gson = new Gson();
	
	private PuyoState callDetectPutState(Puyo[][] arr, Cluster currentPuyo, Cluster nextPuyo) {
		List<Puyo> stack = new ArrayList<>();
		for (int rank = 0; rank < Box.RANK; rank++) {
			for (int row = 0; row < Box.ROW; row++) {
				stack.add(arr[row][rank]);
			}
		}

		return callDetectPutState(stack, currentPuyo, nextPuyo);
	}

	private PuyoState callDetectPutState(List<Puyo> stack, Cluster currentPuyo, Cluster nextPuyo) {
		PuyoPuyoAIImpl ai = new PuyoPuyoAIImpl();

		Box box = new Box();
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
		Puyo[][] arr = gson.fromJson(
				"[[\"GREEN\",\"YELLOW\",\"BLUE\",\"PURPLE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"YELLOW\",\"YELLOW\",\"PURPLE\",\"YELLOW\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"GREEN\",\"RED\",\"YELLOW\",\"YELLOW\",\"GREEN\",\"PURPLE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"GREEN\",\"GREEN\",\"RED\",\"RED\",\"RED\",\"GREEN\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"],"+
				"[\"RED\",\"BLUE\",\"YELLOW\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]]",
				Puyo[][].class);
		Cluster currentPuyo = new Cluster(Puyo.GREEN, Puyo.BLUE);
		Cluster nextPuyo = new Cluster(Puyo.PURPLE, Puyo.BLUE);
		
		PuyoState puyoState = callDetectPutState(arr, currentPuyo, nextPuyo);

		assertEquals(5, puyoState.getRow());
	}

	@Test
	public void test2() {
		List<Puyo> stack = gson.fromJson(
				"[\"RED\",\"PURPLE\",\"GREEN\",\"RED\",\"RED\",\"OJM\",\"RED\",\"PURPLE\",\"PURPLE\",\"GREEN\",\"GREEN\",\"GREEN\",\"YELLOW\",\"BLUE\",\"GREEN\",\"PURPLE\",\"PURPLE\",\"NONE\",\"YELLOW\",\"PURPLE\",\"OJM\",\"OJM\",\"PURPLE\",\"NONE\",\"YELLOW\",\"GREEN\",\"GREEN\",\"PURPLE\",\"GREEN\",\"NONE\",\"RED\",\"YELLOW\",\"NONE\",\"PURPLE\",\"BLUE\",\"NONE\",\"RED\",\"YELLOW\",\"NONE\",\"GREEN\",\"OJM\",\"NONE\",\"PURPLE\",\"RED\",\"NONE\",\"NONE\",\"YELLOW\",\"NONE\",\"OJM\",\"OJM\",\"NONE\",\"NONE\",\"YELLOW\",\"NONE\",\"OJM\",\"BLUE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"OJM\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]",
				new TypeToken<ArrayList<Puyo>>(){}.getType());
		Cluster currentPuyo = new Cluster(Puyo.PURPLE, Puyo.BLUE);
		Cluster nextPuyo = new Cluster(Puyo.BLUE, Puyo.YELLOW);
		
		PuyoState puyoState = callDetectPutState(stack, currentPuyo, nextPuyo);

		assertEquals(2, puyoState.getRow());
	}

	@Test
	public void testRenketsu() {
		Puyo[][] arr = gson.fromJson(
				"[[\"RED\",\"RED\",\"GREEN\",\"YELLOW\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"BLUE\",\"BLUE\",\"GREEN\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]]",
				Puyo[][].class);
		Cluster currentPuyo = new Cluster(Puyo.RED, Puyo.PURPLE);
		Cluster nextPuyo = new Cluster(Puyo.YELLOW, Puyo.YELLOW);

		PuyoState puyoState = callDetectPutState(arr, currentPuyo, nextPuyo);

		assertEquals(1, puyoState.getRow());
	}

	@Test
	public void testRensaPotential() {
		Puyo[][] arr = gson.fromJson(
				"[[\"RED\",\"GREEN\",\"RED\",\"RED\",\"RED\",\"PURPLE\",\"GREEN\",\"PURPLE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]," +
				"[\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\",\"NONE\"]]",
				Puyo[][].class);
		Cluster currentPuyo = new Cluster(Puyo.BLUE, Puyo.YELLOW);
		Cluster nextPuyo = new Cluster(Puyo.YELLOW, Puyo.YELLOW);

		PuyoState puyoState = callDetectPutState(arr, currentPuyo, nextPuyo);

		assertEquals(1, puyoState.getRow());
	}
}
