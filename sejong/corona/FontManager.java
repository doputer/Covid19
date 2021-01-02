package sejong.corona;

import java.awt.*;

public class FontManager {
	public Font font = new Font("맑은 고딕", Font.PLAIN, 13);

	FontManager() {
	}

	FontManager(Component[] comp) {
		setDefaultFont(comp);
	}
	
	FontManager(Component[] comp, Font font) {
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
