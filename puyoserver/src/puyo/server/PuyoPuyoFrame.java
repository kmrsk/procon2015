package puyo.server;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class PuyoPuyoFrame extends JFrame {

	public PuyoPuyoFrame() throws HeadlessException {
	}

	public PuyoPuyoFrame(GraphicsConfiguration gc) {
		super(gc);
	}

	public PuyoPuyoFrame(String title) throws HeadlessException {
		super(title);
	}

	public PuyoPuyoFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
	}

	public void start(PuyoPain canvas) {
		setBounds(100, 100, 500, 400);
		add(canvas);
		getContentPane().add(canvas);
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
