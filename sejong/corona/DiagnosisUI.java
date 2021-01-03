package sejong.corona;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DiagnosisUI extends JFrame {
	JTextField nameTf;
	JTextArea diagnosisTa;
	JComboBox<String> hospitalCb, dateCb;
	JButton okBtn, cancleBtn;
	JDateChooser dateChooser;
	int row, col;

	ManagerUI view;
	DiagnosisController controller;

	DiagnosisUI(JFrame frame, String title, ManagerUI view) { // 파라미터로 예약자 정보 가져오기
		setTitle(title);
		setLayout(null);

		this.view = view;
		this.row = view.row;
		this.col = view.col;

		startUI();

		setSize(360, 480);
		this.setLocation((int) (frame.getX() - this.getWidth() + 16), frame.getY());
	}

	private void startUI() {
		nameTf = new JTextField(10);
		nameTf.setBounds(40, 20, 260, 30);
		nameTf.setBackground(Color.white);
		nameTf.setText("선택한 예약자: " + view.dataTbl.getValueAt(row, 1).toString());
		nameTf.setEditable(false);
		nameTf.setHorizontalAlignment(JLabel.CENTER);

		diagnosisTa = new JTextArea();
		diagnosisTa.setBounds(40, 60, 260, 240);

		okBtn = new JButton("확인");
		okBtn.setBounds(80, 380, 80, 30);
		cancleBtn = new JButton("취소");
		cancleBtn.setBounds(180, 380, 80, 30);

		hospitalCb = new JComboBox<String>(FrontUI.triageRoomModel.getTriageRoom());
		hospitalCb.setBounds(40, 320, 120, 30);
		if (view.dataTbl.getValueAt(row, 11) != null)
			hospitalCb.setSelectedItem(view.dataTbl.getValueAt(row, 11).toString());

		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-mm-dd");
		dateChooser.setBounds(180, 320, 120, 30);
		dateChooser.getJCalendar().setPreferredSize(new Dimension(170, 200));

		if (view.dataTbl.getValueAt(row, 12) != null) {
			Calendar calendar = new GregorianCalendar();
			String date[] = view.dataTbl.getValueAt(row, 12).toString().split("-");
			calendar.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[2]));
			dateChooser.setCalendar(calendar);
		}

		controller = new DiagnosisController(this, view);

		add(nameTf);
		add(diagnosisTa);
		add(hospitalCb);
		add(okBtn);
		add(cancleBtn);
		add(dateChooser);

		new FontManager(this.getComponents());
	}

	public void addButtonActionListener(ActionListener listener) {
		okBtn.addActionListener(listener);
		cancleBtn.addActionListener(listener);
	}
}
