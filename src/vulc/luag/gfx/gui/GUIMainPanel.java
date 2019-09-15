package vulc.luag.gfx.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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

	protected int wheelRotCount = 0;
	protected final MouseWheelListener wheelListener = new MouseWheelListener() {
		public void mouseWheelMoved(MouseWheelEvent e) {
			wheelRotCount += e.getWheelRotation();
		}
	};

	public GUIMainPanel(Console console, int x, int y, int w, int h) {
		super(x, y, w, h);
		this.console = console;
	}

	public void init() {
		input.init(console);
		console.addKeyListener(keyListener);
		console.addMouseWheelListener(wheelListener);
	}

	public void tick() {
		super.tick();

		int xMouse = input.xMouse / Console.SCALE - this.x;
		int yMouse = input.yMouse / Console.SCALE - this.y;

		while(keyBuffer.size() != 0) {
			char c = keyBuffer.remove(0);
			this.onKeyPress(c);
		}

		if(mouse1.isKeyDown()) {
			if(this.isPressed(xMouse, yMouse)) {
				this.onPress(xMouse, yMouse);
			}
		}

		if(wheelRotCount != 0) {
			if(this.isMouseScrolled(xMouse, yMouse, wheelRotCount)) {
				this.onMouseScroll(xMouse, yMouse, wheelRotCount);
			}
			wheelRotCount = 0;
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
		console.removeMouseWheelListener(wheelListener);
	}

}
