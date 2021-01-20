package sejong.corona;

import java.awt.*;

public class FontManager {
	public Font font = new Font("맑은 고딕", Font.PLAIN, 13);

	FontManager() {
	}

	FontManager(Component[] comp) { // 기본 폰트인 맑은 고딕으로 컴포넌트의 글꼴 지정
		setDefaultFont(comp);
	}

	FontManager(Component[] comp, Font font) { // 매개변수로 받는 폰트로 컴포넌트의 글꼴 지정
		this.font = font;
		setDefaultFont(comp);
	}

	public void setDefaultFont(Component[] comp) {
		for (int x = 0; x < comp.length; x++) {
			if (comp[x] instanceof Container)
				setDefaultFont(((Container) comp[x]).getComponents());
			try {
				comp[x].setFont(font);
			} catch (Exception e) {
			}
		}
	}
}
