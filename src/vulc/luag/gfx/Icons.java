package vulc.luag.gfx;

import javax.imageio.ImageIO;

import vulc.bitmap.Bitmap;
import vulc.bitmap.BoolBitmap;

public abstract class Icons {

	// GENERAL
	public static final Bitmap<Boolean> SHELL = loadIcon("/res/icons/shell.png");
	public static final Bitmap<Boolean> SAVE = loadIcon("/res/icons/save.png");
	public static final Bitmap<Boolean> UNDO = loadIcon("/res/icons/undo.png");
	public static final Bitmap<Boolean> REDO = loadIcon("/res/icons/redo.png");

	// EDITORS
	public static final Bitmap<Boolean> SPRITE_EDITOR = loadIcon("/res/icons/editor/sprite_editor.png");
	public static final Bitmap<Boolean> MAP_EDITOR = loadIcon("/res/icons/editor/map_editor.png");

	// TOOLS
	public static final Bitmap<Boolean> PENCIL_TOOL = loadIcon("/res/icons/editor/tool/pencil.png");
	public static final Bitmap<Boolean> BUCKET_TOOL = loadIcon("/res/icons/editor/tool/bucket.png");
	public static final Bitmap<Boolean> PICKUP_TOOL = loadIcon("/res/icons/editor/tool/pickup.png");

	private static Bitmap<Boolean> loadIcon(String icon) {
		try {
			return new BoolBitmap(ImageIO.read(Icons.class.getResourceAsStream(icon)), 0x000000);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

}
