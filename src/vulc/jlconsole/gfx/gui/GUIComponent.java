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

	public void render(Screen screen) {
		if(opaque) screen.fill(x,
		                       y,
		                       x + w - 1,
		                       y + h - 1,
		                       background);
	}

	public boolean isPressed(int xm, int ym) {
		return xm >= x && ym >= y && xm < x + w && ym < y + h;
	}

	public void press() {
	}

	public void onGainFocus() {
	}

	public void onLostFocus() {
	}

	public void onKeyPress(char character) {
	}

	public void onRemove(GUIContainer container) {
	}

}
