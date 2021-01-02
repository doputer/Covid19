package sejong.corona;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import java.awt.Dimension;
import java.awt.event.*;

public class DiagnosisUI extends JDialog implements ActionListener {
	JTextField nameTf;
	JButton symptomBtn;
	JTextArea diagnosisTa;
	JComboBox<String> hospitalCb, dateCb;
	JButton okBtn, cancleBtn;
	JDateChooser dateChooser;

	DiagnosisUI(JFrame frame, String title) { // 파라미터로 예약자 정보 가져오기
		super(frame, title);
		setLayout(null);

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
		nameTf.setBounds(40, 20, 120, 30);
		nameTf.setText("홍길동");
		nameTf.setEditable(false);
		nameTf.setHorizontalAlignment(JLabel.CENTER);
		symptomBtn = new JButton("문진표 보기");
		symptomBtn.setBounds(180, 20, 120, 30);
		diagnosisTa = new JTextArea();
		diagnosisTa.setBounds(40, 60, 260, 240);
		okBtn = new JButton("확인");
		okBtn.setBounds(80, 380, 80, 30);
		cancleBtn = new JButton("취소");
		cancleBtn.setBounds(180, 380, 80, 30);

		hospitalCb = new JComboBox<String>(FrontUI.triageRoomModel.getTriageRoom());
		hospitalCb.setBounds(40, 320, 120, 30);

		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(180, 320, 120, 30);
		dateChooser.getJCalendar().setPreferredSize(new Dimension(170, 200));

		add(nameTf);
		add(symptomBtn);
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
