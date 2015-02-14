package puyo.data;

public class User {

	private String name;
	private String password;
	private String ip;
	private int port;

	public User() {
	}

	public User(String name, String password, String ip, int port) {
		super();
		this.name = name;
		this.password = password;
		this.ip = ip;
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
