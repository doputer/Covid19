package sejong.corona;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
	DefaultTableModel model;
	String header[] = { "아이디", "이름", "연락처", "주소", "생년월일", "성별", "발열", "어지러움", "기침", "오한", "기타", "선별진료소", "예약일자",
			"예약상태", "진단결과" };
	String contents[][] = {};
	JDateChooser dateChooser;
	ManagerController controlL;

	DiagnosisUI diagnosisUI;
	ManagerChatUI managerChatUI;
	String symptom[] = { UserUI.symptom1Name, UserUI.symptom2Name, UserUI.symptom3Name, UserUI.symptom4Name };
	ManagerDAO dao;
	
	int col = -1, row = -1;
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

		model = new DefaultTableModel(contents, header);
		dataTbl = new JTable(model) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		dataTbl.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e) {
		    	col = dataTbl.getSelectedColumn();
		    	if (dataTbl.getSelectedRow() != -1)
		    		row = dataTbl.getSelectedRow();
		    }
		});
		
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

		JLabel mNumber = new JLabel("현재 진료소 인원 수: " + cnt);
		mNumber.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
		mNumber.setBounds(625, 155, 230, 20);
		message.add(mNumber);

		JButton _diagnosis = new JButton("예약자 진단");
		_diagnosis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == _diagnosis) {
//					if (diagnosisUI == null) {
//					}
					if (row < dataTbl.getRowCount() && row > -1) {
						diagnosisUI = new DiagnosisUI(ManagerView.this, "진단", ManagerView.this);
						diagnosisUI.setVisible(true);
					}
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
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == _confirm) {
					controlL.refresh();
					mNumber.setText("현재 진료소 인원 수: " + cnt);
					cnt = 0;
				}
			}
		});
		
		JButton _delete = new JButton("삭제");
		_delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == _delete) {
					if (row < dataTbl.getRowCount() && row > -1) {
						dao.deleteUser(Integer.parseInt(dataTbl.getValueAt(row, 0).toString()));
						controlL.refresh();
					}
				}
			}
		});
		
		_diagnosis.setBounds(40, 30, 150, 40);
		_diagnosis.setBackground(new Color(230,250,250));
		_consulting.setBounds(680, 30, 150, 40);
		_consulting.setBackground(new Color(230,250,250));
		_confirm.setBounds(460, 100, 150, 40);
		_confirm.setBackground(new Color(230,250,250));
		_delete.setBounds(680, 100, 150, 40);
		_delete.setBackground(new Color(230,250,250));

		_clinic = new JComboBox<String>(FrontUI.triageRoomModel.getTriageRoom());
		_clinic.setBounds(40, 100, 170, 40);
		_clinic.setFont(new Font("맑은 고딕", Font.PLAIN, 15));

		message.add(_clinic);
		message.add(_diagnosis);
		message.add(_consulting);
		message.add(_confirm);
		message.add(_delete);
		message.add(dateChooser);

		add(message);

	}
}