package vulc.luag.editor.sprite.gui;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.gui.AtlasPreview;
import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.game.Game;

public class SpriteAtlasPreview extends AtlasPreview {

	private final SpriteEditor editor;

	public SpriteAtlasPreview(int x, int y, int w, int h, SpriteEditor editor) {
		super(x, y, w, h, editor.editorPanel.game);
		this.editor = editor;
	}

	public void onMouseDown(int xMouse, int yMouse) {
		super.onMouseDown(xMouse, yMouse);

		editor.updatePreview();
	}

	public Bitmap<Integer> getPreview() {
		Game game = editor.editorPanel.game;
		return game.getSprite(selected, editor.scope, editor.scope);
	}

	public void setSelected(int xs, int ys) {
		super.setSelected(xs, ys);
		editor.spriteID = selected;
	}

}
