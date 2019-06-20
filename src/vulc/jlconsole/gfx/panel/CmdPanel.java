package vulc.jlconsole.gfx.panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import vulc.jlconsole.Console;
import vulc.jlconsole.cmd.Cmd;
import vulc.jlconsole.cmd.CmdChar;

public class CmdPanel extends Panel {

	private final Cmd cmd;

	private final KeyAdapter listener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			cmd.charBuffer.add(new CmdChar(e.getKeyChar(), true));
		}
	};

	public CmdPanel(Console console) {
		super(console);
		this.cmd = console.cmd;
		cmd.cmdPanel = this;
	}

	public void init() {
		console.addKeyListener(listener);
	}

	public void tick() {
		cmd.tick();
	}

	public void remove() {
		console.removeKeyListener(listener);
	}

}
