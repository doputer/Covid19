package sejong.corona;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class ManagerController {
	ManagerView view;
	ManagerDAO dao;

	ManagerController(ManagerView view, ManagerDAO dao) {
		this.view = view;
		this.dao = dao;
	}

	private Vector<String> makeInfo(UserDTO dto) {
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

		return row;
	}

	public void refresh() {
		view.model.setRowCount(0);
		ArrayList<UserDTO> dtos = new ArrayList<UserDTO>();

		if (view._clinic.getSelectedItem().toString().equals("전체") && view.dateChooser.getDate() == null) {
			dtos = dao.getAllUser();

			for (UserDTO dto : dtos) {
				view.model.addRow(makeInfo(dto));
				view.cnt++;
			}
		} else if (view._clinic.getSelectedItem().toString().equals("전체")) {
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
				view.model.addRow(makeInfo(dto));
				view.cnt++;
			}
		} else if (view.dateChooser.getDate() == null) {
			dtos = dao.getUser(view._clinic.getSelectedItem().toString(), "");

			for (UserDTO dto : dtos) {
				view.model.addRow(makeInfo(dto));
				view.cnt++;
			}
		} else {
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
				view.model.addRow(makeInfo(dto));
				view.cnt++;
			}
		}
	}
}
