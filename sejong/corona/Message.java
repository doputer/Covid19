package sejong.corona;

public class Message {
	public String id;
	private String passwd;
	private String msg;
	private String type;
	private String receiver;

	Message() {
		this.id = "";
		this.passwd = "";
		this.msg = "";
		this.type = "";
		this.receiver = "";
	}
	
	Message(String id, String passwd, String msg, String type) {
		this.id = id;
		this.passwd = passwd;
		this.msg = msg;
		this.type = type;
		this.receiver = "";
	}

	Message(String id, String passwd, String msg, String type, String receiver) {
		this.id = id;
		this.passwd = passwd;
		this.msg = msg;
		this.type = type;
		this.receiver = receiver;
	}

	public String getId() {
		return this.id;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public String getMsg() {
		return this.msg;
	}

	public String getType() {
		return this.type;
	}
	
	public String getReceiver() {
		return this.receiver;
	}
}
