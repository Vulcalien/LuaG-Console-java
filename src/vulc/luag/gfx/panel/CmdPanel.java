package vulc.luag.gfx.panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import vulc.luag.Console;
import vulc.luag.cmd.Cmd;
import vulc.luag.cmd.CmdChar;
import vulc.luag.input.InputHandler;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;

public class CmdPanel extends Panel {

	private final Cmd cmd;

	private final InputHandler input = new InputHandler();
	public final Key ctrl = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_CONTROL);

	private final KeyAdapter keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			cmd.charBuffer.add(new CmdChar(e.getKeyChar(), true));
		}
	};
	private final MouseWheelListener mouseScrollListener = new MouseWheelListener() {
		public void mouseWheelMoved(MouseWheelEvent e) {
			cmd.scrollBuffer += e.getWheelRotation();
		}
	};

	public CmdPanel() {
		this.cmd = Console.cmd;
		cmd.cmdPanel = this;
	}

	public void onShow() {
		Console console = Console.instance;

		input.init();
		console.addKeyListener(keyListener);
		console.addMouseWheelListener(mouseScrollListener);
	}

	public void tick() {
		input.tick();
		cmd.tick();
	}

	public void remove() {
		Console console = Console.instance;

		input.remove();
		console.removeKeyListener(keyListener);
		console.removeMouseWheelListener(mouseScrollListener);
	}

}
