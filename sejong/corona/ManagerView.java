package sejong.corona;

import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ManagerView extends JFrame {
	JTextArea textL;
	JLabel lblmessage;
	JComboBox<String> _clinic;
	JComboBox<String> _symptom;
	JTable dataTbl;
	JDateChooser dateChooser;
	ManagerController controlL;

	DiagnosisUI diagnosisUI;
	ManagerChatUI managerChatUI;
	String symptom[] = { UserUI.symptom1Name, UserUI.symptom2Name, UserUI.symptom3Name, UserUI.symptom4Name };
	ManagerDAO dao;
	public DefaultTableModel model;

	public int cnt = 0;

	public ManagerView() {
		setFrame();
		setOption();
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				dao.connectDB();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				dao.closeDB();
			}
		});

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

		String header[] = { "아이디", "이름", "연락처", "주소", "생년월일", "성별", "증상1", "증상2", "증상3", "증상4", "기타", "선별진료소", "예약일자",
				"예약상태" };
		model = new DefaultTableModel(null, header);
		dataTbl = new JTable(model) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		dataTbl.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		JScrollPane scroll = new JScrollPane(dataTbl, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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