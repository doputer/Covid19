package sejong.corona;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UserChatUI extends JFrame {

	public JTextArea msgOut;

	public JScrollPane jsp;

	public JPanel msgPanel;
	public JTextField msgInput;
	public JButton sendBtn;

	private UserChatController controller;

	public UserChatUI(JFrame frame, String title, String name) {
		setTitle(title);
		setLayout(new BorderLayout());

		startUI();

		controller = new UserChatController(new ChatData(), this, name);

		setSize(400, 440);
		this.setLocation((int) (frame.getX() + frame.getRootPane().getWidth()), frame.getY());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				dispose();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				controller.unconnectServer();
			}
		});

		new FontManager(this.getComponents());
	}

	private void startUI() {
		msgOut = new JTextArea("", 10, 10);
		msgOut.setEditable(false);
		jsp = new JScrollPane(msgOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		msgPanel = new JPanel();
		msgInput = new JTextField(29);

		sendBtn = new JButton("전송");

		msgPanel.setLayout(new BorderLayout());
		msgPanel.add(msgInput, BorderLayout.WEST);
		msgPanel.add(sendBtn, BorderLayout.CENTER);

		add(jsp, BorderLayout.CENTER);
		add(msgPanel, BorderLayout.SOUTH);
	}

	public void addButtonActionListener(ActionListener listener) {
		msgInput.addActionListener(listener);
		sendBtn.addActionListener(listener);
	}
}