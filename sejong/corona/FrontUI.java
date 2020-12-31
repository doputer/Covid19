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
	ManagerView managerView;
	
	FrontUI() {
		setTitle("�ڷγ� ���� ����� ���� / ���� �ý���");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		startUI();
		
		try {
			triageRoomModel = new TriageRoomAPI();
		} catch (IOException e) {
			e.printStackTrace();
		}

		setSize(640, 440);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void startUI() {
		userBtn = new JButton("������");
		userBtn.setBounds(160, 200, 120, 50);
		managerBtn = new JButton("������");
		managerBtn.setBounds(360, 200, 120, 50);

		img = new ImageIcon(FrontUI.class.getResource("/sejong/corona/background.png")).getImage();
//		img.getScaledInstance(1, 1, Image.SCALE_SMOOTH);
		logo = new JLabel(new ImageIcon(img));
//		logo.setBounds(0, 0, 640, 390);
		
		managerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        if (e.getSource() == managerBtn) {
		            if (managerView == null) {
		            	managerView = new ManagerView();
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
