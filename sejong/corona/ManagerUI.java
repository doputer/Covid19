package sejong.corona;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ManagerUI extends JFrame {
	JButton _diagnosis, _consulting, _confirm, _delete;
	JTextArea textL;
	JLabel mNumber;
	JComboBox<String> _clinic;
	
	JTable dataTbl;
	String header[] = { "아이디", "이름", "연락처", "주소", "생년월일", "성별", "발열", "어지러움", "기침", "오한", "기타", "선별진료소", "예약일자", "예약상태",
	"진단결과" };
	String contents[][] = {};
	DefaultTableModel model = new DefaultTableModel(contents, header);
	
	JDateChooser dateChooser;

	DiagnosisUI diagnosisUI;
	ManagerChatUI managerChatUI;
	ManagerDAO dao = new ManagerDAO();
	ManagerController controll;

	int col = -1, row = -1;

	public ManagerUI(JFrame frame) {
		setFrame();
		setOption();

		setVisible(true);
		this.setLocation(frame.getX(), frame.getY());
		setResizable(false);
	}

	public void setFrame() {
		setTitle("코로나 선별진료소 관리 시스템");
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
		
		_diagnosis = new JButton("예약자 진단");
		_consulting = new JButton("예약자 상담");
		_confirm = new JButton("예약자 조회");
		_delete = new JButton("삭제");

		_diagnosis.setBounds(40, 30, 150, 40);
		_diagnosis.setBackground(new Color(230, 250, 250));
		_consulting.setBounds(680, 30, 150, 40);
		_consulting.setBackground(new Color(230, 250, 250));
		_confirm.setBounds(460, 100, 150, 40);
		_confirm.setBackground(new Color(230, 250, 250));
		_delete.setBounds(680, 100, 150, 40);
		_delete.setBackground(new Color(230, 250, 250));

		_clinic = new JComboBox<String>(FrontUI.triageRoomModel.getTriageRoom());
		_clinic.setBounds(40, 100, 170, 40);
		_clinic.setFont(new Font("맑은 고딕", Font.PLAIN, 15));

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
		message.add(scroll);

		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(250, 100, 170, 40);
		dateChooser.getJCalendar().setPreferredSize(new Dimension(170, 200));

		mNumber = new JLabel("현재 진료소 인원 수: 0");
		mNumber.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		mNumber.setBounds(730, 175, 230, 20);
		
		controll = new ManagerController(this, dao);
		
		message.add(mNumber);
		message.add(_clinic);
		message.add(_diagnosis);
		message.add(_consulting);
		message.add(_confirm);
		message.add(_delete);
		message.add(dateChooser);

		add(message);

		new FontManager(this.getComponents());
	}
	
	public void addTableActionListener(ListSelectionListener listener) {
		dataTbl.getSelectionModel().addListSelectionListener(listener);
	}
	
	public void addButtonActionListener(ActionListener listener) {
		_diagnosis.addActionListener(listener);
		_consulting.addActionListener(listener);
		_confirm.addActionListener(listener);
		_delete.addActionListener(listener);
	}
}