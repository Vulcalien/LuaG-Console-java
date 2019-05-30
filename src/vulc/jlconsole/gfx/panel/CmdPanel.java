package vulc.jlconsole.gfx.panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import vulc.jlconsole.Console;
import vulc.jlconsole.cmd.Cmd;

public class CmdPanel extends Panel {

	private final Cmd cmd;

	private final KeyAdapter listener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			cmd.charBuffer.add(e.getKeyChar());
		}
	};

	public CmdPanel(Console console) {
		super(console);
		this.cmd = new Cmd(console);
	}

	public void init() {
		console.addKeyListener(listener);
	}

	public void tick() {
		cmd.tick();
	}

	protected void finalize() throws Throwable {
		console.removeKeyListener(listener);
	}

}
