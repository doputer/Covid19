package sejong.corona;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class FrontUI extends JFrame {
	JButton userBtn, managerBtn;
	JPanel btn;
	JLabel logo;
	Image img;

	static TriageRoomAPI triageRoomModel;
	ManagerUI managerView;

	UserUI userUI;

	FrontUI() {
		setTitle("코로나 선별 진료소 예약 / 관리 시스템");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		startUI();

		try {
			triageRoomModel = new TriageRoomAPI();
		} catch (IOException e) {
			e.printStackTrace();
		}

		new FontManager(this.getComponents());

		setSize(640, 440);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void startUI() {
		userBtn = new JButton("예약자");
		userBtn.setBounds(160, 200, 120, 50);
		userBtn.setBackground(new Color(230, 250, 250));
		managerBtn = new JButton("관리자");
		managerBtn.setBounds(350, 200, 120, 50);
		managerBtn.setBackground(new Color(250, 250, 230));

		img = new ImageIcon(FrontUI.class.getResource("/sejong/corona/background.png")).getImage();
		logo = new JLabel(new ImageIcon(img));

		userBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == userBtn) {
					if (userUI == null) {
						userUI = new UserUI();
					}
					userUI.setVisible(true);
				}
			}
		});

		managerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == managerBtn) {
					if (managerView == null) {
						managerView = new ManagerUI();
					}
					managerView.setVisible(true);
				}
			}
		});

		add(userBtn);
		add(managerBtn);
		add(logo, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		new FrontUI();
	}
}
