package sejong.corona;

import java.awt.event.*;

public class DiagnosisController {
	DiagnosisUI view;
	ManagerUI m_view;

	DiagnosisController(DiagnosisUI view, ManagerUI m_view) {
		this.view = view;
		this.m_view = m_view;

		view.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				view.dispose();
			}
		});

		view.addButtonActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();

				if (obj == view.okBtn) { // 진단하기에서 확인 버튼을 누르면 진단 결과, 선택 선별 진료소, 선택 일자를 데이터베이스에 갱신하고 관리자 뷰를 새로고침
					int id = Integer.parseInt(m_view.dataTbl.getValueAt(view.row, 0).toString());
					String result = view.diagnosisTa.getText();
					String hospital = view.hospitalCb.getSelectedItem().toString();
					String date = m_view.controll.toDate(view.dateChooser.getDate());

					m_view.dao.updateUser(id, result, hospital, date);
					m_view.controll.refresh();
					view.dispose();
				} else if (obj == view.cancleBtn) { // 취소 버튼을 누르면 창 종료
					view.dispose();
				}
			}
		});
	}
}
