package vulc.luag.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import vulc.luag.Console;

public class FullScreenListener implements KeyListener {

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_F11) {
			Console.switchFullScreen();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

}
