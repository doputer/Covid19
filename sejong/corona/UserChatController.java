package sejong.corona;

import java.io.*;
import java.awt.event.*;
import java.net.*;

import com.google.gson.Gson;

import java.util.logging.*;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

public class UserChatController extends Thread {
	private final UserChatUI view;
	private final ChatData chatData;
	private BufferedReader inMsg = null;
	private PrintWriter outMsg = null;
	private Gson gson = new Gson();
	private Logger logger;
	private Socket socket;
	private boolean status = true;
	Thread thread;
	public int num = 2;
	public String id;
	private Message m;

	public UserChatController(ChatData chatData, UserChatUI view, String name) {
		logger = Logger.getLogger(this.getClass().getName());
		this.view = view;
		this.chatData = chatData;
		
		id = name;
	}

	void appMain() {
		view.id = m.getId();
		view.addButtonActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();

				if (obj == view.msgInput) {
					outMsg.println(gson.toJson(new Message(id, "", view.msgInput.getText(), "manager")));
					view.msgInput.setText("");
				} else if (obj == view.exitBtn) {
					outMsg.println(gson.toJson(new Message(id, "", "", "logout")));
					view.msgOut.setText("");
					outMsg.close();
					try {
						inMsg.close();
						socket.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					status = false;
				}
			}
		});
	}
	
	public void connectController() {
		chatData.addObj(view.msgOut);
		
		if (connectServer())
			appMain();
		else
			chatData.refreshData("서버에 연결되지 않았습니다.\n");
	}

	public boolean connectServer() {
		try {
			socket = new Socket("127.0.0.1", 8888);
			logger.log(INFO, "[Client]Server 연결 성공!!");

			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outMsg = new PrintWriter(socket.getOutputStream(), true);

			m = new Message(id, "", "", "login");
			outMsg.println(gson.toJson(m));

			thread = new Thread(this);
			thread.start();
			
			return true;
		} catch (Exception e) {
			logger.log(WARNING, "[UserChatController]connectServer() Exception 발생!!");
			e.printStackTrace();
			
			return false;
		}
	}

	public void run() {
		String msg;
		status = true;

		while (status) {
			try {
				msg = inMsg.readLine();
				m = gson.fromJson(msg, Message.class);

				chatData.refreshData(m.getId() + ">" + m.getMsg() + "\n");
				view.msgOut.setCaretPosition(view.msgOut.getDocument().getLength());
			} catch (IOException e) {
				logger.log(WARNING, "[UserChatController]메시지 스트림 종료!!");
			}
		}
		logger.info("[UserChatController]" + thread.getName() + " 메시지 수신 스레드 종료됨!!");
	}
}