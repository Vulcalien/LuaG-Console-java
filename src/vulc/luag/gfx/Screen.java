package vulc.luag.gfx;

import vulc.bitmap.Bitmap;
import vulc.bitmap.Font;

public class Screen extends Bitmap {

	public static final Font FONT;
	static {
		FONT = new Font(Screen.class.getResourceAsStream("/res/font.lwfont"));
		FONT.setLetterSpacing(1);
	}

	public Screen(int width, int height) {
		super(width, height);
	}

	public void write(String text, int color, int x, int y) {
		FONT.write(text, color, this, x, y);
	}

}
