package sejong.corona;

import java.util.Vector;

public class Message {
	public String id;
	private String passwd;
	private String msg;
	private String type;
	private String receiver;
	private Vector<String> ids;

	Message() {

	}

	Message(String id, String passwd, String msg, String type) {
		this.id = id;
		this.passwd = passwd;
		this.msg = msg;
		this.type = type;
	}

	Message(String id, String passwd, String msg, String type, String receiver) {
		this.id = id;
		this.passwd = passwd;
		this.msg = msg;
		this.type = type;
		this.receiver = receiver;
	}

	Message(String type, Vector<String> ids) {
		this.type = type;
		this.ids = ids;
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

	public Vector<String> getIds() {
		return this.ids;
	}
}
