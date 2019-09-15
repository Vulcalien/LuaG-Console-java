package vulc.luag.editor.sprite.tool;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.sprite.SpriteEditor;

public class PickupTool extends SpriteTool {

	public boolean onEdit(int x, int y, SpriteEditor editor, Bitmap canvas) {
		editor.selectColor(canvas.getPixel(x, y));
		return false;
	}

}
