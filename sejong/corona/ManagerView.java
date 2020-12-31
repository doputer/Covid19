package sejong.corona;

import javax.swing.JFrame;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class ManagerView extends JFrame {
	JTextArea textL;
	JLabel lblmessage;
	JComboBox _clinic;
	JComboBox _symptom;
	JList<String> List;
	JDateChooser dateChooser;
//	ManagerController controlL;
	
	ManagerChatUI managerChatUI;
	
	public int cnt = 0;

	public ManagerView() {
		setFrame();

		setOption();
		setVisible(true);
	}

	public void setFrame() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Manager 1.0");
		setSize(900, 700);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
	}

	public void setOption() {
		JPanel message = new JPanel();
		message.setBounds(0, 0, 880, 660);
		message.setBackground(Color.white);
		message.setLayout(null);

		List = new JList<String>();
		List.setFont(new Font("���� ���", Font.PLAIN, 12));
		JScrollPane scroll = new JScrollPane(List);
		scroll.setBounds(10, 200, 870, 450);
		add(scroll);

//		controlL = new ManagerController(this);

		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(250, 100, 170, 40);
		dateChooser.getJCalendar().setPreferredSize(new Dimension(170, 200));

		JButton _diagnosis = new JButton("������ ����");
		JButton _consulting = new JButton("������ ���");
		_consulting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        if (e.getSource() == _consulting) {
//		            if (managerChatUI == null) {
		            	managerChatUI = new ManagerChatUI(ManagerView.this, "����ϱ�"); // ��� �������� ���� ��ġ�� �����س���
//		            }
		            managerChatUI.setVisible(true);
		        }
		    }
		});
		JButton _confirm = new JButton("������ ��ȸ");

		_diagnosis.setBounds(40, 30, 150, 40);
		_consulting.setBounds(680, 30, 150, 40);
		_confirm.setBounds(680, 100, 150, 40);

//		_diagnosis.addActionListener(controlL);
//		_consulting.addActionListener(controlL);
//		_confirm.addActionListener(controlL);

		JLabel mNumber = new JLabel(cnt + "/40");
		mNumber.setFont(new Font("���� ���", Font.PLAIN, 40));
		mNumber.setBounds(500, 30, 150, 40);
		message.add(mNumber);

		_clinic = new JComboBox();
		_clinic.setBounds(40, 100, 170, 40);
		_clinic.setFont(new Font("���� ���", Font.PLAIN, 15));
		_clinic.addItem("�����");

		_symptom = new JComboBox();
		_symptom.setBounds(460, 100, 170, 40);
		_symptom.setFont(new Font("���� ���", Font.PLAIN, 15));
		_symptom.addItem("����");

		message.add(_clinic);
		message.add(_diagnosis);
		message.add(_consulting);
		message.add(_confirm);
		message.add(dateChooser);
		message.add(_symptom);

		add(message);

	}

	public void resetComboBox(DefaultComboBoxModel DCBM) {
		_clinic.setModel(DCBM);
//		_clinic.insertItemAt(Constants.ALL, 0);
//		_clinic.setSelectedItem(Constants.ALL);

	}

}