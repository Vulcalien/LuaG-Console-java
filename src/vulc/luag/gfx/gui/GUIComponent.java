package vulc.luag.gfx.gui;

import vulc.luag.gfx.Screen;

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

	public void tick() {
	}

	public void render(Screen screen) {
		if(opaque) screen.fill(x,
		                       y,
		                       x + w - 1,
		                       y + h - 1,
		                       background);
	}

	public boolean isPressed(int x, int y) {
		return x >= 0 && x < w && y >= 0 && y < h;
	}

	public void press(int x, int y) {
	}

	public void onGainFocus() {
	}

	public void onLostFocus() {
	}

	public void onKeyPress(char character) {
	}

	public void onRemove(GUIMainPanel container) {
	}

}
