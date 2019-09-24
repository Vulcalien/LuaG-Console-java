package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.game.Game;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;

public class SpriteAtlasPreview extends GUIComponent {

	public static final int VERTICAL_TILES = 8;

	private final SpriteEditor editor;

	public SpriteAtlasPreview(int x, int y, int w, int h, SpriteEditor editor) {
		super(x, y, w, h);
		this.editor = editor;
	}

	public void render(Screen screen) {
		screen.draw(editor.atlas.getSubimage(0, editor.atlasOffset * 8, w, h), x, y);
	}

	public void onPress(int xMouse, int yMouse) {
		int xs = xMouse / 8;
		int ys = yMouse / 8 + editor.atlasOffset;

		int id = xs + ys * 16; // 16 = atlas.width (in sprites)
		editor.spriteID = id;

		Game game = editor.editorPanel.game;
		editor.preview = game.getSprite(id, editor.scope, editor.scope);
	}

	public void onMouseScroll(int xMouse, int yMouse, int count) {
		int newOffset = editor.atlasOffset + count;
		if(newOffset >= 0 && newOffset + h / 8 <= 16) {
			editor.atlasOffset = newOffset;
		}
	}

}
