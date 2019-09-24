package vulc.luag.editor.map.gui;

import vulc.luag.editor.map.MapEditor;
import vulc.luag.game.Game;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.gui.GUIPanel;

public class MapPreview extends GUIPanel {

	private final MapEditor editor;
	private final Game game;

	public MapPreview(int x, int y, int w, int h, MapEditor editor) {
		super(x, y, w, h);
		this.editor = editor;
		this.game = editor.editorPanel.game;

		background = Colors.BACKGROUND_1;
	}

	protected void drawComponents() {
		super.drawComponents();
		game.map.render(this.screen, game, editor.xOffset, editor.yOffset, 1);
	}

	public void onPress(int xMouse, int yMouse) {
		super.onPress(xMouse, yMouse);

		int xt = (xMouse + editor.xOffset) / Game.SPR_SIZE;
		int yt = (yMouse + editor.yOffset) / Game.SPR_SIZE;

		if(xt < 0 || yt < 0 || xt >= game.map.width || yt >= game.map.height) return;

		if(game.map.getTile(xt, yt) != editor.selectedTile) {
			game.map.setTile(xt, yt, editor.selectedTile);
			editor.shouldSaveContent = true;
		}
	}

}
