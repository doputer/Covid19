package sejong.corona;

import java.io.*;
import java.net.*;

import java.util.*;
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
			logger.info("Chat Server start");
			while (true) {
				socket = serverSocket.accept();
				ChatThread chat = new ChatThread();
				chatThreads.add(chat);
				chat.start();
			}
		} catch (IOException e) {
			logger.info("Server Start Exception!");
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
						loginSignal(msg);
					} else if (m.getType().equals("user")) {
						msgSendToUser(msg);
					} else if (m.getType().equals("manager")) {
						msgSendToManager(msg);
					} else if (m.getType().equals("all")) {
						msgSendToAll(msg);
					}

					/*
					 * if(m.type.equals("logout")) { chatThreads.remove(this);
					 * msgSendAll(gson.toJson(new ChatMessage(m.id, "", "���� �����߽��ϴ�.", "chat")));
					 * status = false; } if(m.type.equals("login")) { if(id == null) id = m.id;
					 * msgSendAll(gson.toJson(new ChatMessage(m.id, "", "���� �α����߽��ϴ�.", "chat"))); }
					 */
//               id = m.id;
//               msgSendAll(gson.toJson(new ChatMessage(m.id, "���� �����մϴ�.")));

//               for(ChatThread ct : chatThreads) {
//                  ct.outMsg.println(gson.toJson(new ChatMessage(m.msg)));               
//               }
				} catch (IOException e) {
					e.printStackTrace();
					status = false;
				}
			}
			this.interrupt();
			logger.info(this.getName() + " Close!");
		}
		
		void loginSignal(String msg) {
			for (ChatThread ct : chatThreads) {
				if (ct.m.getId().equals("관리자")) {
					ct.outMsg.println(msg);
				}
			}
		}
		
		void msgSendToUser(String msg) {
			for (ChatThread ct : chatThreads) {
				if (ct.m.getId().equals(m.getReceiver())) {
					ct.outMsg.println(msg);
				} else if (ct.m.getId().equals(m.getId())) {
					ct.outMsg.println(msg);
				}
			}
		}
		
		void msgSendToManager(String msg) {
			for (ChatThread ct : chatThreads) {
				if (ct.m.getId().equals("관리자")) {
					ct.outMsg.println(msg);
				} else if (ct.m.getId().equals(m.getId())) {
					ct.outMsg.println(msg);
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