package puyo.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import puyo.common.Pair;
import puyo.common.Tuple;
import puyo.data.Box;
import puyo.data.Message;

import com.google.gson.Gson;

public enum ServerMain {
	INSTANCE;
	private int port;
	private PuyoPuyoMaster master;
	private DatagramSocket recSocket;

	// 解析用log出力機能
	public void logging(Box box) {
		Gson gson = new Gson();
		String msg = gson.toJson(box, Box.class);
		System.out.println(msg);
	}

	// "127.0.0.1"
	public void sendUDP() throws IOException {
		for (Tuple<String, Integer, Message> tpl : master.getSendBox()) {
			String ip = tpl.getFirst();
			int port = tpl.getSecond();
			Message message = tpl.getThird();
			DatagramSocket sendSocket = new DatagramSocket();
			InetAddress inetAddress = InetAddress.getByName(ip);
			Gson gson = new Gson();
			String msg = gson.toJson(message, Message.class);
			byte[] buf = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, inetAddress, port);
			sendSocket.send(packet);
			sendSocket.close();
			//System.out.println(msg);
		}
		master.getSendBox().clear();
	}

	public void receiveUDP() throws IOException {
		if (recSocket == null) {
			recSocket = new DatagramSocket(port);
		}
		byte[] buf = new byte[1024 * 5];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		recSocket.receive(packet);
		SocketAddress sockAddress = packet.getSocketAddress();
		int len = packet.getLength();
		String msg = new String(buf, 0, len);
		if (msg.equals("")) {
			return;
		}
		Gson gson = new Gson();
		Message message = gson.fromJson(msg, Message.class);
		String[] tokens = sockAddress.toString().split(":");
		String ip = tokens[0].substring(1);
		master.getReceivedBox().add(new Pair<>(ip, message));
		//System.out.println(msg);
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setMaster(PuyoPuyoMaster master) {
		this.master = master;
	}

	public static void main(String[] args) {
		Pattern p1 = Pattern.compile("-speed=(\\d+)");
		Pattern p2 = Pattern.compile("-frate=(\\d+)");
		Pattern p3 = Pattern.compile("-fmax=(\\d+)");
		// フレームレート
		int frate = 60;
		// 最大フレーム
		int fmax = 60 * 5 * 60;
		for (String str : args) {
			Matcher m = p1.matcher(str);
			if (m.find()) {
				int speed = Integer.parseInt(m.group(1));
				PuyoPuyoMaster.FALL_MAX = (int) Math.pow(2, speed + 4);
				System.out.println("speed:" + speed);
			}
			m = p2.matcher(str);
			if (m.find()) {
				frate = Integer.parseInt(m.group(1));
				System.out.println("frate:" + frate);
			}
			m = p3.matcher(str);
			if (m.find()) {
				fmax = Integer.parseInt(m.group(1));
				System.out.println("fmax:" + fmax);
			}
		}
		PuyoPuyoMaster master = new PuyoPuyoMaster();
		master.setFrameMax(fmax);
		INSTANCE.setPort(5432);
		INSTANCE.setMaster(master);
		new Thread(() -> {
			while (true) {
				try {
					INSTANCE.receiveUDP();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
		PuyoPain canvas = new PuyoPain(500, 400);
		new PuyoPuyoFrame("ぷよぷよ").start(canvas);
		final int frameRate = frate;
		new Thread(() -> {
			while (true) {
				try {
					master.update();
					INSTANCE.sendUDP();
					canvas.drawPain(master);
					Thread.sleep(1000 / frameRate);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
	}

}
