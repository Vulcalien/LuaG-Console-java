package vulc.jlconsole.gfx.gui;

import vulc.jlconsole.gfx.Screen;

public class GUIComponent {

	public int x, y;
	public int w, h;
	public boolean focused = false;
	public boolean opaque = false;
	public int background = 0xDDDDDD;

	public GUIComponent(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void render(Screen screen, int xOff, int yOff) {
		if(opaque) screen.fill(xOff + x,
		                       yOff + y,
		                       xOff + x + w - 2,
		                       yOff + y + h - 2,
		                       background);
	}

	public boolean isPressed(int xm, int ym) {
		return xm >= x && ym >= y && xm < w && ym < h;
	}

	public void press() {
	}

	public void onKeyPress(char character) {
	}

}
