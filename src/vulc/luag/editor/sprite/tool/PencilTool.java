package vulc.luag.editor.sprite.tool;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.sprite.SpriteEditor;

public class PencilTool extends SpriteTool {

	public boolean onEdit(int x, int y, SpriteEditor editor, Bitmap<Integer> canvas) {
		int color = editor.selectedColor;
		if(canvas.getPixel(x, y) != color) {
			canvas.setPixel(x, y, color);
			return true;
		}
		return false;
	}

}
