package sejong.corona;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

public class ManagerChatUI extends JFrame {
	JButton sendBtn;
	JPanel msgPanel;
	JTextField msgInput;
	JTextArea msgOut;
	JScrollPane jsp;

	public Vector<String> uId;
	JComboBox<String> idCb;

	ManagerChatUI(JFrame frame, String title) {
		setTitle(title);
		setLayout(new BorderLayout());

		startUI();
		new ManagerChatController(new ChatData(), this);
		uId.add("전체");
		idCb.setSelectedIndex(0);

		setSize(400, 700);

		this.setLocation((int) (frame.getX() + frame.getRootPane().getWidth()), frame.getY());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				dispose();
			}
		});
		
		new FontManager(this.getComponents());
	}

	private void startUI() {
		uId = new Vector<String>();
		idCb = new JComboBox<String>(uId);
		msgOut = new JTextArea();
		msgOut.setEditable(false);
		msgPanel = new JPanel();
		msgInput = new JTextField(29);
		sendBtn = new JButton("전송");

		jsp = new JScrollPane(msgOut, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		msgPanel.setLayout(new BorderLayout());
		msgPanel.add(msgInput, BorderLayout.WEST);
		msgPanel.add(sendBtn, BorderLayout.CENTER);

		add(idCb, BorderLayout.NORTH);
		add(jsp, BorderLayout.CENTER);
		add(msgPanel, BorderLayout.SOUTH);
	}

	public void addButtonActionListener(ActionListener listener) {
		msgInput.addActionListener(listener);
	}
}
