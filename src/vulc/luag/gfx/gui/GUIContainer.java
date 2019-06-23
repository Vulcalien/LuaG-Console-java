package vulc.luag.gfx.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import vulc.luag.Console;
import vulc.luag.gfx.Screen;
import vulc.luag.input.InputHandler;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;

public class GUIContainer extends GUIComponent {

	protected final List<GUIComponent> comps = new ArrayList<GUIComponent>();

	private final Console console;
	public final InputHandler input = new InputHandler();
	public final Screen screen;
	public int xParentAbs = 0, yParentAbs = 0;

	protected final List<Character> keyBuffer = new ArrayList<Character>();
	protected final KeyAdapter keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			keyBuffer.add(e.getKeyChar());
		}
	};

	protected final Key mouse1 = input.new Key(KeyType.MOUSE, MouseEvent.BUTTON1);

	public GUIContainer(Console console, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.console = console;
		this.screen = new Screen(w, h);
		input.init(console);
		console.addKeyListener(keyListener);
	}

	public void tick() {
		for(GUIComponent comp : comps) {
			comp.tick();
		}
		while(keyBuffer.size() != 0) {
			char c = keyBuffer.remove(0);
			for(GUIComponent comp : comps) {
				comp.onKeyPress(c);
			}
		}
		if(mouse1.isKeyDown()) {
			if(this.isPressed(input.xMouse / Console.SCALE - xParentAbs,
			                  input.yMouse / Console.SCALE - yParentAbs)) {
				this.press();
			}
		}
		input.tick();
	}

	@Override
	public void render(Screen screen) {
		this.screen.clear(background);
		for(GUIComponent comp : comps) {
			comp.render(this.screen);
		}
		screen.draw(this.screen, x, y);
	}

	public void add(GUIComponent comp) {
		comps.add(comp);
		if(comp instanceof GUIContainer) {
			GUIContainer container = (GUIContainer) comp;
			container.xParentAbs = this.xParentAbs + x;
			container.yParentAbs = this.yParentAbs + y;
		}
	}

	public void remove(GUIComponent comp) {
		comps.remove(comp);
	}

	@Override
	public void onRemove(GUIContainer container) {
		super.onRemove(container);
		removeInputListeners();
	}

	public void removeInputListeners() {
		input.remove();
		console.removeKeyListener(keyListener);
	}

	@Override
	public void press() {
		for(GUIComponent comp : comps) {
			if(comp.isPressed(input.xMouse / Console.SCALE - xParentAbs - x,
			                  input.yMouse / Console.SCALE - yParentAbs - y)) {
				boolean gainFocus = !comp.focused;
				comp.focused = true;
				if(gainFocus) comp.onGainFocus();

				comp.press();
			} else {
				boolean lostFocus = comp.focused;
				comp.focused = false;
				if(lostFocus) comp.onLostFocus();
			}
		}
	}

}
