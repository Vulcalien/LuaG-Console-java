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
		for(GUIComponent comp : comps) {
			comp.tick();
		}
	}

	public void render(Screen screen) {
		this.screen.clear(background);
		for(GUIComponent comp : comps) {
			comp.render(this.screen);
		}
		screen.draw(this.screen, x, y);
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
				boolean lostFocus = comp.focused;
				comp.focused = false;
				if(lostFocus) comp.onLostFocus();
			}
		}
	}

	public void onKeyPress(char character) {
		for(GUIComponent comp : comps) {
			comp.onKeyPress(character);
		}
	}

}
