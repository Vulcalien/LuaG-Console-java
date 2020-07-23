package vulc.luag.editor.sprite.tool;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.sprite.SpriteEditor;

public class SelectTool extends SpriteTool {

	public int x0, y0, x1, y1;

	public boolean onMousePress(int x, int y, SpriteEditor editor, Bitmap<Integer> canvas) {
		x0 = x;
		y0 = y;
		return false;
	}

	public boolean onMouseDown(int x, int y, SpriteEditor editor, Bitmap<Integer> canvas) {
		x1 = x;
		y1 = y;
		return false;
	}

}
