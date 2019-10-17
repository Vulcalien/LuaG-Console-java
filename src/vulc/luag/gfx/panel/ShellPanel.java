package vulc.luag.gfx.panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import vulc.luag.Console;
import vulc.luag.input.InputHandler;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;
import vulc.luag.shell.ShellChar;
import vulc.luag.shell.Shell;

public class ShellPanel extends Panel {

	private final Shell shell;

	private final InputHandler input = new InputHandler();
	public final Key ctrl = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_CONTROL);

	private final KeyAdapter keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			shell.charBuffer.add(new ShellChar(e.getKeyChar(), true));
		}
	};
	private final MouseWheelListener mouseScrollListener = new MouseWheelListener() {
		public void mouseWheelMoved(MouseWheelEvent e) {
			shell.scrollBuffer += e.getWheelRotation();
		}
	};

	public ShellPanel() {
		this.shell = Console.shell;
		shell.panel = this;
	}

	public void onShow() {
		Console console = Console.instance;

		input.init();
		console.addKeyListener(keyListener);
		console.addMouseWheelListener(mouseScrollListener);
	}

	public void tick() {
		input.tick();
		shell.tick();
	}

	public void remove() {
		Console console = Console.instance;

		input.remove();
		console.removeKeyListener(keyListener);
		console.removeMouseWheelListener(mouseScrollListener);
	}

}
