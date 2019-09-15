package vulc.luag.editor.sprite.tool;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.sprite.SpriteEditor;

public abstract class SpriteTool {

	// returns true if is editing
	public abstract boolean onEdit(int x, int y, SpriteEditor editor, Bitmap canvas);
}
