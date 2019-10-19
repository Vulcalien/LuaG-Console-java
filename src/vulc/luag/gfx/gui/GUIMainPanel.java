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

	public GUIMainPanel(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	public void init() {
		Console console = Console.instance;

		input.init();
		console.addKeyListener(keyListener);
		console.addMouseWheelListener(wheelListener);
	}

	public void tick() {
		super.tick();

		input.tick();

		int xMouse = input.xMouse / Console.SCALE - this.x;
		int yMouse = input.yMouse / Console.SCALE - this.y;

		while(keyBuffer.size() != 0) {
			char c = keyBuffer.remove(0);
			this.onKeyPress(c);
		}

		if(mouse1.isKeyDown()) {
			if(this.isPointInside(xMouse, yMouse)) {
				this.onMouseDown(xMouse, yMouse);
			}
		}
		// TODO when onMousePress and onMouseRelease will be added
//		if(mouse1.isPressed()) {
//			if(this.isPointInside(xMouse, yMouse)) {
//				this.onMousePress(xMouse, yMouse);
//			}
//		}
//		if(mouse1.isReleased()) {
//			if(this.isPointInside(xMouse, yMouse)) {
//				this.onMouseRelease(xMouse, yMouse);
//			}
//		}

		this.onMouseInside(xMouse, yMouse);

		if(wheelRotCount != 0) {
			if(this.isMouseScrolled(xMouse, yMouse, wheelRotCount)) {
				this.onMouseScroll(xMouse, yMouse, wheelRotCount);
			}
			wheelRotCount = 0;
		}
	}

	public void onRemove(GUIMainPanel container) {
		super.onRemove(container);
		removeInputListeners();
	}

	public void removeInputListeners() {
		Console console = Console.instance;

		input.remove();
		console.removeKeyListener(keyListener);
		console.removeMouseWheelListener(wheelListener);
	}

}
