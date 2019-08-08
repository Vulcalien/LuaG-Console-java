package vulc.luag.gfx.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import vulc.luag.Console;
import vulc.luag.input.InputHandler;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;

public class GUIMainContainer extends GUIPanel {

	private final Console console;
	public final InputHandler input = new InputHandler();

	protected final List<Character> keyBuffer = new ArrayList<Character>();
	protected final KeyAdapter keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			keyBuffer.add(e.getKeyChar());
		}
	};

	protected final Key mouse1 = input.new Key(KeyType.MOUSE, MouseEvent.BUTTON1);

	public GUIMainContainer(Console console, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.console = console;
	}

	public void init() {
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
			int xPress = input.xMouse / Console.SCALE - xInputOffset;
			int yPress = input.yMouse / Console.SCALE - yInputOffset;
			if(this.isPressed(xPress, yPress)) {
				this.press(xPress, yPress);
			}
		}
		input.tick();
	}

	public void onRemove(GUIMainContainer container) {
		super.onRemove(container);
		removeInputListeners();
	}

	public void removeInputListeners() {
		input.remove();
		console.removeKeyListener(keyListener);
	}

}
