package sejong.corona;

import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.*;
import static java.util.logging.Level.*;
import com.google.gson.Gson;

public class ManagerChatController implements Runnable {
	private Gson gson = new Gson();

	private final ManagerChatUI view;
	private final ChatData chatData;

	Logger logger;

	private BufferedReader inMsg = null;
	private PrintWriter outMsg = null;

	private Socket socket;
	private Message m;
	private boolean status;
	private Thread thread;

	public ManagerChatController(ChatData chatData, ManagerChatUI view) { // 생성자에서 채팅 서버 연결
		logger = Logger.getLogger(this.getClass().getName());

		this.chatData = chatData;
		this.view = view;

		connectServer();
		appMain();
	}

	public void appMain() {
		chatData.addObj(view.msgOut);

		view.addButtonActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();

				if (obj == view.msgInput) {
					if (view.idCb.getSelectedItem().equals("전체")) { // 콤보박스에서 전체가 선택되면 모두에게 메시지
						outMsg.println(gson.toJson(new Message("관리자", "", view.msgInput.getText(), "all")));
					} else { // 전체 이외에 사용자가 선택되면 선택된 사용자에게만 메시지
						outMsg.println(gson.toJson(new Message("관리자", "", view.msgInput.getText(), "user",
								view.idCb.getSelectedItem().toString())));
					}
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
			public void windowClosed(WindowEvent e) { // 창이 닫히면 서버와 연결 종료
				view.controller.unconnectServer();
			}
		});
	}

	public void connectServer() { // 로컬 서버와 연결하는 메소드
		try {
			socket = new Socket("127.0.0.1", 8888);
			logger.log(INFO, "[Manager] 서버 연결 성공");

			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outMsg = new PrintWriter(socket.getOutputStream(), true);

			m = new Message("관리자", "", "", "login"); // 관리자가 로그인하면 서버에 신호를 보냄
			outMsg.println(gson.toJson(m));

			thread = new Thread(this);
			thread.start();
		} catch (Exception e) {
			logger.log(WARNING, "[Manager] 서버 연결 실패");
			e.printStackTrace();
		}
	}
	
	public void unconnectServer() { // 로컬 서버와 연결 해제하는 메소드
		outMsg.println(gson.toJson(new Message("관리자", "", "", "logout"))); // 관리자가 로그아웃 하면 서버에 신호를 보냄
		view.msgOut.setText("");
	}

	public void run() {
		String msg;

		status = true;
		while (status) {
			try {
				msg = inMsg.readLine();
				m = gson.fromJson(msg, Message.class);

				if (m.getType().equals("update")) { // 서버에서 update 신호가 오면 콤보박스 갱신
					view.uId.clear();
					view.uId.add("전체");
					view.uId.addAll(m.getIds());
				} else if (m.getType().equals("login")) { // 서버에서 사용자가 로그인했다는 신호가 오면 콤보박스에 아이디 추가
					if (!m.getId().equals("관리자"))
						view.uId.add(m.getId());
				} else if (m.getType().equals("logout")) { // 서버에서 사용자가 로그아웃했다는 신호가 오면 콤보박스에 아이디 삭제
					view.uId.remove(m.getId());
				} else if (m.getType().equals("sys")) { // 서버에서 시스템 메시지 신호가 오면 시스템 메시지 출력
					chatData.refreshData("시스템> " + m.getMsg() + "\n");
					view.msgOut.setCaretPosition(view.msgOut.getDocument().getLength());
					view.idCb.setSelectedIndex(0);
				} else { // 사용자에게 메시지가 오면 출력
					chatData.refreshData(m.getId() + "> " + m.getMsg() + "\n");
					view.msgOut.setCaretPosition(view.msgOut.getDocument().getLength());
				}
			} catch (IOException e) {
				logger.log(WARNING, "[Manager] 메시지 스트림 종료");
			}
		}
		logger.info("[Manager]" + thread.getName() + " 메시지 수신 스레드 종료");
	}
}
