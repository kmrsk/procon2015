package puyo.client.human;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class MainFrame extends JFrame implements KeyListener {
	
	static public final int LEFT = 0;
	static public final int RIGHT = 1;
	static public final int DOWN = 2;
	static public final int ROTATE = 3;
	static public final int RROTATE = 4;

	// キーボード状態
	static public boolean keyState[] = new boolean[5];
	
	public void init() {
		setBounds(100, 100, 300, 100);
		setVisible(true);
		addKeyListener(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()) {
		case '4':
			keyState[LEFT] = true;
			break;
		case '6':
			keyState[RIGHT] = true;
			break;
		case '2':
			keyState[DOWN] = true;
			break;
		case '5':
			keyState[ROTATE] = true;
			break;
		case '8':
			keyState[RROTATE] = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyChar()) {
		case '4':
			keyState[LEFT] = false;
			break;
		case '6':
			keyState[RIGHT] = false;
			break;
		case '2':
			keyState[DOWN] = false;
			break;
		case '5':
			keyState[ROTATE] = false;
			break;
		case '8':
			keyState[RROTATE] = false;
			break;
		}
	}

}
