package puyo.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;

import puyo.common.Pair;
import puyo.common.Tuple;
import puyo.data.Message;

import com.google.gson.Gson;

public enum ServerMain {
	INSTANCE;
	private int port;
	private PuyoPuyoMaster master;
	private DatagramSocket recSocket;

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
		PuyoPuyoMaster master = new PuyoPuyoMaster();
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
		new Thread(() -> {
			while (true) {
				try {
					master.update();
					INSTANCE.sendUDP();
					canvas.drawPain(master);
					Thread.sleep(1000 / 60);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
	}

}
