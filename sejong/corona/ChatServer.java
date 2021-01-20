package sejong.corona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;

import com.google.gson.Gson;

public class ChatServer {

	private ServerSocket serverSocket = null;
	private Socket socket = null;
	private ArrayList<ChatThread> chatThreads = new ArrayList<ChatThread>();

	Logger logger;

	public static void main(String[] args) {
		new ChatServer().start();
	}

	public void start() { // 채팅 서버를 시작하고 사용자나 관리자가 연결되기를 기다림
		logger = Logger.getLogger(this.getClass().getName());
		try {
			serverSocket = new ServerSocket(8888);
			logger.info("채팅 서버 시작");
			while (true) {
				socket = serverSocket.accept();
				ChatThread chat = new ChatThread();
				chatThreads.add(chat);
				chat.start();
			}
		} catch (IOException e) {
			logger.info("채팅 서버 시작 오류");
			e.printStackTrace();
		}
	}

	class ChatThread extends Thread {
		private String msg;
		private Message m = new Message();
		private Gson gson = new Gson();
		private BufferedReader inMsg;
		private PrintWriter outMsg;
		private boolean status = true;

		@Override
		public void run() {
			try {
				inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				outMsg = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}

			while (status) {
				try {
					msg = inMsg.readLine();
					m = gson.fromJson(msg, Message.class);

					if (m.getType().equals("login")) { // 로그인 신호 받으면 관리자에게 보내서 콤보박스에 추가할 수 있게 함
						logSignal(msg);
						if (m.getId().equals("관리자")) {
							managerLogin();
						}
					} else if (m.getType().equals("logout")) { // 로그아웃 신호 받으면 관리자에게 보내서 콤보박스에서 제거할 수 있게 함
						logSignal(msg);
						for (ChatThread ct : chatThreads) {
							if (ct.m.getId().equals(m.getId())) { // 로그아웃한 스레드 삭제
								chatThreads.remove(ct);
								ct.interrupt();
							}
						}
					} else if (m.getType().equals("user")) { // 관리자가 사용자에게 보내는 메시지
						msgSendToUser(msg);
					} else if (m.getType().equals("manager")) { // 사용자가 관리자에게 보내는 메시지
						msgSendToManager(msg);
					} else if (m.getType().equals("all")) { // 관리자가 전체 사용자에게 보내는 메시지
						msgSendToAll(msg);
					}
				} catch (IOException e) {
					e.printStackTrace();
					status = false;
				}
			}
			this.interrupt();
			logger.info(this.getName() + " 종료");
		}

		void managerLogin() { // 관리자가 로그인하면 실행되는 메소드로 관리자 접속 전에 들어와있는 사용자들의 목록을 관리자에게 보냄
			Vector<String> ids = new Vector<String>();

			for (ChatThread ct : chatThreads) {
				if (!ct.m.getId().equals("관리자")) {
					ids.add(ct.m.getId());
				}
			}

			for (ChatThread ct : chatThreads) {
				if (ct.m.getId().equals("관리자")) {
					ct.outMsg.println(gson.toJson(new Message("update", ids)));
				}
			}
		}

		void logSignal(String msg) { // 사용자가 로그인하면 관리자에게 신호를 보내는 메소드
			for (ChatThread ct : chatThreads) {
				if (ct.m.getId().equals("관리자")) {
					ct.outMsg.println(msg);
				}
			}
		}

		void msgSendToUser(String msg) { // 관리자가 사용자 한 명에게 메시지를 보내는 메소드
			boolean log = false;
			
			for (ChatThread ct : chatThreads) {
				if (ct.m.getId().equals(m.getReceiver())) {
					ct.outMsg.println(msg);
					log = true;
				} else if (ct.m.getId().equals(m.getId())) {
					ct.outMsg.println(msg);
				}
			}
			
			if (!log) { // 해당 사용자가 존재하지 않으면 시스템 메시지를 보냄
				for (ChatThread ct : chatThreads) {
					if (ct.m.getId().equals(m.getId())) {
						m = gson.fromJson(msg, Message.class);
						m.setMsg("해당 사용자가 존재하지 않습니다.");
						m.setType("sys");
						msg = gson.toJson(m, Message.class);
						ct.outMsg.println(msg);
					}
				}
			}
		}

		void msgSendToManager(String msg) { // 사용자가 관리자에게 보내는 메시지
			boolean log = false;
			
			for (ChatThread ct : chatThreads) {
				if (ct.m.getId().equals("관리자")) {
					ct.outMsg.println(msg);
					log = true;
				} else if (ct.m.getId().equals(m.getId())) {
					ct.outMsg.println(msg);
				}
			}
			
			if (!log) { // 관리자가 존재하지 않으면 시스템 메시지를 보냄
				for (ChatThread ct : chatThreads) {
					if (ct.m.getId().equals(m.getId())) {
						m = gson.fromJson(msg, Message.class);
						m.setMsg("관리자가 부재 중 입니다.");
						m.setType("sys");
						msg = gson.toJson(m, Message.class);
						ct.outMsg.println(msg);
					}
				}
			}
		}

		void msgSendToAll(String msg) { // 관리자가 전체에게 메시지를 보내는 메소드
			for (ChatThread ct : chatThreads) {
				ct.outMsg.println(msg);
			}
		}
	}

}