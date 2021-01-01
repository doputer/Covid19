package sejong.corona;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.toedter.calendar.JDateChooser;

public class UserView extends JFrame {

	static TriageRoomAPI triageRoomModel;

	protected JButton reserve1Btn, reserveCheckBtn, connect1Btn, cancel1Btn;
	// 예약, 예약 확인, 문의, 취소
	protected JButton nextBtn, cancel2Btn;
	// 다음, 취소
	protected JButton reserve2Btn, cancel3Btn;
	// 등록, 취소
	protected JButton confirmBtn, connect2Btn;
	// 확인, 문의하기
	protected JTextField name, phone;
	// 이름, 핸드폰번호
	protected JTextField address, birth, etc;
	// 주소, 생년월일
	protected JComboBox hospitalCb;
	// 선별진료소
	protected JLabel numLabel;
	// 예약 인원 수 (출력)
	protected JRadioButton gender[];
	// 성별
	protected JCheckBox symptom1, symptom2, symptom3, symptom4;
	// 증상
	protected JDateChooser date;
	// 캘린더

	JPanel reservePnl, checkPnl, writePnl, choosePnl;

	JLabel logo;
	Image img;
	
	UserChatUI userChatUI;
	
	int backNum = 1; // background flag

	public UserView() {
		setTitle("코로나 선별 진료소 예약 시스템");
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		try {
			triageRoomModel = new TriageRoomAPI();
		} catch (IOException e) {
			e.printStackTrace();
		}
		reserveCheckUI();
		writeSymptomUI();
		chooseHospitalUI();
		reserveUI();

		setSize(640, 440);
		setLocationRelativeTo(null);
		setVisible(true);
		
		UserView.this.addButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();
				
				if (obj == reserve1Btn) {
					switchPanel(reservePnl, writePnl);
				} else if (obj == nextBtn) {
					switchPanel(writePnl, choosePnl);
				} else if (obj == cancel2Btn) {
					switchPanel(writePnl, reservePnl);
				} else if (obj == cancel3Btn) {
					switchPanel(choosePnl, writePnl);
				} else if (obj == reserve2Btn) {
					switchPanel(choosePnl, checkPnl);
				} else if (obj == confirmBtn) {
					switchPanel(checkPnl, reservePnl);
				} else if (obj == reserveCheckBtn) {
					switchPanel(reservePnl, checkPnl);
				} else if (obj == cancel1Btn) {
					UserView.this.setVisible(false);
				}
			}
		});
	}

	private void backGround(int backNum, JPanel p) {
		if (backNum == 1) {
			img = new ImageIcon(UserView.class.getResource("/sejong/corona/background.png")).getImage();

		} else if (backNum == 2) {
			img = new ImageIcon(UserView.class.getResource("/sejong/corona/background2.png")).getImage();
		}
		logo = new JLabel(new ImageIcon(img));
		logo.setBounds(0, 0, 640, 440);

		p.add(logo);
	}
	
	private void switchPanel(JPanel originPnl, JPanel newPnl) {
		originPnl.setVisible(false);
		newPnl.setVisible(true);
		this.setContentPane(newPnl);
	}

	// 예약 초기 화면
	public void reserveUI() {
		reservePnl = new JPanel();
		reservePnl.setLayout(null);

		backNum = 1;

		name = new JTextField(10);
		phone = new JTextField(10);

		reserve1Btn = new JButton("예약하기");
		reserveCheckBtn = new JButton("예약 확인");
		connect1Btn = new JButton("문의하기");
		connect1Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        if (e.getSource() == connect1Btn) {
		            if (userChatUI == null) {
		            	userChatUI = new UserChatUI(UserView.this, "문의", name.getText());
		            }
		            userChatUI.setVisible(true);
		        }
		    }
		});
		cancel1Btn = new JButton("취소");

		name.setBounds(280, 180, 200, 30);
		reservePnl.add(name);

		phone.setBounds(280, 230, 200, 30);
		reservePnl.add(phone);

		JLabel lbl = new JLabel("이름");
		lbl.setBounds(200, 180, 50, 30);
		reservePnl.add(lbl);

		JLabel lbl2 = new JLabel("핸드폰");
		lbl2.setBounds(200, 230, 50, 30);
		reservePnl.add(lbl2);

		reserve1Btn.setBounds(60, 300, 100, 50);
		reservePnl.add(reserve1Btn);
		reserveCheckBtn.setBounds(200, 300, 100, 50);
		reservePnl.add(reserveCheckBtn);
		connect1Btn.setBounds(340, 300, 100, 50);
		reservePnl.add(connect1Btn);
		cancel1Btn.setBounds(480, 300, 100, 50);
		reservePnl.add(cancel1Btn);

		reservePnl.setVisible(true);
		UserView.this.setContentPane(reservePnl);

		backGround(backNum, reservePnl);
	}

	// 예약 확인 화면
	public void reserveCheckUI() {
		checkPnl = new JPanel();
		checkPnl.setLayout(null);
		
		backNum = 1;

		// 버튼
		confirmBtn = new JButton("확인");
		connect2Btn = new JButton("문의하기");

		confirmBtn.setBounds(190, 300, 100, 45);
		checkPnl.add(confirmBtn);
		connect2Btn.setBounds(350, 300, 100, 45);
		checkPnl.add(connect2Btn);

		// Text
// 임의로 지정
		String name = "김자바";
		String hos = "광진구청";
		String d = "2021-01-04";
		String st = "예약 대기";

		JLabel txt = new JLabel("");
		txt.setBounds(170, 150, 100, 30);
		txt.setText(name + "  님,");

		JLabel txt2 = new JLabel("");
		txt2.setBounds(170, 170, 300, 30);
		txt2.setText("예약하신 선별진료소는        " + hos + "        입니다.");

		JLabel txt3 = new JLabel("");
		txt3.setBounds(170, 200, 300, 30);
		txt3.setText("예약 날짜는        " + d + "        입니다.");

		JLabel txt4 = new JLabel("");
		txt4.setBounds(170, 240, 300, 30);
		txt4.setText("예약 현황        " + st + "        입니다.");

		checkPnl.add(txt);
		checkPnl.add(txt2);
		checkPnl.add(txt3);
		checkPnl.add(txt4);

		// Panel
		JPanel p1 = new JPanel();
		p1.setBounds(310, 170, 70, 28);
		p1.setBackground(Color.LIGHT_GRAY);
		JPanel p2 = new JPanel();
		p2.setBounds(240, 202, 90, 28);
		p2.setBackground(Color.LIGHT_GRAY);
		JPanel p3 = new JPanel();
		p3.setBounds(235, 240, 80, 28);
		p3.setBackground(Color.LIGHT_GRAY);
		checkPnl.add(p1);
		checkPnl.add(p2);
		checkPnl.add(p3);

	    backGround(backNum, checkPnl);
		checkPnl.setVisible(false);
	}

	// 문진표 작성 화면
	public void writeSymptomUI() {
		writePnl = new JPanel();
		writePnl.setLayout(null);

		backNum = 2;

		// 버튼
		nextBtn = new JButton("다음");
		cancel2Btn = new JButton("취소");

		nextBtn.setBounds(190, 300, 100, 45);
		writePnl.add(nextBtn);
		cancel2Btn.setBounds(350, 300, 100, 45);
		writePnl.add(cancel2Btn);

		// Text
		address = new JTextField(10);
		birth = new JTextField(10);
		etc = new JTextField(10);

		address.setBounds(130, 130, 150, 30);
		writePnl.add(address);
		birth.setBounds(130, 180, 150, 30);
		writePnl.add(birth);
		etc.setBounds(390, 230, 150, 30);
		writePnl.add(etc);

		// 라벨
		JLabel lbl = new JLabel("주소");
		lbl.setBounds(70, 130, 50, 30);
		writePnl.add(lbl);

		JLabel lbl2 = new JLabel("생년월일");
		lbl2.setBounds(70, 180, 50, 30);
		writePnl.add(lbl2);

		JLabel lbl3 = new JLabel("성별");
		lbl3.setBounds(70, 230, 50, 30);
		writePnl.add(lbl3);

		JLabel lbl4 = new JLabel("증상");
		lbl4.setBounds(330, 130, 50, 30);
		writePnl.add(lbl4);

		JLabel lbl5 = new JLabel("기타사항");
		lbl5.setBounds(330, 230, 50, 30);
		writePnl.add(lbl5);

		// Radio 버튼
		gender = new JRadioButton[2];
		String g[] = { "남", "여" };

		ButtonGroup group = new ButtonGroup();
		for (int i = 0; i < gender.length; i++) {
			gender[i] = new JRadioButton(g[i]);
			group.add(gender[i]);
			writePnl.add(gender[i]);
		}
		gender[0].setBounds(140, 230, 50, 30);
		gender[1].setBounds(210, 230, 50, 30);
		gender[0].setBackground(Color.WHITE);
		gender[1].setBackground(Color.WHITE);
		gender[0].setSelected(false);
		gender[1].setSelected(true);

		// Checkbox
		symptom1 = new JCheckBox("발열");
		symptom2 = new JCheckBox("어지러움");
		symptom3 = new JCheckBox("기침");
		symptom4 = new JCheckBox("오한");

		symptom1.setBounds(390, 130, 50, 30);
		symptom1.setBackground(Color.WHITE);
		symptom2.setBounds(390, 170, 80, 30);
		symptom2.setBackground(Color.WHITE);
		symptom3.setBounds(470, 130, 50, 30);
		symptom3.setBackground(Color.WHITE);
		symptom4.setBounds(470, 170, 50, 30);
		symptom4.setBackground(Color.WHITE);
		writePnl.add(symptom1);
		writePnl.add(symptom2);
		writePnl.add(symptom3);
		writePnl.add(symptom4);

	    backGround(backNum, writePnl);
		writePnl.setVisible(false);
	}

	// 선별진료소,날짜 선택 화면
	public void chooseHospitalUI() {
		choosePnl = new JPanel();
		choosePnl.setLayout(null);
		backNum = 2;

		setTitle("코로나 선별 진료소 예약 시스템");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		setSize(640, 440);
		setLocationRelativeTo(null);

		// 버튼
		reserve2Btn = new JButton("등록");
		cancel3Btn = new JButton("취소");

		reserve2Btn.setBounds(190, 300, 100, 45);
		choosePnl.add(reserve2Btn);
		cancel3Btn.setBounds(350, 300, 100, 45);
		choosePnl.add(cancel3Btn);

		// 라벨
		JLabel lbl = new JLabel("선별진료소");
		lbl.setBounds(70, 130, 80, 30);
		choosePnl.add(lbl);

		JLabel lbl2 = new JLabel("예약 인원수");
		lbl2.setBounds(70, 180, 80, 30);
		choosePnl.add(lbl2);

		JLabel lbl3 = new JLabel("예약 날짜");
		lbl3.setBounds(330, 130, 80, 30);
		choosePnl.add(lbl3);

		numLabel = new JLabel("");
		numLabel.setBounds(170, 180, 150, 30);
//임의로 지정해둠 나중에 수정
		numLabel.setText(" 01 / 40 ");
		choosePnl.add(numLabel);

		// 콤보박스
		hospitalCb = new JComboBox<String>(FrontUI.triageRoomModel.getTriageRoom());
		hospitalCb.setBounds(150, 130, 110, 30);
		choosePnl.add(hospitalCb);

		// DateChooser
		date = new JDateChooser();
	    date.setDateFormatString("yyyy-MM-dd");
	    date.setBounds(390, 130, 170, 40);
	    date.getJCalendar().setPreferredSize(new Dimension(170, 200)); 
	    choosePnl.add(date);

		backGround(backNum, choosePnl);
		choosePnl.setVisible(false);
	}

	public void addButtonActionListener(ActionListener listener) {
		// Event Listener
		reserve1Btn.addActionListener(listener);
		reserveCheckBtn.addActionListener(listener);
		connect1Btn.addActionListener(listener);
		cancel1Btn.addActionListener(listener);
		nextBtn.addActionListener(listener);
		cancel2Btn.addActionListener(listener);
		reserve2Btn.addActionListener(listener);
		cancel3Btn.addActionListener(listener);
		confirmBtn.addActionListener(listener);
		connect2Btn.addActionListener(listener);

	}
}
