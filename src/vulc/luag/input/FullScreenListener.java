package vulc.luag.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import vulc.luag.Console;

public class FullScreenListener implements KeyListener {

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_F11) {
			Console.frame.setVisible(false);

			if(Console.isFullScreen) {
				Console.initFrame(false);
				Console.setInitialScale();
				Console.frame.setLocationRelativeTo(null);
			} else {
				Console.initFrame(true);
			}
			Console.frame.setVisible(true);
			Console.instance.requestFocus();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

}
