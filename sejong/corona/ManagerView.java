package sejong.corona;

import javax.swing.JFrame;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ManagerView extends JFrame {
	JTextArea textL;
	JLabel lblmessage;
	JComboBox<String> _clinic;
	JComboBox<String> _symptom;
	JList<String> List;
	JDateChooser dateChooser;
	ManagerController controlL;

	DiagnosisUI diagnosisUI;
	ManagerChatUI managerChatUI;
	String symptom[] = { UserView.symptom1Name, UserView.symptom2Name, UserView.symptom3Name, UserView.symptom4Name };
	ManagerDAO dao;
	public DefaultListModel<String> listVct;

	public int cnt = 0;

	public ManagerView() {
		setFrame();
		setOption();

		setVisible(true);
	}

	public void setFrame() {
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

		listVct = new DefaultListModel<String>();
		List = new JList<String>(listVct);
		List.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		JScrollPane scroll = new JScrollPane(List);
		scroll.setBounds(10, 200, 870, 450);
		add(scroll);

		dao = new ManagerDAO();
		controlL = new ManagerController(this, dao);

		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(250, 100, 170, 40);
		dateChooser.getJCalendar().setPreferredSize(new Dimension(170, 200));

		JLabel mNumber = new JLabel(cnt + "/40");
		mNumber.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		mNumber.setBounds(830, 160, 100, 20);
		message.add(mNumber);
		
		JButton _diagnosis = new JButton("예약자 진단");
		_diagnosis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == _diagnosis) {
					if (diagnosisUI == null) {
						diagnosisUI = new DiagnosisUI(ManagerView.this, "진단");
					}
					diagnosisUI.setVisible(true);
					_diagnosis.requestFocus();
				}
			}
		});
		JButton _consulting = new JButton("예약자 상담");
		_consulting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == _consulting) {
//		            if (managerChatUI == null) {
					managerChatUI = new ManagerChatUI(ManagerView.this, "상담하기"); // 계속 생성하지 말고 위치만 저장해놓기
//		            }
					managerChatUI.setVisible(true);
				}
			}
		});
		JButton _confirm = new JButton("예약자 조회");
		_confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == _confirm) {
					controlL.refresh();
					mNumber.setText(cnt + "/40");
					cnt = 0;
				}
			}
		});

		_diagnosis.setBounds(40, 30, 150, 40);
		_consulting.setBounds(680, 30, 150, 40);
		_confirm.setBounds(680, 100, 150, 40);

		

		_clinic = new JComboBox<String>(FrontUI.triageRoomModel.getTriageRoom());
		_clinic.setBounds(40, 100, 170, 40);
		_clinic.setFont(new Font("맑은 고딕", Font.PLAIN, 15));

		_symptom = new JComboBox<String>(symptom);
		_symptom.setBounds(460, 100, 170, 40);
		_symptom.setFont(new Font("맑은 고딕", Font.PLAIN, 15));

		message.add(_clinic);
		message.add(_diagnosis);
		message.add(_consulting);
		message.add(_confirm);
		message.add(dateChooser);
		message.add(_symptom);

		add(message);

	}
}