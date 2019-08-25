package vulc.luag.editor.map.gui;

import vulc.luag.editor.map.MapEditor;
import vulc.luag.game.Game;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;

public class MapTilePreview extends GUIComponent {

	private final MapEditor editor;
	private final Game game;

	public MapTilePreview(int x, int y, int w, int h, MapEditor editor) {
		super(x, y, w, h);
		this.editor = editor;
		this.game = editor.editorPanel.game;
	}

	public void render(Screen screen) {
		super.render(screen);
		screen.draw(game.getSprite(editor.selectedTile, 1, 1).getScaled(2), x, y);
	}

}
