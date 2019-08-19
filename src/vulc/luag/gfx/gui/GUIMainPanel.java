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

public class GUIMainPanel extends GUIPanel {

	private final Console console;
	public final InputHandler input = new InputHandler();

	protected final List<Character> keyBuffer = new ArrayList<Character>();
	protected final KeyAdapter keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			keyBuffer.add(e.getKeyChar());
		}
	};

	protected final Key mouse1 = input.new Key(KeyType.MOUSE, MouseEvent.BUTTON1);

	public GUIMainPanel(Console console, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.console = console;
	}

	public void init() {
		input.init(console);
		console.addKeyListener(keyListener);
	}

	public void tick() {
		super.tick();
		while(keyBuffer.size() != 0) {
			char c = keyBuffer.remove(0);
			this.onKeyPress(c);
		}
		if(mouse1.isKeyDown()) {
			int xPress = input.xMouse / Console.SCALE - this.x;
			int yPress = input.yMouse / Console.SCALE - this.y;
			if(this.isPressed(xPress, yPress)) {
				this.press(xPress, yPress);
			}
		}
		input.tick();
	}

	public void onRemove(GUIMainPanel container) {
		super.onRemove(container);
		removeInputListeners();
	}

	public void removeInputListeners() {
		input.remove();
		console.removeKeyListener(keyListener);
	}

}
