package sejong.corona;

import java.util.ArrayList;

public class ManagerController {
	ManagerView view;
	ManagerDAO dao;

	ManagerController(ManagerView view, ManagerDAO dao) {
		this.view = view;
		this.dao = dao;
	}
	
	private String makeInfo(UserDTO dto) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(dto.getId());
		sb.append("   " + dto.getName());
		sb.append("   " + dto.getPhone());
		sb.append("   " + dto.getAddress());
		sb.append("   " + dto.getBirth());
		sb.append("   " + dto.getGender());
		sb.append("   " + dto.getSymptom1());
		sb.append("   " + dto.getSymptom2());
		sb.append("   " + dto.getSymptom3());
		sb.append("   " + dto.getSymptom4());
		sb.append("   " + dto.getEtc());
		sb.append("   " + dto.getHospital());
		sb.append("   " + dto.getDate());
		sb.append("   " + dto.getStatus());
		
		return sb.toString();
	}

	public void refreshAll() { // 나중에 합쳐도 될 듯
		view.listVct.clear();
		ArrayList<UserDTO> dtos = new ArrayList<UserDTO>();
		dtos = dao.getAllUser();

		for (UserDTO dto : dtos) {
			view.listVct.addElement(makeInfo(dto));
		}
	}

	public void refresh() {
		view.listVct.clear();
		ArrayList<UserDTO> dtos = new ArrayList<UserDTO>();
		dtos = dao.getUser(view._clinic.getSelectedItem().toString());

		for (UserDTO dto : dtos) {
			view.listVct.addElement(makeInfo(dto));
		}
	}
}
