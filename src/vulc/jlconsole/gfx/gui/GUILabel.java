package vulc.jlconsole.gfx.gui;

import vulc.jlconsole.gfx.Screen;

public class GUILabel extends GUIComponent {

	public String text = "";
	public int textColor = 0;

	public GUILabel(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	@Override
	public void render(Screen screen) {
		super.render(screen);
		screen.write(text, textColor, x + 1, y + (h - Screen.FONT.getHeight()) / 2);
	}

}
