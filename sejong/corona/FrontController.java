package sejong.corona;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrontController {
	FrontUI view;

	FrontController(FrontUI view) {
		this.view = view;

		view.addButtonActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object obj = e.getSource();

				if (obj == view.userBtn) {
					view.userUI = new UserUI(view);
					view.userUI.setVisible(true);
				} else if (obj == view.managerBtn) {
					view.managerView = new ManagerUI(view);
					view.managerView.setVisible(true);
				}
			}
		});
	}
}
