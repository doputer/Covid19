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

	public UserChatController(ChatData chatData, UserChatUI view, String name) { // 생성자에서 바로 서버에 연결하고, 이벤트 연결
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

				if (obj == view.msgInput) { // 입력이 되면 매니저에게 채팅을 보냄
					outMsg.println(gson.toJson(new Message(name, "", view.msgInput.getText(), "manager")));
					view.msgInput.setText("");
				}
			}
		});
		
		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				view.dispose();
			}

			@Override
			public void windowClosed(WindowEvent e) { // 윈도우 창이 닫히면 채팅 서버와 연결 종료
				view.controller.unconnectServer();
			}
		});
	}

	public void connectServer() { // 로컬 호스트 채팅 서버에 연결하는 메소드
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

	public void unconnectServer() { // 채팅 서버와 연결 해제하는 메소드
		outMsg.println(gson.toJson(new Message(name, "", "", "logout"))); // 서버에 사용자가 로그아웃했다는 신호를 보냄
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

	public void run() { // 반복하면서 관리자 혹은 시스템의 메시지를 읽음
		String msg;
		status = true;

		while (status) {
			try {
				msg = inMsg.readLine();
				m = gson.fromJson(msg, Message.class);

				if (m.getType().equals("sys")) { // 시스템 메시지를 받은 경우
					chatData.refreshData("시스템> " + m.getMsg() + "\n");
					view.msgOut.setCaretPosition(view.msgOut.getDocument().getLength());
				} else { // 관리자로부터 메시지를 받은 경우
					chatData.refreshData(m.getId() + "> " + m.getMsg() + "\n");
					view.msgOut.setCaretPosition(view.msgOut.getDocument().getLength());
				}
			} catch (IOException e) {
				logger.log(WARNING, "[User] 메시지 스트림 종료");
			}
		}
		this.interrupt();
		logger.info("[User]" + thread.getName() + " 메시지 수신 스레드 종료");
	}
}