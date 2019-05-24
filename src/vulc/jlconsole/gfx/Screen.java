package vulc.jlconsole.gfx;

import vulc.bitmap.Bitmap;
import vulc.bitmap.Font;

public class Screen extends Bitmap {

	public final Font font;

	public Screen(int width, int height) {
		super(width, height);
		font = new Font(Screen.class.getResourceAsStream("/res/font.lwfont"));
		font.setLetterSpacing(1);
	}

	public void write(String text, int color, int x, int y) {
		font.write(text, color, this, x, y);
	}

}
