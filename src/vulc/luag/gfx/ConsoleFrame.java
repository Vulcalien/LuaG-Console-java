package vulc.luag.gfx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.imageio.ImageIO;

import vulc.luag.Console;

// BUG try to do WIN+down when in full screen
public class ConsoleFrame extends Frame {

	private static final long serialVersionUID = 1L;

	private int wOld = -1, hOld = -1;

	public void init() {
		setTitle(Console.NAME + " " + Console.VERSION);
		setBackground(Color.DARK_GRAY);

		try {
			setIconImage(ImageIO.read(Console.class.getResourceAsStream("/res/icon.png")));
		} catch(IOException e) {
			e.printStackTrace();
		}

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Console.stop();
				System.exit(0);
			}

			public void windowIconified(WindowEvent e) {
				Console.stop();
			}

			public void windowDeiconified(WindowEvent e) {
				Console.start();
			}
		});
	}

	public void checkSize() {
		Dimension d = getSize();
		Insets insets = getInsets();
		int wFrame = d.width - insets.left - insets.right;
		int hFrame = d.height - insets.top - insets.bottom;

		if(wFrame != wOld || hFrame != hOld) {
			Console.updateScaledSize(wFrame, hFrame);

			wOld = wFrame;
			hOld = hFrame;
		}
	}

}
