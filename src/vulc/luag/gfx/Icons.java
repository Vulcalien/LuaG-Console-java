package vulc.luag.gfx;

import javax.imageio.ImageIO;

import vulc.bitmap.BoolBitmap;

public abstract class Icons {

	public static final BoolBitmap CMD = loadIcon("/res/icons/cmd.png");
	public static final BoolBitmap SAVE = loadIcon("/res/icons/save.png");

	public static final BoolBitmap SPRITE_EDITOR = loadIcon("/res/icons/sprite_editor.png");
	public static final BoolBitmap MAP_EDITOR = loadIcon("/res/icons/map_editor.png");

	private static BoolBitmap loadIcon(String icon) {
		try {
			return new BoolBitmap(ImageIO.read(Icons.class.getResourceAsStream(icon)));
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
