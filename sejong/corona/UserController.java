package sejong.corona;

import java.awt.Font;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

public class UserController {
	UserUI view;
	UserDAO dao;

	UserController(UserUI view, UserDAO dao) {
		this.view = view;
		this.dao = dao;

		view.addButtonActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();

				if (obj == view.reserve1Btn) {
					// 이름, 전화번호
					String naming = view.name.getText();
					String number = view.phone.getText();
					if (naming == null || naming.trim().length() == 0 || number == null
							|| number.trim().length() == 0) {
						// 비어있으면 통과X
					} else {

						// 해당 전화번호가 이전에 사용한 적이 있는지 검사
						UserDTO search = dao.searchPhone(number);
						UserDTO data = new UserDTO(naming, number);

						// 핸드폰 조회 시, 이미 등록되어 있는 User의 경우 update
						if (search != null) {
							String options[] = { "네", "아니오" };
							JLabel msg = new JLabel("이미 작성한 사용자입니다. 새로 작성하시겠습니까?");
							msg.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
							int option = JOptionPane.showOptionDialog(null, msg, "알림", JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
							if (option == JOptionPane.YES_OPTION) {
								view.dao.updateUser(data);
							} else {
								return;
							}
						}
						// New User 일 경우 새로 등록
						else {
							dao.newUser(data);
						}

						// id 조회
						UserDTO dto = dao.getUserId(number);
						view.id = dto.getId();

						if (search != null) {
						} else {
							dao.newResult(view.id);
						}

						view.name.setText("");
						view.phone.setText("");
						switchPanel(view.reservePnl, view.writePnl);
					}

				} else if (obj == view.nextBtn) {
					// 주소, 생년월일, 성별, 증상 4개, 기타사항

					String addr = view.address.getText();
					String birthday = view.birth.getText();
					String sex;
					String sym1;
					String sym2;
					String sym3;
					String sym4;
					String other = view.etc.getText();

					if (view.gender[0].isSelected()) {
						sex = "Male";
						view.gender[0].setSelected(false);
					} else {
						sex = "Female";
						view.gender[1].setSelected(false);
					}
					if (view.symptom1.isSelected()) {
						sym1 = "True";
						view.symptom1.setSelected(false);
					} else
						sym1 = "False";
					if (view.symptom2.isSelected()) {
						sym2 = "True";
						view.symptom2.setSelected(false);
					} else
						sym2 = "False";
					if (view.symptom3.isSelected()) {
						sym3 = "True";
						view.symptom3.setSelected(false);
					} else
						sym3 = "False";
					if (view.symptom4.isSelected()) {
						sym4 = "True";
						view.symptom4.setSelected(false);
					} else
						sym4 = "False";

					UserDTO data = new UserDTO(view.id, addr, birthday, sex);
					UserDTO dataSymptom = new UserDTO(sym1, sym2, sym3, sym4, other);

					// 이전에 detail 정보를 등록한적 있는지 검사
					UserDTO search = dao.IsRegister(view.id);

					// 등록한적 있다면 update
					if (search != null) {
						dao.updateOriginalUser(data, dataSymptom);
					}
					// 등록한적 없다면 새로 등록
					else {
						dao.originalUser(data, dataSymptom);
					}

					view.address.setText("");
					view.birth.setText("");
					view.etc.setText("");

					switchPanel(view.writePnl, view.choosePnl);

				} else if (obj == view.cancel2Btn) {

					switchPanel(view.writePnl, view.reservePnl);

				} else if (obj == view.cancel3Btn) {

					switchPanel(view.choosePnl, view.writePnl);

				} else if (obj == view.reserve2Btn) {
					// 진료소, 예약날짜 - Set, 인원수 - Get

					String clinic = (String) view.hospitalCb.getSelectedItem();
					String reserveDate = ((JTextField) view.date.getDateEditor().getUiComponent()).getText();
					if (clinic == null || clinic.trim().length() == 0 || reserveDate == null
							|| reserveDate.trim().length() == 0) {
						// 비어있으면 통과X
					} else {
						// 이전에 detail 정보를 등록한적 있는지 검사
						boolean search = dao.IsChoosing(view.id);

						// 등록한적 있다면 update
						if (search) {
							dao.updateReservation(view.id, clinic, reserveDate);
						}
						// 등록한적 없다면 새로 등록
						else {
							dao.chooseReservation(view.id, clinic, reserveDate);
						}

						// 예약확인 할 정보 불러오기
						view.reservation = dao.checkReservation(view.id);

						view.checkLabel1
								.setText("<html><font color='blue'>" + view.reservation.getName() + "</font> 님</html>");
						view.checkLabel2.setText("<html>예약하신 선별진료소는 <font color='blue'>"
								+ view.reservation.getHospital() + "</font> 입니다.</html>");
						view.checkLabel3.setText("<html>예약 날짜는 <font color='blue'>" + view.reservation.getDate()
								+ "</font> 입니다.</html>");
						if (view.reservation.getStatus() == null) {
							view.checkLabel4.setText("<html>예약 현황 <font color='blue'>예약대기</font> 입니다.</html>");
						} else {
							view.checkLabel4.setText("<html>예약 현황 <font color='blue'>" + view.reservation.getStatus()
									+ "</font> 입니다.</html>");
						}
						switchPanel(view.choosePnl, view.checkPnl);
					}

				} else if (obj == view.confirmBtn) {
					// 예약정보 다 적고 -> 예약 첫번째 창으로 돌아가기
					switchPanel(view.checkPnl, view.reservePnl);

				} else if (obj == view.reserveCheckBtn) {
					// 첫 화면 -> 예약확인
					// 이름, 진료소, 예약날짜, 예약 상태 - Get

					// 이름, 전화번호
					String naming = view.name.getText();
					String number = view.phone.getText();
					if (naming == null || naming.trim().length() == 0 || number == null
							|| number.trim().length() == 0) {
						// 비어있으면 통과X
					} else {

						UserDTO dto = dao.getUserId(number);

						if (dto == null) {
							JLabel msg = new JLabel("존재하지 않는 사용자 입니다.");
							msg.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
							new JOptionPane();
							JOptionPane.showMessageDialog(null, msg, "경고", JOptionPane.ERROR_MESSAGE);
						} else {
							view.id = dto.getId();

							view.reservation = dao.checkReservation(view.id);

							view.checkLabel1.setText(
									"<html><font color='blue'>" + view.reservation.getName() + "</font> 님</html>");
							view.checkLabel2.setText("<html>예약하신 선별진료소는 <font color='blue'>"
									+ view.reservation.getHospital() + "</font> 입니다.</html>");
							view.checkLabel3.setText("<html>예약 날짜는 <font color='blue'>" + view.reservation.getDate()
									+ "</font> 입니다.</html>");
							if (view.reservation.getStatus() == null) {
								view.checkLabel4.setText("<html>예약 현황 <font color='blue'>예약대기</font> 입니다.</html>");
							} else {
								view.checkLabel4.setText("<html>예약 현황 <font color='blue'>"
										+ view.reservation.getStatus() + "</font> 입니다.</html>");
							}
							switchPanel(view.reservePnl, view.checkPnl);

							view.name.setText("");
							view.phone.setText("");
						}
					}

				} else if (obj == view.cancel1Btn) {
					view.dispose();
				} else if (obj == view.connect2Btn) {
					view.reservation = dao.checkReservation(view.id);

					view.userChatUI = new UserChatUI(view, "문의하기", view.reservation.getName());
					view.userChatUI.setVisible(true);
				} else if (obj == view.connect1Btn) {
					if (!view.name.getText().equals("")) {
						view.userChatUI = new UserChatUI(view, "문의하기", view.name.getText());
						view.userChatUI.setVisible(true);
					}
				} else if (obj == view.hospitalCb) {
					if (view.date.getDate() != null) {
						String n = String.valueOf(dao.getUser(view.hospitalCb.getSelectedItem().toString(),
								new ManagerController(null, null).toDate(view.date.getDate())).size());
						view.numLabel.setText(n + "명");
					}
					if (view.hospitalCb.getSelectedItem().equals("전체") || view.date.getDate() == null) {
						view.reserve2Btn.setEnabled(false);
					} else
						view.reserve2Btn.setEnabled(true);
				}
			}
		});

		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				dao.connectDB();
			}

			@Override
			public void windowClosed(WindowEvent e) {
				dao.closeDB();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				dao.closeDB();
			}
		});

		view.date.addPropertyChangeListener("date", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				if (view.date.getDate() != null) {
					String n = String.valueOf(dao.getUser(view.hospitalCb.getSelectedItem().toString(),
							new ManagerController().toDate(view.date.getDate())).size());
					view.numLabel.setText(n + "명");
				}
				if (view.hospitalCb.getSelectedItem().equals("전체") || view.date.getDate() == null) {
					view.reserve2Btn.setEnabled(false);
				} else
					view.reserve2Btn.setEnabled(true);
			}
		});
	}

	private void switchPanel(JPanel originPnl, JPanel newPnl) {
		originPnl.setVisible(false);
		newPnl.setVisible(true);
		view.setContentPane(newPnl);
	}
}
