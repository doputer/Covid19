package sejong.corona;

import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.logging.*;
import static java.util.logging.Level.*;
import com.google.gson.Gson;

public class ManagerChatController implements Runnable {
	private final ManagerChatUI v;
	private final ChatData chatData;

	Logger logger;

	private BufferedReader inMsg = null;
	private PrintWriter outMsg = null;

	private Gson gson = new Gson();
	private Socket socket;
	private Message m = new Message("", "", "", "");
	private boolean status = true;
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
						outMsg.println(gson.toJson(new Message("관리자", "", v.msgInput.getText(), "user", v.idCb.getSelectedItem().toString())));
					}
					v.msgInput.setText("");
				}
			}
		});
	}

	public void connectServer() {
		try {
			socket = new Socket("127.0.0.1", 8888);
			logger.log(INFO, "[Client]Server Connect!");

			inMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outMsg = new PrintWriter(socket.getOutputStream(), true);

			m = new Message("관리자", "", "", "login"); // 관리자 로그인이 필요하면 추후에 수정할 것
			outMsg.println(gson.toJson(m));

			thread = new Thread(this);
			thread.start();
		} catch (Exception e) {
			logger.log(WARNING, "[MultiChatUI]connectServer() Exception Occur!");
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

				
				if (m.getType().equals("login"))
					v.uId.add(m.getId());
				else {
					chatData.refreshData(m.getId() + ">" + m.getMsg() + "\n");
					v.msgOut.setCaretPosition(v.msgOut.getDocument().getLength());
				}
			} catch (IOException e) {
				logger.log(WARNING, "[MultiChatUI] Message Stream Close!");
			}
		}
		logger.info("[MultiChatUI]" + thread.getName() + "Message Receive Thread Close!");
	}
}
