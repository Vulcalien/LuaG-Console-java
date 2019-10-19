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

	protected boolean isPointInside(int x, int y) {
		return x >= 0 && x < w && y >= 0 && y < h;
	}

	public void onMouseDown(int xMouse, int yMouse) {
	}

	public void onMousePress(int xMouse, int yMouse) {
	}

	public void onMouseRelease(int xMouse, int yMouse) {
	}

	public void onMouseInside(int xMouse, int yMouse) {
	}

	public void onGainFocus() {
	}

	public void onLostFocus() {
	}

	public void onKeyPress(char character) {
	}

	public boolean isMouseScrolled(int xMouse, int yMouse, int count) {
		return isPointInside(xMouse, yMouse);
	}

	public void onMouseScroll(int xMouse, int yMouse, int count) {
	}

	public void onRemove(GUIMainPanel container) {
	}

}
