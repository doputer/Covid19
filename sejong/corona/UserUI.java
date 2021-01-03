package sejong.corona;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

public class UserUI extends JFrame {

	protected JButton reserve1Btn, reserveCheckBtn, connect1Btn, cancel1Btn;
	// 예약, 예약 확인, 문의, 취소
	protected JButton nextBtn, cancel2Btn;
	// 다음, 취소
	protected JButton reserve2Btn, cancel3Btn;
	// 등록, 취소
	protected JButton confirmBtn, connect2Btn;
	// 확인, 문의하기
	public JTextField name, phone;
	// 이름, 핸드폰번호
	protected JTextField address, birth, etc;
	// 주소, 생년월일
	protected JComboBox<String> hospitalCb;
	// 선별진료소
	protected JLabel numLabel;
	// 예약 인원 수 (출력)
	public JLabel checkLabel1, checkLabel2, checkLabel3, checkLabel4;
	// 예약 확인 출력 라벨
	protected JRadioButton gender[];
	// 성별
	protected JCheckBox symptom1, symptom2, symptom3, symptom4;
	// 증상
	public static String symptom1Name = "발열";
	public static String symptom2Name = "어지러움";
	public static String symptom3Name = "기침";
	public static String symptom4Name = "오한";

	protected JDateChooser date;
	// 캘린더
	public int id;

	JPanel reservePnl, checkPnl, writePnl, choosePnl;
	// 예약 첫화면, 예약 확인, 문진표 작성, 선별진료소
	JLabel logo;
	Image img;
	int backNum = 1; // background flag

	UserChatUI userChatUI;
	UserChatController userChatController;

	UserDAO dao = new UserDAO();
	UserDTO reservation;

	public UserController controller;

	public UserUI(JFrame frame) {

		setTitle("코로나 선별 진료소 예약 시스템");
		setLayout(null);

		reserveCheckUI();
		writeSymptomUI();
		chooseHospitalUI();
		reserveUI();

		FontManager fm = new FontManager();
		fm.setDefaultFont(reservePnl.getComponents());
		fm.setDefaultFont(checkPnl.getComponents());
		fm.setDefaultFont(writePnl.getComponents());
		fm.setDefaultFont(choosePnl.getComponents());
		
		controller = new UserController(this, dao);

		setSize(640, 440);
		this.setLocation(frame.getX(), frame.getY());
		setVisible(true);
		setResizable(false);

	}

	private void backGround(int backNum, JPanel p) {
		if (backNum == 1) {
			img = new ImageIcon(UserUI.class.getResource("/sejong/corona/background.png")).getImage();

		} else if (backNum == 2) {
			img = new ImageIcon(UserUI.class.getResource("/sejong/corona/background2.png")).getImage();
		}
		logo = new JLabel(new ImageIcon(img));
		logo.setBounds(0, 0, 640, 440);

		p.add(logo);
	}

	// 예약 초기 화면
	public void reserveUI() {
		backNum = 1;

		reservePnl = new JPanel();
		reservePnl.setLayout(null);

		name = new JTextField(10);
		phone = new JTextField(10);

		reserve1Btn = new JButton("예약하기");
		reserveCheckBtn = new JButton("예약 확인");
		connect1Btn = new JButton("문의하기");
		cancel1Btn = new JButton("취소");

		name.setBounds(250, 180, 200, 30);
		reservePnl.add(name);

		phone.setBounds(250, 230, 200, 30);
		reservePnl.add(phone);

		JLabel lbl = new JLabel("이름");
		lbl.setBounds(180, 180, 50, 30);
		reservePnl.add(lbl);

		JLabel lbl2 = new JLabel("연락처");
		lbl2.setBounds(180, 230, 50, 30);
		reservePnl.add(lbl2);

		reserve1Btn.setBounds(60, 300, 100, 50);
		reserve1Btn.setBackground(new Color(230, 250, 230));
		reservePnl.add(reserve1Btn);
		reserveCheckBtn.setBounds(200, 300, 100, 50);
		reserveCheckBtn.setBackground(new Color(230, 250, 230));
		reservePnl.add(reserveCheckBtn);
		connect1Btn.setBounds(340, 300, 100, 50);
		connect1Btn.setBackground(new Color(230, 250, 230));
		reservePnl.add(connect1Btn);
		cancel1Btn.setBounds(480, 300, 100, 50);
		cancel1Btn.setBackground(new Color(230, 250, 230));
		reservePnl.add(cancel1Btn);

		reservePnl.setVisible(true);
		UserUI.this.setContentPane(reservePnl);

		backGround(backNum, reservePnl);
	}

