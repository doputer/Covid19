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

				if (obj == view.okBtn) {
					int id = Integer.parseInt(m_view.dataTbl.getValueAt(view.row, 0).toString());
					String result = view.diagnosisTa.getText();
					String hospital = view.hospitalCb.getSelectedItem().toString();
					String date = m_view.controll.toDate(view.dateChooser.getDate());

					m_view.dao.updateUser(id, result, hospital, date);
					m_view.controll.refresh();
					view.dispose();
				} else if (obj == view.cancleBtn) {
					view.dispose();
				}
			}
		});
	}
}
