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
						msgSendAll(msg);
					} else if (m.getId().equals("전체")) {
						msgSendAll(msg);
					} else {
						msgSendTo(msg, m.getId());
					}

					/*
					 * if(m.type.equals("logout")) { chatThreads.remove(this);
					 * msgSendAll(gson.toJson(new ChatMessage(m.id, "", "님이 종료했습니다.", "chat")));
					 * status = false; } if(m.type.equals("login")) { if(id == null) id = m.id;
					 * msgSendAll(gson.toJson(new ChatMessage(m.id, "", "님이 로그인했습니다.", "chat"))); }
					 */
//               id = m.id;
//               msgSendAll(gson.toJson(new ChatMessage(m.id, "님이 문의합니다.")));

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
		
		void msgSendTo(String msg, String receiver) {
			for (ChatThread ct : chatThreads) {
				if (ct.m.getId().equals(receiver)) {
					ct.outMsg.println(msg);
				}
			}
		}

		void msgSendAll(String msg) {
			for (ChatThread ct : chatThreads) {
				ct.outMsg.println(msg);
			}
		}
	}

}