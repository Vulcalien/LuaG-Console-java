package vulc.luag.editor.sprite.tool;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.sprite.SpriteEditor;

public class BucketTool extends SpriteTool {

	public boolean onMouseDown(int x, int y, SpriteEditor editor, Bitmap<Integer> canvas) {
		boolean[][] checked = new boolean[canvas.width][canvas.height];

		int newColor = editor.selectedColor;
		int backgroundColor = canvas.getPixel(x, y);

		if(newColor == backgroundColor) return false;

		fill(x, y, canvas, backgroundColor, newColor, checked);
		return true;
	}

	private void fill(int x, int y, Bitmap<Integer> canvas, int backgroundColor, int newColor, boolean[][] checked) {
		if(x < 0 || y < 0 || x >= canvas.width || y >= canvas.height) return;
		if(checked[x][y]) return;

		checked[x][y] = true;
		if(canvas.getPixel(x, y) == backgroundColor) {
			canvas.setPixel(x, y, newColor);

			fill(x - 1, y, canvas, backgroundColor, newColor, checked);
			fill(x + 1, y, canvas, backgroundColor, newColor, checked);
			fill(x, y - 1, canvas, backgroundColor, newColor, checked);
			fill(x, y + 1, canvas, backgroundColor, newColor, checked);
		}
	}

}
