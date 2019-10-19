package vulc.luag.gfx.gui;

import java.util.ArrayList;
import java.util.List;

import vulc.luag.gfx.Screen;

public class GUIPanel extends GUIComponent {

	protected final List<GUIComponent> comps = new ArrayList<GUIComponent>();
	public final Screen screen;

	public GUIPanel(int x, int y, int w, int h) {
		super(x, y, w, h);
		this.screen = new Screen(w, h);
	}

	public void tick() {
		for(int i = 0; i < comps.size(); i++) {
			GUIComponent comp = comps.get(i);
			comp.tick();
		}
	}

	public void render(Screen screen) {
		this.screen.clear(background);
		drawComponents();
		screen.draw(this.screen, x, y);
	}

	protected void drawComponents() {
		for(int i = 0; i < comps.size(); i++) {
			GUIComponent comp = comps.get(i);
			comp.render(this.screen);
		}
	}

	public void add(GUIComponent comp) {
		comps.add(comp);
	}

	public void remove(GUIComponent comp) {
		comps.remove(comp);
	}

	public void onMouseDown(int xMouse, int yMouse) {
		for(int i = 0; i < comps.size(); i++) {
			GUIComponent comp = comps.get(i);

			// relative coordinates
			int xr = xMouse - comp.x;
			int yr = yMouse - comp.y;

			if(comp.isPointInside(xr, yr)) {
				boolean gainFocus = !comp.focused;
				comp.focused = true;
				if(gainFocus) comp.onGainFocus();

				comp.onMouseDown(xr, yr);
			} else {
				boolean losingFocus = comp.focused;
				comp.focused = false;
				if(losingFocus) comp.onLostFocus();
			}
		}
	}

	public void onMousePress(int xMouse, int yMouse) {
		for(int i = 0; i < comps.size(); i++) {
			GUIComponent comp = comps.get(i);

			// relative coordinates
			int xr = xMouse - comp.x;
			int yr = yMouse - comp.y;

			if(comp.isPointInside(xr, yr)) {
				comp.onMousePress(xr, yr);
			}
		}
	}

	public void onMouseRelease(int xMouse, int yMouse) {
		for(int i = 0; i < comps.size(); i++) {
			GUIComponent comp = comps.get(i);

			// relative coordinates
			int xr = xMouse - comp.x;
			int yr = yMouse - comp.y;

			if(comp.isPointInside(xr, yr)) {
				comp.onMouseRelease(xr, yr);
			}
		}
	}

	public void onMouseInside(int xMouse, int yMouse) {
		for(int i = 0; i < comps.size(); i++) {
			GUIComponent comp = comps.get(i);

			// relative coordinates
			int xr = xMouse - comp.x;
			int yr = yMouse - comp.y;

			if(comp.isPointInside(xr, yr)) {
				comp.onMouseInside(xr, yr);
			}
		}
	}

	public void onMouseScroll(int xMouse, int yMouse, int count) {
		for(int i = 0; i < comps.size(); i++) {
			GUIComponent comp = comps.get(i);

			// relative coordinates
			int xr = xMouse - comp.x;
			int yr = yMouse - comp.y;

			if(comp.isMouseScrolled(xr, yr, count)) {
				comp.onMouseScroll(xr, yr, count);
			}
		}
	}

	public void onKeyPress(char character) {
		for(int i = 0; i < comps.size(); i++) {
			GUIComponent comp = comps.get(i);
			comp.onKeyPress(character);
		}
	}

	public void onLostFocus() {
		for(int i = 0; i < comps.size(); i++) {
			GUIComponent comp = comps.get(i);
			boolean losingFocus = comp.focused;
			comp.focused = false;
			if(losingFocus) comp.onLostFocus();
		}
	}

}
