package sejong.corona;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DiagnosisUI extends JDialog implements ActionListener {
	JTextField nameTf;
	JTextArea diagnosisTa;
	JComboBox<String> hospitalCb, dateCb;
	JButton okBtn, cancleBtn;
	JDateChooser dateChooser;
	ManagerView view;
	int row, col;

	DiagnosisUI(JFrame frame, String title, ManagerView view) { // 파라미터로 예약자 정보 가져오기
		super(frame, title);
		setLayout(null);

		this.view = view;
		this.row = view.row;
		this.col = view.col;
		
		startUI();

		setSize(360, 480);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				dispose();
			}
		});
	}

	private void startUI() {
		nameTf = new JTextField(10); // 추후에 수정
		nameTf.setBounds(40, 20, 260, 30);
		nameTf.setBackground(Color.white);
		nameTf.setText("선택한 예약자: " + view.dataTbl.getValueAt(row, 1).toString());
		nameTf.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		nameTf.setEditable(false);
		nameTf.setHorizontalAlignment(JLabel.CENTER);
		diagnosisTa = new JTextArea();
		diagnosisTa.setBounds(40, 60, 260, 240);
		okBtn = new JButton("확인");
		okBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == okBtn) {
					int id = Integer.parseInt(view.dataTbl.getValueAt(row, 0).toString());
					String result = diagnosisTa.getText();
					String hospital = hospitalCb.getSelectedItem().toString();
					String date = view.controlL.toDate(dateChooser.getDate());
					
					view.dao.updateUser(id, result, hospital, date);
					view.cnt = 0;
					view.controlL.refresh();
					dispose();
				}
			}
		});
		okBtn.setBounds(80, 380, 80, 30);
		cancleBtn = new JButton("취소");
		cancleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == cancleBtn) {
					dispose();
				}
			}
		});
		cancleBtn.setBounds(180, 380, 80, 30);

		hospitalCb = new JComboBox<String>(FrontUI.triageRoomModel.getTriageRoom());
		hospitalCb.setBounds(40, 320, 120, 30);
		hospitalCb.setSelectedItem(view.dataTbl.getValueAt(row, 11).toString());

		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(180, 320, 120, 30);
		dateChooser.getJCalendar().setPreferredSize(new Dimension(170, 200));
		
		Calendar calendar = new GregorianCalendar();
		String date[] = view.dataTbl.getValueAt(row, 12).toString().split("-");
		calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
		dateChooser.setCalendar(calendar);
		
		add(nameTf);
		add(diagnosisTa);
		add(hospitalCb);
		add(okBtn);
		add(cancleBtn);
		add(dateChooser);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();
	}
}
