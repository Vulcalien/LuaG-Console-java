package vulc.luag.gfx.gui;

import java.util.ArrayList;
import java.util.List;

import vulc.luag.gfx.Screen;

public class GUIPanel extends GUIComponent {

	protected final List<GUIComponent> comps = new ArrayList<GUIComponent>();
	public int xInputOffset = 0, yInputOffset = 0;
	public final Screen screen;

	public GUIPanel(int x, int y, int w, int h) {
		super(x, y, w, h);
		this.screen = new Screen(w, h);
		this.xInputOffset = x;
		this.yInputOffset = y;
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

	public void press(int x, int y) {
		for(int i = 0; i < comps.size(); i++) {
			GUIComponent comp = comps.get(i);

			// relative coordinates
			int xr = x - xInputOffset;
			int yr = y - yInputOffset;

			if(comp.isPressed(xr, yr)) {
				boolean gainFocus = !comp.focused;
				comp.focused = true;
				if(gainFocus) comp.onGainFocus();

				comp.press(xr, yr);
			} else {
				boolean losingFocus = comp.focused;
				comp.focused = false;
				if(losingFocus) comp.onLostFocus();
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
