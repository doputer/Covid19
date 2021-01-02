package sejong.corona;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

import com.toedter.calendar.JDateChooser;

public class UserUI extends JFrame{
	
	protected JButton reserve1Btn, reserveCheckBtn,connect1Btn, cancel1Btn;
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
	protected JComboBox<String> hospitalCb;
						// 선별진료소
	protected JLabel numLabel;
						// 예약 인원 수 (출력)
	private JLabel checkLabel1, checkLabel2, checkLabel3, checkLabel4;
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
	private int id;
	
	JPanel reservePnl, checkPnl, writePnl, choosePnl;
			//예약 첫화면, 예약 확인, 문진표 작성, 선별진료소 
	JLabel logo;
	Image img;
	int backNum = 1;	// background flag
	
	UserChatUI userChatUI;
	UserChatController userChatController;
	
	UserDAO dao = new UserDAO();
	
	
	public UserUI() {

		setTitle("코로나 선별 진료소 예약 시스템");
	    setLayout(null);

		reserveCheckUI();
		writeSymptomUI();
		chooseHospitalUI();
		reserveUI();

		setSize(640, 440);
		setLocationRelativeTo(null);
		setVisible(true);
		
		UserUI.this.addButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();

				if (obj == reserve1Btn) {		
					// 이름, 전화번호
					String naming = name.getText();
					String number = phone.getText();
					if(naming == null || naming.trim().length() == 0 
								|| number == null || number.trim().length() ==0) {
						// 비어있으면 통과X
					}
					else {
					
						// 해당 전화번호가 이전에 사용한 적이 있는지 검사
						UserDTO search = dao.searchPhone(number);
						UserDTO data = new UserDTO(naming, number);
						
						// 핸드폰 조회 시, 이미 등록되어 있는 User의 경우 update
						if( search != null) {
							dao.updateUser(data);	
						}
						// New User 일 경우 새로 등록
						else {
							dao.newUser(data);
						}
						
						// id 조회
						UserDTO dto = dao.getUserId(number);
						id = dto.getId();
						
						name.setText("");
						phone.setText("");
						switchPanel(reservePnl, writePnl);
					}
					
				} else if (obj == nextBtn) {
					// 주소, 생년월일, 성별, 증상 4개, 기타사항
					
					String addr = address.getText();
					String birthday = birth.getText();
					String sex;
					String sym1;
					String sym2;
					String sym3;
					String sym4;
					String other = etc.getText();
					
					if(gender[0].isSelected()) {
						sex = "Male";
						gender[0].setSelected(false);
					}else{
						sex = "Female";
						gender[1].setSelected(false);
					}
					if(symptom1.isSelected()) {
						sym1 = "True";
						symptom1.setSelected(false);
					}else
						sym1 = "False";
					if(symptom2.isSelected()) {
						sym2 = "True";
						symptom2.setSelected(false);
					}else
						sym2 = "False";
					if(symptom3.isSelected()) {
						sym3 = "True";
						symptom3.setSelected(false);
					}else
						sym3 = "False";
					if(symptom4.isSelected()) { 
						sym4 = "True";
						symptom4.setSelected(false);
					}
					else
						sym4 = "False";
					
					UserDTO data = new UserDTO(id, addr, birthday, sex);
					UserDTO dataSymptom = new UserDTO(sym1, sym2, sym3, sym4, other);

					// 이전에 detail 정보를 등록한적 있는지 검사
					UserDTO search = dao.IsRegister(id);
										
					// 등록한적 있다면 update
					if( search != null) {
						dao.updateOriginalUser(data, dataSymptom);
					}
					// 등록한적 없다면 새로 등록
					else {
						dao.originalUser(data, dataSymptom);
					}

					address.setText("");
					birth.setText("");
					etc.setText("");
					
					switchPanel(writePnl, choosePnl);

					
				} else if (obj == cancel2Btn) {		
					
					switchPanel(writePnl, reservePnl);
					
				} else if (obj == cancel3Btn) {		
						
					switchPanel(choosePnl, writePnl);
					
				} else if (obj == reserve2Btn) {
					// 진료소, 예약날짜 - Set, 인원수 - Get
					
					String clinic = (String)hospitalCb.getSelectedItem();
					String reserveDate = ((JTextField)date.getDateEditor().getUiComponent()).getText();
					if(clinic == null || clinic.trim().length() == 0 
							|| reserveDate == null || reserveDate.trim().length() ==0) {
						// 비어있으면 통과X
					}
					else {
						// 이전에 detail 정보를 등록한적 있는지 검사
						boolean search = dao.IsChoosing(id);
											
						// 등록한적 있다면 update
						if(search) {
							dao.updateReservation(id, clinic, reserveDate);
						}
						// 등록한적 없다면 새로 등록
						else {
							dao.chooseReservation(id, clinic, reserveDate);
						}
						
						// 예약확인 할 정보 불러오기
						UserDTO reservation = dao.checkReservation(id);

						checkLabel1.setText(reservation.getName() + "    님");
						checkLabel2.setText("예약하신 선별진료소는        " + reservation.getHospital() +  "                    입니다.");
						checkLabel3.setText("예약 날짜는         " + reservation.getDate() +"            입니다.");
						if( reservation.getStatus() == null) {
							checkLabel4.setText("예약 현황            예약 미승인               입니다.");
						}else {
							checkLabel4.setText("예약 현황            " + reservation.getStatus() + "               입니다.");
						}
						
						
						switchPanel(choosePnl, checkPnl);
					}
					
				} else if (obj == confirmBtn) {	
					// 예약정보 다 적고 -> 예약 첫번째 창으로 돌아가기
					switchPanel(checkPnl, reservePnl);
					
				} else if (obj == reserveCheckBtn) {
					// 첫 화면 -> 예약확인
					// 이름, 진료소, 예약날짜, 예약 상태 - Get
					
					// 이름, 전화번호
					String naming = name.getText();
					String number = phone.getText();
					if(naming == null || naming.trim().length() == 0 
								|| number == null || number.trim().length() ==0) {
						// 비어있으면 통과X
					}
					else {
						
						UserDTO dto = dao.getUserId(number);
						id = dto.getId();
						
						UserDTO reservation = dao.checkReservation(id);
						
						checkLabel1.setText(reservation.getName() + "    님");
						checkLabel2.setText("예약하신 선별진료소는        " + reservation.getHospital() +  "                    입니다.");
						checkLabel3.setText("예약 날짜는         " + reservation.getDate() +"            입니다.");
						if( reservation.getStatus() == null) {
							checkLabel4.setText("예약 현황            예약 미승인               입니다.");
						}else {
							checkLabel4.setText("예약 현황            " + reservation.getStatus() + "               입니다.");
						}
						switchPanel(reservePnl, checkPnl);
					}
					
				} else if (obj == cancel1Btn) {
					
					UserUI.this.setVisible(false);
				}
			}
		});
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
	
	private void switchPanel(JPanel originPnl, JPanel newPnl) {
		originPnl.setVisible(false);
		newPnl.setVisible(true);
		this.setContentPane(newPnl);
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
		
		connect1Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == connect1Btn) {
//					if (userChatUI == null) {
						userChatUI = new UserChatUI(UserUI.this, "문의하기", name.getText());
//					}
//					userChatController = new UserChatController(new ChatData(), userChatUI, name.getText());
					userChatUI.setVisible(true);
				}
			}
		});
		
		name.setBounds(280,180,200,30);
		reservePnl.add(name);
		
		phone.setBounds(280,230,200,30);
		reservePnl.add(phone);
		
		JLabel lbl = new JLabel("이름");
		lbl.setBounds(200, 180, 50, 30);
		reservePnl.add(lbl);
		
		JLabel lbl2 = new JLabel("핸드폰");
		lbl2.setBounds(200, 230, 50, 30);
		reservePnl.add(lbl2);
		
		reserve1Btn.setBounds(60, 300, 100, 50);
		reserve1Btn.setBackground(new Color(230,250,230));
		reservePnl.add(reserve1Btn);
		reserveCheckBtn.setBounds(200, 300, 100, 50);
		reserveCheckBtn.setBackground(new Color(230,250,230));
		reservePnl.add(reserveCheckBtn);
		connect1Btn.setBounds(340, 300, 100, 50);
		connect1Btn.setBackground(new Color(230,250,230));
		reservePnl.add(connect1Btn);
		cancel1Btn.setBounds(480, 300, 100, 50);
		cancel1Btn.setBackground(new Color(230,250,230));
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
		confirmBtn.setBackground(new Color(230,250,230));
		checkPnl.add(confirmBtn);
		connect2Btn.setBounds(350, 300, 100, 45);
		connect2Btn.setBackground(new Color(230,250,230));
		connect2Btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == connect2Btn) {
					userChatUI = new UserChatUI(UserUI.this, "문의하기", name.getText());
					userChatUI.setVisible(true);
				}
			}
		});
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
		
		// Panel
		JPanel p1 = new JPanel();
		p1.setBounds(310, 170, 150, 28);
		p1.setBackground(Color.LIGHT_GRAY);
		JPanel p2 = new JPanel();
		p2.setBounds(245, 202, 90, 28);
		p2.setBackground(Color.LIGHT_GRAY);
		JPanel p3 = new JPanel();
		p3.setBounds(240, 240, 100, 28);
		p3.setBackground(Color.LIGHT_GRAY);
		checkPnl.add(p1);
		checkPnl.add(p2);
		checkPnl.add(p3);
		
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
		nextBtn.setBackground(new Color(230,250,230));
		writePnl.add(nextBtn);
		cancel2Btn.setBounds(350, 300, 100, 45);
		cancel2Btn.setBackground(new Color(230,250,230));
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
		String g[] = {"남","여"};
		
		ButtonGroup group = new ButtonGroup();
		for(int i=0; i<gender.length; i++) {
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
		backNum = 2;
		
		choosePnl = new JPanel();
		choosePnl.setLayout(null);

		// 버튼
		reserve2Btn = new JButton("등록");
		cancel3Btn = new JButton("취소");
		
		reserve2Btn.setBounds(190, 300, 100, 45);
		reserve2Btn.setBackground(new Color(230,250,230));
		choosePnl.add(reserve2Btn);
		cancel3Btn.setBounds(350, 300, 100, 45);
		cancel3Btn.setBackground(new Color(230,250,230));
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

