package sejong.corona;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrontController {
	FrontUI view;

	FrontController(FrontUI view) {
		this.view = view;

		view.addButtonActionListener(new ActionListener() { // 사용자 모드 혹은 관리자 모드를 선택하면 새로운 창을 열어주는 이벤트
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
