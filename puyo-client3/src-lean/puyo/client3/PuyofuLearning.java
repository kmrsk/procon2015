package puyo.client3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import puyo.data.Box;
import puyo.data.PuyoState;
import puyo.data.PuyoState.Rotate;

public class PuyofuLearning {
	static private Gson gson = new Gson();

	public static void main(String[] args) throws IOException {
		// 引数からぷよ譜の格納ディレクトリパスを取得
		if (args.length < 1) {
			System.out.println("arg1 : puyofu directory path");
			return;
		}
		String dirpath = args[0];

		// ぷよ譜読み込み
		ArrayList<Box> puyofuList = new ArrayList<>();
		// ディレクトリ内のファイル一覧取得
		for (File file : new File(dirpath).listFiles()) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String strLine;
			while((strLine = br.readLine()) != null) {
				if (strLine.length() > 1 && strLine.charAt(0) == '{') {
					Box box = gson.fromJson(strLine, Box.class);
					puyofuList.add(box);
				}
			}
			br.close();
		}

		// 特徴ベクトル定義
		Feature[] features = getDefFeatures();
		
		// 学習
		learn(features, puyofuList);
	}

	// 特徴ベクトル定義
	private static Feature[] getDefFeatures() throws IOException {
		// 特徴ベクトル定義読み込み
		//	feature[0] : id
		//	feature[1] : value
		//	feature[2] : start
		//	feature[3] : end
		// 	feature[4] : step
		BufferedReader in = new BufferedReader(new FileReader("DefFeatures.txt"));

		ArrayList<Feature> features = new ArrayList<>();
		String line;
		while ((line = in.readLine()) != null) {
			String val[] = line.split("\t");
			Feature feature = new Feature();
			feature.id = val[0];
			feature.value = Integer.parseInt(val[1]);
			feature.start = Integer.parseInt(val[2]);
			feature.end = Integer.parseInt(val[3]);
			feature.step = Integer.parseInt(val[4]);

			features.add(feature);
		}
		
		in.close();

		return features.toArray(new Feature[0]);
	}
	
	// ぷよ譜学習
	public static void learn(Feature[] features, List<Box> puyofuList) {
		PuyoPuyoAIImpl ai = new PuyoPuyoAIImpl();
		
		// パラメータを初期値に設定
		for (Feature feature : features) {
			ai.setParam(feature.id, feature.value);
		}
		
		// 各特徴について
		for (Feature feature : features) {
			System.out.printf("%s : ", feature.id);

			// パラメータを変化させて
			int maxMatch = 0;
			int maxParamVal = 0;
			for (int paramVal = feature.start; paramVal <= feature.end; paramVal += feature.step) {
				ai.setParam(feature.id, paramVal);
				
				// 探索結果とぷよ譜との一致数
				int match = 0;
				for (Box box : puyofuList) {
					// 探索
					PuyoState state = ai.detectPutState(box);
					
					// 一致
					if (isMatch(state, box.getCurrentPuyo())) {
						match++;
					}
				}
				
				if (match > maxMatch) {
					maxMatch = match;
					maxParamVal = paramVal;
				}

				System.out.printf("%d,", match);
			}
			
			// パラメータを更新
			System.out.printf(" %d -> %d\n", feature.value, maxParamVal);
			feature.value = maxParamVal;
			ai.setParam(feature.id, feature.value);
		}
	}

	private static boolean isMatch(PuyoState state, PuyoState puyofu) {
		// 回転と位置が一致
		if (state.getRotate() == puyofu.getRotate()) {
			if (state.getRow() == puyofu.getRow()) {
				return true;
			}
		}
		
		// 色が同じとき
		if (state.getCluster().getFirst() == state.getCluster().getSecond()) {
			if (state.getRotate() == Rotate.R0 && puyofu.getRotate() == Rotate.R180) {
				if (state.getRow() == puyofu.getRow() - 1) {
					return true;
				}
			} else if (state.getRotate() == Rotate.R180 && puyofu.getRotate() == Rotate.R0) {
				if (state.getRow() - 1 == puyofu.getRow()) {
					return true;
				}
			} else if ((state.getRotate() == Rotate.R90 || state.getRotate() == Rotate.R270)
					&& (puyofu.getRotate() == Rotate.R90 || puyofu.getRotate() == Rotate.R270)) {
				if (state.getRow() == puyofu.getRow()) {
					return true;
				}
			}
		}
		
		return false;
	}

}
