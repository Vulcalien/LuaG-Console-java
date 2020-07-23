package vulc.luag.editor.sprite.tool;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.sprite.SpriteEditor;

public class SelectTool extends SpriteTool {

	public boolean onMousePress(int x, int y, SpriteEditor editor, Bitmap<Integer> canvas) {
		editor.selx0 = x;
		editor.sely0 = y;

		if(editor.pasteMode) editor.endPaste();
		return false;
	}

	public boolean onMouseDown(int x, int y, SpriteEditor editor, Bitmap<Integer> canvas) {
		editor.selx1 = x;
		editor.sely1 = y;

		if(editor.pasteMode) editor.endPaste();
		return false;
	}

}
