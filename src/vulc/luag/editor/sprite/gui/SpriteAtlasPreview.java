package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.gui.AtlasPreview;
import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.game.Game;

public class SpriteAtlasPreview extends AtlasPreview {

	private final SpriteEditor editor;

	public SpriteAtlasPreview(int x, int y, int w, int h, SpriteEditor editor, int verticalTiles) {
		super(x, y, w, h, editor.editorPanel.game, verticalTiles);
		this.editor = editor;
	}

	public void onMouseDown(int xMouse, int yMouse) {
		super.onMouseDown(xMouse, yMouse);
		editor.spriteID = selectedTile;

		resetPreview();

		editor.resetHistory();
	}

	public void resetPreview() {
		Game game = editor.editorPanel.game;
		editor.preview = game.getSprite(selectedTile, editor.scope, editor.scope);
	}

}
