package sejong.corona;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserChatUI extends JFrame {

	public JButton sendBtn;
	public JButton exitBtn;

	public JPanel msgPanel;

	public JTextArea msgOut;
	public JScrollPane jsp;
	public JTextField msgInput;

	public Container tab;
	public String msg;
	public CardLayout cardLayout;

	public String id;
	
	private UserChatController userChatController;

	public UserChatUI(JFrame frame, String title, String name) {
		setTitle("문의하기");

		startUI();
		userChatController = new UserChatController(new ChatData(), this, name);
		
		setSize(500, 300);
		this.setLocation((int) (frame.getX() + frame.getRootPane().getWidth()), frame.getY());
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				userChatController.connectController();
			}
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				dispose();
			}
		});
	}
	
	private void startUI() {
		tab = getContentPane();

		tab.setLayout(new BorderLayout());

		msgOut = new JTextArea("", 10, 10);
		msgOut.setEditable(false);
		jsp = new JScrollPane(msgOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		tab.add(jsp, BorderLayout.CENTER);

		msgPanel = new JPanel();
		msgInput = new JTextField(20);

		sendBtn = new JButton("전송");
		exitBtn = new JButton("종료");
		msgPanel.add(msgInput);
		msgPanel.add(sendBtn);
		msgPanel.add(exitBtn);

		tab.add(msgPanel, BorderLayout.SOUTH);
	}

	public void addButtonActionListener(ActionListener listener) {
		msgInput.addActionListener(listener);
		sendBtn.addActionListener(listener);
		exitBtn.addActionListener(listener);
	}
}