package vulc.luag.editor.map.gui;

import vulc.luag.editor.map.MapEditor;
import vulc.luag.game.Game;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIPanel;

public class MapPreview extends GUIPanel {

	private final MapEditor editor;
	private final Game game;

	private int xPointed, yPointed;

	public MapPreview(int x, int y, int w, int h, MapEditor editor) {
		super(x, y, w, h);
		this.editor = editor;
		this.game = editor.editorPanel.game;

		background = Colors.BACKGROUND_1;
	}

	protected void drawComponents() {
		super.drawComponents();
		game.map.render(this.screen, game, editor.xOffset, editor.yOffset, 1);

		String xText = "x: " + xPointed;
		String yText = "y: " + yPointed;

		int wPanel = Math.max(Screen.FONT.widthOf(xText), Screen.FONT.widthOf(yText));
		int hPanel = Screen.FONT.getHeight() * 2 + 1;

		screen.fill(w - wPanel, h - hPanel, w, h, Colors.BACKGROUND_1);

		screen.write(xText, Colors.FOREGROUND_1,
		             w - wPanel, h - hPanel);
		screen.write(yText, Colors.FOREGROUND_1,
		             w - wPanel, h - Screen.FONT.getHeight());
	}

	public void onMouseDown(int xMouse, int yMouse) {
		super.onMouseDown(xMouse, yMouse);

		int xt = (xMouse + editor.xOffset) / Game.SPR_SIZE;
		int yt = (yMouse + editor.yOffset) / Game.SPR_SIZE;

		if(xt < 0 || yt < 0 || xt >= game.map.width || yt >= game.map.height) return;

		if(game.map.getTile(xt, yt) != editor.selectedTile) {
			game.map.setTile(xt, yt, editor.selectedTile);
			editor.shouldSaveContent = true;
		}
	}

	public void onMouseInside(int xMouse, int yMouse) {
		this.xPointed = (xMouse + editor.xOffset) / Game.SPR_SIZE;
		this.yPointed = (yMouse + editor.yOffset) / Game.SPR_SIZE;
	}

}
