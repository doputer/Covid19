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

	private final ManagerChatUI v;
	private final ChatData chatData;

	Logger logger;

	private BufferedReader inMsg = null;
	private PrintWriter outMsg = null;

	private Socket socket;
	private Message m;
	private boolean status;
	private Thread thread;

	public ManagerChatController(ChatData chatData, ManagerChatUI v) {
		logger = Logger.getLogger(this.getClass().getName());

		this.chatData = chatData;
		this.v = v;

		connectServer();
		appMain();
	}

	public void appMain() {
		chatData.addObj(v.msgOut);

		v.addButtonActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();

				if (obj == v.msgInput) {
					if (v.idCb.getSelectedItem().equals("전체")) {
						outMsg.println(gson.toJson(new Message("관리자", "", v.msgInput.getText(), "all")));
					} else {
						outMsg.println(gson.toJson(new Message("관리자", "", v.msgInput.getText(), "user",
								v.idCb.getSelectedItem().toString())));
					}
					v.msgInput.setText("");
				}
			}
		});
	}

	public void connectServer() {
		try {
			socket = new Socket("127.0.0.1", 8888);
			logger.log(INFO, "[Manager] 서버 연결 성공");

			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outMsg = new PrintWriter(socket.getOutputStream(), true);

			m = new Message("관리자", "", "", "login");
			outMsg.println(gson.toJson(m));

			thread = new Thread(this);
			thread.start();
		} catch (Exception e) {
			logger.log(WARNING, "[Manager] 서버 연결 실패");
			e.printStackTrace();
		}
	}

	public void run() {
		String msg;

		status = true;
		while (status) {
			try {
				msg = inMsg.readLine();
				m = gson.fromJson(msg, Message.class);

				if (m.getType().equals("update")) {
					v.uId.clear();
					v.uId.add("전체");
					v.uId.addAll(m.getIds());
				} else if (m.getType().equals("login")) {
					if (!m.getId().equals("관리자"))
						v.uId.add(m.getId());
				} else if (m.getType().equals("logout")) {
					v.uId.remove(m.getId());
				} else if (m.getType().equals("sys")) {
					chatData.refreshData("시스템> " + m.getMsg() + "\n");
					v.msgOut.setCaretPosition(v.msgOut.getDocument().getLength());
				} else {
					chatData.refreshData(m.getId() + "> " + m.getMsg() + "\n");
					v.msgOut.setCaretPosition(v.msgOut.getDocument().getLength());
				}
			} catch (IOException e) {
				logger.log(WARNING, "[Manager] 메시지 스트림 종료");
			}
		}
		logger.info("[Manager]" + thread.getName() + " 메시지 수신 스레드 종료");
	}
}
