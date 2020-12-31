package sejong.corona;

import javax.swing.*;
import java.awt.event.*;

public class DiagnosisUI extends JDialog implements ActionListener {
	JTextField nameTf;
	JButton symptomBtn;
	JTextArea diagnosisTa;
	JComboBox<String> hospitalCb, dateCb;
	JButton okBtn, cancleBtn;

	DiagnosisUI(JFrame frame, String title) { // �Ķ���ͷ� ������ ���� ��������		
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
		nameTf = new JTextField(10); // ���Ŀ� ����
		nameTf.setBounds(40, 20, 120, 30);
		nameTf.setText("ȫ�浿");
		nameTf.setEditable(false);
		nameTf.setHorizontalAlignment(JLabel.CENTER);
		symptomBtn = new JButton("����ǥ ����");
		symptomBtn.setBounds(180, 20, 120, 30);
		diagnosisTa = new JTextArea();
		diagnosisTa.setBounds(40, 60, 260, 240);
		okBtn = new JButton("Ȯ��");
		okBtn.setBounds(80, 380, 80, 30);
		cancleBtn = new JButton("���");
		cancleBtn.setBounds(180, 380, 80, 30);
		
		hospitalCb = new JComboBox<String>(FrontUI.triageRoomModel.getTriageRoom());
		hospitalCb.setBounds(40, 320, 120, 30);

		add(nameTf);
		add(symptomBtn);
		add(diagnosisTa);
		add(hospitalCb);
		add(okBtn);
		add(cancleBtn);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();
	}
}
