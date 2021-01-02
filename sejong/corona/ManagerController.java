package sejong.corona;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ManagerController {
	ManagerView view;
	ManagerDAO dao;

	ManagerController(ManagerView view, ManagerDAO dao) {
		this.view = view;
		this.dao = dao;
	}

	private String makeInfo(UserDTO dto) {
		StringBuilder sb = new StringBuilder();

		sb.append(dto.getId() + "    ");
		sb.append(dto.getName() + "    ");
		sb.append(dto.getPhone() + "    ");
		sb.append(dto.getAddress() + "    ");
		sb.append(dto.getBirth() + "    ");
		sb.append(dto.getGender() + "    ");
		sb.append(dto.getSymptom1() + "    ");
		sb.append(dto.getSymptom2() + "    ");
		sb.append(dto.getSymptom3() + "    ");
		sb.append(dto.getSymptom4() + "    ");
		sb.append(dto.getEtc() + "    ");
		sb.append(dto.getHospital() + "    ");
		sb.append(dto.getDate() + "    ");
		sb.append(dto.getStatus());
		
		return sb.toString();
	}
	
	private String makeDefualtInfO() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("아이디    ");
		sb.append("이름    ");
		sb.append("연락처    ");
		sb.append("주소    ");
		sb.append("생년월일    ");
		sb.append("성별    ");
		sb.append("증상1    ");
		sb.append("증상2    ");
		sb.append("증상3    ");
		sb.append("증상4    ");
		sb.append("기타    ");
		sb.append("선별진료소    ");
		sb.append("예약일자    ");
		sb.append("예약상태    ");
		
		return sb.toString();
	}

	public void refresh() {
		view.listVct.clear();
//		view.listVct.addElement(makeDefualtInfO());
		ArrayList<UserDTO> dtos = new ArrayList<UserDTO>();

		if (view._clinic.getSelectedItem().toString().equals("전체") && view.dateChooser.getDate() == null) {
			dtos = dao.getAllUser();

			for (UserDTO dto : dtos) {
				view.listVct.addElement(makeInfo(dto));
			}
		}
		else if (view._clinic.getSelectedItem().toString().equals("전체")) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(view.dateChooser.getDate());
			
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
			String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			
			if (month.length() == 1) {
				month = "0" + month;
			}
			if (day.length() == 1) {
				day = "0" + day;
			}
			
			dtos = dao.getUser("", year + "-" + month + "-" + day);
			
			for (UserDTO dto : dtos) {
				view.listVct.addElement(makeInfo(dto));
			}
		} else if (view.dateChooser.getDate() == null) {
			dtos = dao.getUser(view._clinic.getSelectedItem().toString(), "");

			for (UserDTO dto : dtos) {
				view.listVct.addElement(makeInfo(dto));
			}
		}
		else {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(view.dateChooser.getDate());
			
			String year = String.valueOf(calendar.get(Calendar.YEAR));
			String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
			String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
			
			if (month.length() == 1) {
				month = "0" + month;
			}
			if (day.length() == 1) {
				day = "0" + day;
			}
			
			dtos = dao.getUser(view._clinic.getSelectedItem().toString(), year + "-" + month + "-" + day);

			for (UserDTO dto : dtos) {
				view.listVct.addElement(makeInfo(dto));
			}
		}
	}
}