	// 예약 확인 화면
	public void reserveCheckUI() {
		backNum = 1;

		checkPnl = new JPanel();
		checkPnl.setLayout(null);

		// 버튼
		confirmBtn = new JButton("확인");
		connect2Btn = new JButton("문의하기");

		confirmBtn.setBounds(190, 300, 100, 45);
		confirmBtn.setBackground(new Color(230, 250, 230));
		checkPnl.add(confirmBtn);
		connect2Btn.setBounds(350, 300, 100, 45);
		connect2Btn.setBackground(new Color(230, 250, 230));
		connect2Btn.setBackground(new Color(230, 250, 230));

		checkPnl.add(connect2Btn);

		// Text
		checkLabel1 = new JLabel("");
		checkLabel1.setBounds(170, 150, 100, 30);

		checkLabel2 = new JLabel("");
		checkLabel2.setBounds(170, 170, 400, 30);

		checkLabel3 = new JLabel("");
		checkLabel3.setBounds(170, 200, 300, 30);

		checkLabel4 = new JLabel("");
		checkLabel4.setBounds(170, 240, 300, 30);

		checkPnl.add(checkLabel1);
		checkPnl.add(checkLabel2);
		checkPnl.add(checkLabel3);
		checkPnl.add(checkLabel4);

		backGround(backNum, checkPnl);
		checkPnl.setVisible(false);
	}

	// 문진표 작성 화면
	public void writeSymptomUI() {
		backNum = 2;

		writePnl = new JPanel();
		writePnl.setLayout(null);

		// 버튼
		nextBtn = new JButton("다음");
		cancel2Btn = new JButton("취소");

		nextBtn.setBounds(190, 300, 100, 45);
		nextBtn.setBackground(new Color(230, 250, 230));
		writePnl.add(nextBtn);
		cancel2Btn.setBounds(350, 300, 100, 45);
		cancel2Btn.setBackground(new Color(230, 250, 230));
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
		lbl.setBounds(70, 130, 60, 30);
		writePnl.add(lbl);

		JLabel lbl2 = new JLabel("생년월일");
		lbl2.setBounds(70, 180, 60, 30);
		writePnl.add(lbl2);

		JLabel lbl3 = new JLabel("성별");
		lbl3.setBounds(70, 230, 60, 30);
		writePnl.add(lbl3);

		JLabel lbl4 = new JLabel("증상");
		lbl4.setBounds(330, 130, 80, 30);
		writePnl.add(lbl4);

		JLabel lbl5 = new JLabel("기타사항");
		lbl5.setBounds(330, 230, 80, 30);
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
		symptom1 = new JCheckBox(symptom1Name);
		symptom2 = new JCheckBox(symptom2Name);
		symptom3 = new JCheckBox(symptom3Name);
		symptom4 = new JCheckBox(symptom4Name);

		symptom1.setBounds(390, 130, 80, 30);
		symptom1.setBackground(Color.WHITE);
		symptom2.setBounds(390, 170, 80, 30);
		symptom2.setBackground(Color.WHITE);
		symptom3.setBounds(470, 130, 80, 30);
		symptom3.setBackground(Color.WHITE);
		symptom4.setBounds(470, 170, 80, 30);
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
		backNum = 2;

		choosePnl = new JPanel();
		choosePnl.setLayout(null);

		// 버튼
		reserve2Btn = new JButton("등록");
		reserve2Btn.setEnabled(false);
		cancel3Btn = new JButton("취소");

		reserve2Btn.setBounds(190, 300, 100, 45);
		reserve2Btn.setBackground(new Color(230, 250, 230));
		choosePnl.add(reserve2Btn);
		cancel3Btn.setBounds(350, 300, 100, 45);
		cancel3Btn.setBackground(new Color(230, 250, 230));
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
		numLabel.setBounds(150, 180, 80, 30);

		numLabel.setText("0명");
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
		hospitalCb.addActionListener(listener);
	}
}
