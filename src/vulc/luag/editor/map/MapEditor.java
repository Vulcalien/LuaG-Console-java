package vulc.luag.editor.map;

import java.awt.event.KeyEvent;

import vulc.luag.editor.Editor;
import vulc.luag.editor.map.gui.MapAtlasPreview;
import vulc.luag.editor.map.gui.MapPreview;
import vulc.luag.editor.map.gui.MapSizeTextBox;
import vulc.luag.game.Game;
import vulc.luag.game.map.Map;
import vulc.luag.gfx.gui.GUIPanel;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;

public class MapEditor extends Editor {

	private final Key moveUp;
	private final Key moveLeft;
	private final Key moveDown;
	private final Key moveRight;

	public int xOffset = 0, yOffset = 0;
	public int selectedTile = 0;

	public boolean shouldSaveContent = false;

	public MapEditor(EditorPanel editorPanel, int x, int y, int w, int h) {
		super(editorPanel, x, y, w, h);

		moveUp = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_W);
		moveLeft = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_A);
		moveDown = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_S);
		moveRight = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_D);

		// INTERFACE
		guiPanel.background = 0x000000;

		GUIPanel previewPanel = new MapPreview(5, 5, guiPanel.w - 10, 10 * Game.SPR_SIZE,
		                                       this);
		guiPanel.add(previewPanel);

		int verticalTiles = 4;
		int wAtlas = editorPanel.game.atlas.width;
		int hAtlas = verticalTiles * Game.SPR_SIZE;
		MapAtlasPreview atlasPreview = new MapAtlasPreview((guiPanel.w - wAtlas) / 2, guiPanel.h - hAtlas - 5,
		                                                   wAtlas, hAtlas,
		                                                   this, verticalTiles);
		guiPanel.add(atlasPreview);
	}

	public void tick() {
		int moveSpeed = 2;
		if(moveUp.isKeyDown()) yOffset -= moveSpeed;
		if(moveLeft.isKeyDown()) xOffset -= moveSpeed;
		if(moveDown.isKeyDown()) yOffset += moveSpeed;
		if(moveRight.isKeyDown()) xOffset += moveSpeed;
	}

	public void resizeMap(int newSide, boolean sideFlag) {
		Map oldMap = editorPanel.game.map;

		int w = (sideFlag == MapSizeTextBox.WIDTH ? newSide : oldMap.width);
		int h = (sideFlag == MapSizeTextBox.HEIGHT ? newSide : oldMap.height);
		Map newMap = new Map(w, h);

		x_loop:
		for(int x = 0; x < oldMap.width; x++) {
			if(x >= w) break x_loop;

			y_loop:
			for(int y = 0; y < oldMap.height; y++) {
				if(y >= h) break y_loop;

				newMap.setTile(x, y, oldMap.getTile(x, y));
			}
		}

		editorPanel.game.map = newMap;
		shouldSaveContent = true;
	}

	public boolean shouldSave() {
		return shouldSaveContent;
	}

	public void onSave() {
		MapCompiler.compile(editorPanel.game.map);
		shouldSaveContent = false;
	}

	public String getTitle() {
		return "Map Editor";
	}

}
