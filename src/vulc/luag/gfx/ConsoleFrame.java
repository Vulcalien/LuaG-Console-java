package vulc.luag.gfx;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import vulc.luag.Console;

public class ConsoleFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(Console.NAME + " " + Console.VERSION);

		try {
			setIconImage(ImageIO.read(Console.class.getResourceAsStream("/res/icon.png")));
		} catch(IOException e) {
			e.printStackTrace();
		}

		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				Console.updateRealSize();
			}
		});
	}

}
