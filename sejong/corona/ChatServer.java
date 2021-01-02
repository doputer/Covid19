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

	public void start() {
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

					if (m.getType().equals("login")) {
						logSignal(msg);
						if (m.getId().equals("관리자")) {
							managerLogin();
						}
					} else if (m.getType().equals("logout")) {
						logSignal(msg);
						for (ChatThread ct : chatThreads) {
							if (ct.m.getId().equals(m.getId())) {
								chatThreads.remove(ct);
								ct.interrupt();
							}
						}
					} else if (m.getType().equals("user")) {
						msgSendToUser(msg);
					} else if (m.getType().equals("manager")) {
						msgSendToManager(msg);
					} else if (m.getType().equals("all")) {
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

		void managerLogin() {
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

		void logSignal(String msg) {
			for (ChatThread ct : chatThreads) {
				if (ct.m.getId().equals("관리자")) {
					ct.outMsg.println(msg);
				}
			}
		}

		void msgSendToUser(String msg) {
			boolean log = false;
			
			for (ChatThread ct : chatThreads) {
				if (ct.m.getId().equals(m.getReceiver())) {
					ct.outMsg.println(msg);
					log = true;
				} else if (ct.m.getId().equals(m.getId())) {
					ct.outMsg.println(msg);
				}
			}
			
			if (!log) {
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

		void msgSendToManager(String msg) {
			boolean log = false;
			
			for (ChatThread ct : chatThreads) {
				if (ct.m.getId().equals("관리자")) {
					ct.outMsg.println(msg);
					log = true;
				} else if (ct.m.getId().equals(m.getId())) {
					ct.outMsg.println(msg);
				}
			}
			
			if (!log) {
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

		void msgSendToAll(String msg) {
			for (ChatThread ct : chatThreads) {
				ct.outMsg.println(msg);
			}
		}
	}

}