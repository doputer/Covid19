package sejong.corona;

import java.awt.event.*;
import java.util.*;

import javax.swing.event.*;

public class ManagerController {
	ManagerUI view;
	ManagerDAO dao;
	
	ManagerController() {
	}

	ManagerController(ManagerUI view, ManagerDAO dao) {
		this.view = view;
		this.dao = dao;

		view.addButtonActionListener(new ActionListener() { // 진단하기 혹은 상담하기를 누르면 새로운 창을 열어주는 메소드
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();

				if (obj == view._diagnosis) {
					if (view.row < view.dataTbl.getRowCount() && view.row > -1) {
						view.diagnosisUI = new DiagnosisUI(view, "진단", view);
						view.diagnosisUI.setVisible(true);
					}
				} else if (obj == view._consulting) {
					view.managerChatUI = new ManagerChatUI(view, "상담하기");
					view.managerChatUI.setVisible(true);
				} else if (obj == view._confirm) { 
					view.controll.refresh();
				} else if (obj == view._delete) { // 삭제하기를 누르면 선택된 행들을 deleteUser 메소드로 보내서 삭제할 수 있도록 함
					if (view.row < view.dataTbl.getRowCount() && view.row > -1) {
						if (view.dataTbl.getSelectedRows().length > 1) {
							for (int x : view.dataTbl.getSelectedRows()) {
								dao.deleteUser(Integer.parseInt(view.dataTbl.getValueAt(x, 0).toString()));
							}
						} else {
							dao.deleteUser(Integer.parseInt(view.dataTbl.getValueAt(view.row, 0).toString()));
						}
						view.controll.refresh();
					}
				}
			}
		});

		view.addTableActionListener(new ListSelectionListener() { // 선택된 테이블 행에 대한 정보를 기록하는 메소드
			public void valueChanged(ListSelectionEvent e) {
				view.col = view.dataTbl.getSelectedColumn();
				if (view.dataTbl.getSelectedRow() != -1)
					view.row = view.dataTbl.getSelectedRow();
			}
		});

		view.addWindowListener(new WindowAdapter() { // 창이 활성화 되면 DB에 연결하고, 창이 닫히면 DB에서 연결 종료
			@Override
			public void windowActivated(WindowEvent e) {
				dao.connectDB();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				dao.closeDB();
			}
		});
	}

	private Vector<String> makeInfo(UserDTO dto) { // 행에 정보를 적을 수 있도록 dto 값을 벡터에 넣어서 반환하는 메소드
		Vector<String> row = new Vector<String>();
		row.add(String.valueOf(dto.getId()));
		row.add(dto.getName());
		row.add(dto.getPhone());
		row.add(dto.getAddress());
		row.add(dto.getBirth());
		row.add(dto.getGender());
		row.add(dto.getSymptom2());
		row.add(dto.getSymptom1());
		row.add(dto.getSymptom3());
		row.add(dto.getSymptom4());
		row.add(dto.getEtc());
		row.add(dto.getHospital());
		row.add(dto.getDate());
		row.add(dto.getStatus());
		row.add(dto.getResult());

		return row;
	}

	public void refresh() { // 화면을 갱신해주는 메소드로 선별 진료소 콤보박스와 일자 선택에서 일어날 수 있는 모든 경우의 수를 예외 처리
		view.model.setRowCount(0);
		ArrayList<UserDTO> dtos = new ArrayList<UserDTO>();

		if (view._clinic.getSelectedItem().toString().equals("전체") && view.dateChooser.getDate() == null) {
			dtos = dao.getAllUser();

			for (UserDTO dto : dtos) {
				view.model.addRow(makeInfo(dto));
			}
		} else if (view._clinic.getSelectedItem().toString().equals("전체")) {
			dtos = dao.getUser("", toDate(view.dateChooser.getDate()));

			for (UserDTO dto : dtos) {
				view.model.addRow(makeInfo(dto));
			}
		} else if (view.dateChooser.getDate() == null) {
			dtos = dao.getUser(view._clinic.getSelectedItem().toString(), "");

			for (UserDTO dto : dtos) {
				view.model.addRow(makeInfo(dto));
			}
		} else {
			dtos = dao.getUser(view._clinic.getSelectedItem().toString(), toDate(view.dateChooser.getDate()));

			for (UserDTO dto : dtos) {
				view.model.addRow(makeInfo(dto));
			}
		}

		view.mNumber.setText("현재 진료소 인원 수: " + String.valueOf(dtos.size()));
	}

	public String toDate(Date date) { // JDateChooser에 있는 값을 yyyy-mm-dd 양식의 스트링으로 변환해주는 메소드
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);

		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

		if (month.length() == 1) {
			month = "0" + month;
		}
		if (day.length() == 1) {
			day = "0" + day;
		}

		return year + "-" + month + "-" + day;
	}
}
