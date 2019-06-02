package vulc.jlconsole.gfx.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import vulc.jlconsole.Console;
import vulc.jlconsole.gfx.Screen;
import vulc.jlconsole.input.InputHandler;
import vulc.jlconsole.input.InputHandler.Key;
import vulc.jlconsole.input.InputHandler.KeyType;

public class GUIContainer extends GUIComponent {

	protected final List<GUIComponent> comps = new ArrayList<GUIComponent>();
	protected final InputHandler input = new InputHandler();

	protected final KeyAdapter keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			for(GUIComponent comp : comps) {
				comp.onKeyPress(e.getKeyChar());
			}
		}
	};

	protected final Key mouse1 = input.new Key(KeyType.MOUSE, MouseEvent.BUTTON1);

	public GUIContainer(Console console, int x, int y, int w, int h) {
		super(x, y, w, h);
		input.init(console);
	}

	public void tick() {
		for(GUIComponent comp : comps) {
			//TODO change to absolute input coordinates
			if(comp.isPressed(input.xMouse / Console.SCALE, input.yMouse / Console.SCALE)) {
				comp.focused = true;
				comp.press();
			}
		}
		input.tick();
	}

	public void render(Screen screen, int xOff, int yOff) {
		for(GUIComponent comp : comps) {
			comp.render(screen, x + xOff, y + yOff);
		}
	}

	public void add(GUIComponent comp) {
		comps.add(comp);
	}

	public void remove(GUIComponent comp) {
		comps.remove(comp);
	}

}
