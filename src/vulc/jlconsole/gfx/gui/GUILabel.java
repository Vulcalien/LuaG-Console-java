package vulc.jlconsole.gfx.gui;

import vulc.jlconsole.gfx.Screen;

public class GUILabel extends GUIComponent {

	public String text = "";
	public int textColor = 0;

	public GUILabel(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	public void render(Screen screen, int xOff, int yOff) {
		super.render(screen, xOff, yOff);
		screen.write(text, textColor, xOff + x + 1, yOff + y + (h - Screen.FONT.getHeight()) / 2);
	}

}
