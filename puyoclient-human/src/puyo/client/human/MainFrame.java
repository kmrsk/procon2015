package puyo.client.human;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class MainFrame extends JFrame {

	static public final int LEFT = 0;
	static public final int RIGHT = 1;
	static public final int DOWN = 2;
	static public final int ROTATE = 3;
	static public final int RROTATE = 4;

	// キーボード状態
	static public boolean keyState[] = new boolean[5];
	static int cntKeyState[] = new int[5];

	static final int CNT = 10;

	Controller keyboard;
	Controller gamepad;

	public void init() {
		// コントローラ取得
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for (Controller controller : controllers) {
			if (controller != null) {
				System.out.print("Type : " + controller.getType() + ", ");
				System.out.println("Name : " + controller.getName());

				if (controller.getType() == Controller.Type.KEYBOARD) {
					keyboard = controller;
				} else if (controller.getType() == Controller.Type.STICK) {
					gamepad = controller;
				}
			}
		}

		setBounds(100, 100, 300, 100);
		setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void poll() {
		boolean tmpKeyState[] = new boolean[5];
		if (keyboard != null && keyboard.poll()) {
			if (keyboard.getComponent(Identifier.Key.LEFT).getPollData() >= 1.0f
					|| keyboard.getComponent(Identifier.Key.A).getPollData() >= 1.0f
					|| keyboard.getComponent(Identifier.Key._4).getPollData() >= 1.0f) {
				tmpKeyState[LEFT] = true;
			}
			if (keyboard.getComponent(Identifier.Key.RIGHT).getPollData() >= 1.0f
					|| keyboard.getComponent(Identifier.Key.D).getPollData() >= 1.0f
					|| keyboard.getComponent(Identifier.Key._6).getPollData() >= 1.0f) {
				tmpKeyState[RIGHT] = true;
			}
			if (keyboard.getComponent(Identifier.Key.DOWN).getPollData() >= 1.0f
					|| keyboard.getComponent(Identifier.Key.X).getPollData() >= 1.0f
					|| keyboard.getComponent(Identifier.Key._2).getPollData() >= 1.0f) {
				tmpKeyState[DOWN] = true;
			}
			if (keyboard.getComponent(Identifier.Key.UP).getPollData() >= 1.0f
					|| keyboard.getComponent(Identifier.Key.S).getPollData() >= 1.0f
					|| keyboard.getComponent(Identifier.Key._5).getPollData() >= 1.0f) {
				tmpKeyState[ROTATE] = true;
			}
			if (keyboard.getComponent(Identifier.Key.END).getPollData() >= 1.0f
					|| keyboard.getComponent(Identifier.Key.W).getPollData() >= 1.0f
					|| keyboard.getComponent(Identifier.Key._8).getPollData() >= 1.0f) {
				tmpKeyState[RROTATE] = true;
			}
		}
		if (gamepad != null && gamepad.poll()) {
			float x = gamepad.getComponent(Identifier.Axis.X).getPollData();
			if (x <= -1.0) {
				tmpKeyState[LEFT] = true;
			} else if (x >= 1.0) {
				tmpKeyState[RIGHT] = true;
			}
			float y = gamepad.getComponent(Identifier.Axis.Y).getPollData();
			if (y >= 1.0) {
				tmpKeyState[DOWN] = true;
			}
			if (gamepad.getComponent(Identifier.Button._0).getPollData() >= 1.0) {
				tmpKeyState[ROTATE] = true;
			}
			if (gamepad.getComponent(Identifier.Button._1).getPollData() >= 1.0) {
				tmpKeyState[RROTATE] = true;
			}
		}

		for (int i = 0; i < 5; i++) {
			cntKeyState[i]--;

			if (tmpKeyState[i] && cntKeyState[i] < 0) {
				keyState[i] = true;
				cntKeyState[i] = CNT;
			} else {
				keyState[i] = false;
			}
		}
	}

}
