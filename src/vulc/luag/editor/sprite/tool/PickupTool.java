package vulc.luag.editor.sprite.tool;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.sprite.SpriteEditor;

public class PickupTool extends SpriteTool {

	public boolean onEdit(int x, int y, SpriteEditor editor, Bitmap<Integer> canvas) {
		editor.selectColor(canvas.getPixel(x, y));

		SpriteToolkit toolkit = editor.toolkit;
		toolkit.setTool(toolkit.pencil);

		return false;
	}

}
