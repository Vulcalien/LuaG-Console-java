package vulc.luag.editor.sprite.tool;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.sprite.SpriteEditor;

public class SelectTool extends SpriteTool {

	private int x0, y0, x1, y1;
	private boolean selecting = false;

	public boolean onMousePress(int x, int y, SpriteEditor editor, Bitmap<Integer> canvas) {
		x0 = x;
		y0 = y;
		return false;
	}

	public boolean onMouseRelease(int x, int y, SpriteEditor editor, Bitmap<Integer> canvas) {
		x1 = x;
		y1 = y;
		return false;
	}

}
