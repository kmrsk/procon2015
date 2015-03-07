package puyo.server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import puyo.data.Box;
import puyo.data.Box.BoxState;
import puyo.data.Puyo;
import puyo.server.PuyoPuyoMaster.PuyoType;

public class PuyoPain extends JPanel {

	private BufferedImage bufferedImage;
	private int width;
	private int height;
	private java.awt.Font awtFont;
	private Map<Puyo, BufferedImage> puyoImages;
	private Map<Integer, BufferedImage> chainImages;
	private BufferedImage win;
	private BufferedImage lose;
	private BufferedImage puyopuyobg;
	private BufferedImage number;

	public PuyoPain() {
		initialize();
		puyoImages = new HashMap<>();
		chainImages = new HashMap<>();
	}

	public PuyoPain(double paramDouble1, double paramDouble2) {
		width = (int) paramDouble1;
		height = (int) paramDouble2;
		initialize();
		puyoImages = new HashMap<>();
		chainImages = new HashMap<>();
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	void initialize() {
		setPreferredSize(new Dimension(width, height));
		setDoubleBuffered(true);
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.drawImage(bufferedImage, 0, 0, width, height, this);
		g2.dispose();
	}

	@Override
	public boolean imageUpdate(Image img, int flg, int x, int y, int w, int h) {
		if (flg == ALLBITS) {
			repaint();
			return false;
		}
		else
			return true;
	}

	public void drawPain(PuyoPuyoMaster master) {
		clear(0x000000ff);
		drawImage(getPuyoPuyoBG(), 0, 0, 500, 400, 0, 0, 500, 400);
		drawPainBox(master.getGame().getBox1(), master.getPuyoArray1(), 50, master.getLoser());
		drawNext(master.getGame().getBox1(), 213);
		drawChain(master.getGame().getBox1(), 50);
		drawPainBox(master.getGame().getBox2(), master.getPuyoArray2(), 300, master.getLoser());
		drawNext(master.getGame().getBox2(), 265);
		drawChain(master.getGame().getBox2(), 300);
		drawNumber(200, master.getGame().getBox1().getScore());
		drawNumber(450, master.getGame().getBox2().getScore());
		repaint();
	}

	private void drawNumber(int x, int score) {
		BufferedImage num = getNumber();
		int[] digits = new int[] { 0, 0, 0, 0, 0, 0 };
		int exp10 = 1;
		for (int i = 0; i < 6; i++) {
			digits[i] = (score / exp10) % 10;
			exp10 *= 10;
		}
		for (int i = 0; i < 6; i++) {
			drawImage(num, 15 * (digits[i] % 5), 20 * (digits[i] / 5), 15, 20, x - (15 * i) - 15, 378, 15, 20);
		}
	}

	private void drawChain(Box box, int x) {
		if (box.getState() == BoxState.EFFECT) {
			BufferedImage img = getChainImage(box.getChainCount());
			if (img != null) {
				drawImage(img, 0, 0, 150, 50, x, 150, 150, 50);
			}
		}
	}

	private void drawNext(Box box, int x) {
		if (box.getNextPuyo() != null) {
			drawPuyoImage(box.getNextPuyo().getFirst(), x, 115);
			drawPuyoImage(box.getNextPuyo().getSecond(), x, 115 + 25);
		}
	}

	private void drawPainBox(Box box, PuyoPuyoMaster.PuyoEx[][] puyoArray, int x, String loser) {
		drawText(box.getName(), 0xffffffff, x, 45);
		for (int i = 0; i < Box.ROW; i++) {
			for (int j = 0; j < Box.RANK; j++) {
				if (puyoArray[i][j] != null && puyoArray[i][j].puyo != Puyo.NONE) {
					int slip = (box.getFallCount()) * 25 / PuyoPuyoMaster.FALL_MAX;
					if (puyoArray[i][j].type == PuyoType.STATIC) {
						slip = 0;
					}
					drawPuyoImage(puyoArray[i][j].puyo, x + (i * 25), 350 - (j * 25) - slip);
				}
			}
		}
		if (loser != null) {
			if (loser.equals(box.getName())) {
				drawImage(getLose(), 0, 0, 112, 50, x, 150, 112, 50);
			} else {
				drawImage(getWin(), 0, 0, 112, 50, x, 150, 112, 50);
			}
		}
	}

	private void clear(int rgba) {
		Graphics2D g2 = getGraphics2D();
		int argb = RGBA2ARGB(rgba);
		g2.setBackground(new Color(argb, true));
		g2.clearRect(0, 0, getWidth(), getHeight());
		g2.dispose();
	}

	public void fill(int rgba, int targetX, int targetY, int targetWidth, int targetHeight) {
		Graphics2D g2 = getGraphics2D();
		int argb = RGBA2ARGB(rgba);
		g2.setColor(new Color(argb, true));
		g2.fillRect(targetX, targetY, targetWidth, targetHeight);
		g2.dispose();
	}

	public void drawPuyoImage(Puyo puyo, int targetX, int targetY) {
		BufferedImage gr = getPuyoImage(puyo);
		drawImage(gr, 0, 0, 25, 25, targetX, targetY, 25, 25);
	}

	public void drawImage(BufferedImage gr, int srcX, int srcY, int srcWidth, int srcHeight, int targetX, int targetY) {
		drawImage(gr, srcX, srcY, srcWidth, srcHeight, targetX, targetY, srcWidth, srcHeight);
	}

	public void drawImage(BufferedImage gr, int srcX, int srcY, int srcWidth, int srcHeight,
			int targetX, int targetY, int targetWidth, int targetHeight) {
		Graphics2D g2 = getGraphics2D();
		g2.drawImage(gr, targetX, targetY, targetX + targetWidth, targetY + targetHeight,
				srcX, srcY, srcX + srcWidth, srcY + srcHeight, null);
		g2.dispose();
	}

	public void drawText(String text, int rgba, int targetX, int targetY) {
		if (text == null || text.length() == 0) {
			return;
		}
		// おおよその幅を計算する。
		int tmpHeight = getAwtFont().getSize();
		int tmpWidth = getAwtFont().getSize() * text.length() * 2;
		BufferedImage textBmp = new BufferedImage(tmpWidth, tmpHeight * 2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = textBmp.createGraphics();
		g2.setColor(new Color(0.0f, 0f, 0f, 0.0f));
		g2.fillRect(0, 0, tmpWidth, tmpHeight * 2);
		g2.setFont(getAwtFont());
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int argb = RGBA2ARGB(rgba);
		g2.setColor(new Color(argb, true));
		g2.drawString(text, 0, tmpHeight + tmpHeight / 2);
		g2.dispose();
		g2 = getGraphics2D();
		g2.drawImage(textBmp, targetX, targetY - (tmpHeight + tmpHeight / 2), targetX + tmpWidth, targetY
				+ (tmpHeight / 2),
				0, 0, tmpWidth, tmpHeight * 2, null);
		g2.dispose();
	}

	private int RGBA2ARGB(int rgba) {
		int rgb = (rgba >>> 8);
		int alpha = (0x000000ff & (rgba));
		int argb = alpha * 0x1000000 + rgb;
		return argb;

	}

	public Graphics2D getGraphics2D() {
		return bufferedImage.createGraphics();
	}

	private BufferedImage loadImage(String key) {
		try (InputStream io = getClass().getClassLoader().getResourceAsStream(key)) {
			BufferedImage image = ImageIO.read(io);
			return image;
		} catch (Exception e) {
			throw new RuntimeException(key, e);
		}
	}

	private BufferedImage getWin() {
		if (win == null) {
			win = loadImage("win.png");
		}
		return win;
	}

	private BufferedImage getLose() {
		if (lose == null) {
			lose = loadImage("lose.png");
		}
		return lose;
	}

	private BufferedImage getPuyoPuyoBG() {
		if (puyopuyobg == null) {
			puyopuyobg = loadImage("puyopuyobg.png");
		}
		return puyopuyobg;
	}

	private BufferedImage getNumber() {
		if (number == null) {
			number = loadImage("number.png");
		}
		return number;
	}

	private BufferedImage getPuyoImage(Puyo puyo) {
		BufferedImage puyoImage = puyoImages.get(puyo);
		if (puyoImage == null) {
			switch (puyo) {
			case BLUE:
				puyoImage = loadImage("puyo-b.png");
				break;
			case NONE:
				break;
			case RED:
				puyoImage = loadImage("puyo-r.png");
				break;
			case YELLOW:
				puyoImage = loadImage("puyo-y.png");
				break;
			case PURPLE:
				puyoImage = loadImage("puyo-p.png");
				break;
			case GREEN:
				puyoImage = loadImage("puyo-g.png");
				break;
			default:
				break;
			}
			puyoImages.put(puyo, puyoImage);
		}
		return puyoImage;
	}

	private BufferedImage getChainImage(int chain) {
		if (chain <= 1) {
			return null;
		}
		if (chain >= 5) {
			chain = 5;
		}
		BufferedImage chainImage = chainImages.get(chain);
		if (chainImage == null) {
			switch (chain) {
			//case 1:
			//	chainImage = loadImage("rensa1.png");
			//	break;
			case 2:
				chainImage = loadImage("rensa2.png");
				break;
			case 3:
				chainImage = loadImage("rensa3.png");
				break;
			case 4:
				chainImage = loadImage("rensa4.png");
				break;
			case 5:
				chainImage = loadImage("rensa5.png");
				break;
			default:
				break;
			}
			chainImages.put(chain, chainImage);
		}
		return chainImage;
	}

	private java.awt.Font loadFont(String key, float fontSize) {
		try (InputStream io = getClass().getClassLoader().getResourceAsStream(key)) {
			return java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, io).deriveFont(fontSize);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public java.awt.Font getAwtFont() {
		if (awtFont == null) {
			awtFont = loadFont("ipag.ttf", 16);
		}
		return awtFont;
	}
}
