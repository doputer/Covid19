package sejong.corona;

import java.io.*;
import java.awt.event.*;
import java.net.*;

import com.google.gson.Gson;

import java.util.logging.*;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

public class UserChatController extends Thread {
	private Gson gson = new Gson();

	private final UserChatUI view;
	private final ChatData chatData;

	private BufferedReader inMsg = null;
	private PrintWriter outMsg = null;
	private Logger logger;
	private Socket socket;
	private boolean status;
	private String name;
	private Message m;

	private Thread thread;

	public UserChatController(ChatData chatData, UserChatUI view, String name) {
		logger = Logger.getLogger(this.getClass().getName());
		this.view = view;
		this.chatData = chatData;
		this.name = name;

		connectServer();
		appMain();
	}

	void appMain() {
		chatData.addObj(view.msgOut);
		view.addButtonActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();

				if (obj == view.msgInput) {
					outMsg.println(gson.toJson(new Message(name, "", view.msgInput.getText(), "manager")));
					view.msgInput.setText("");
				}
			}
		});
	}

	public void connectServer() {
		try {
			socket = new Socket("127.0.0.1", 8888);
			logger.log(INFO, "[User] 서버 연결 성공");

			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outMsg = new PrintWriter(socket.getOutputStream(), true);

			m = new Message(name, "", "", "login");
			outMsg.println(gson.toJson(m));

			thread = new Thread(this);
			thread.start();
		} catch (Exception e) {
			logger.log(WARNING, "[User] 서버 연결 실패");
			e.printStackTrace();
		}
	}

	public void unconnectServer() {
		outMsg.println(gson.toJson(new Message(name, "", "", "logout")));
		view.msgOut.setText("");
//		outMsg.close();
//		
//		try {
//			inMsg.close();
//			socket.close();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//		status = false;
	}

	public void run() {
		String msg;
		status = true;

		while (status) {
			try {
				msg = inMsg.readLine();
				m = gson.fromJson(msg, Message.class);

				chatData.refreshData(m.getId() + "> " + m.getMsg() + "\n");
				view.msgOut.setCaretPosition(view.msgOut.getDocument().getLength());
			} catch (IOException e) {
				logger.log(WARNING, "[User] 메시지 스트림 종료");
			}
		}
		this.interrupt();
		logger.info("[User]" + thread.getName() + " 메시지 수신 스레드 종료");
	}
}