package vulc.luag.gfx;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import vulc.luag.Console;

public class ConsoleFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private int wOld = -1, hOld = -1;

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
				pack();
			}
		});

		Toolkit.getDefaultToolkit().setDynamicLayout(false);
	}

	public void checkSize() {
		Dimension d = getSize();
		Insets insets = getInsets();
		int w = d.width - insets.left - insets.right;
		int h = d.height - insets.top - insets.bottom;

		if(w != wOld || h != hOld) {
			int min = Math.min(w, h);

			Console.updateScaledSize(min, min);

			wOld = w;
			hOld = h;
		}
	}

}
