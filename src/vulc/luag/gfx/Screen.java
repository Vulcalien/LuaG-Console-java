package vulc.luag.gfx;

import vulc.bitmap.IntBitmap;
import vulc.bitmap.font.Font;

public class Screen extends IntBitmap {

	public static final Font FONT;
	static {
		FONT = new Font(Screen.class.getResourceAsStream("/res/font.fv3"));

		FONT.setLetterSpacing(1);
		FONT.setLineSpacing(1);
	}

	public Screen(int width, int height) {
		super(width, height);
		setFont(FONT);
	}

}
