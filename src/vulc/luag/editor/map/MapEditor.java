package vulc.luag.editor.map;

import java.awt.event.KeyEvent;

import vulc.luag.editor.Editor;
import vulc.luag.editor.map.gui.MapPreview;
import vulc.luag.editor.map.gui.MapSidebar;
import vulc.luag.game.map.Map;
import vulc.luag.gfx.gui.GUIPanel;
import vulc.luag.gfx.gui.GUITextBox;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;

public class MapEditor extends Editor {

	private final Key moveUp;
	private final Key moveLeft;
	private final Key moveDown;
	private final Key moveRight;

	public GUITextBox wTextBox, hTextBox, selectTileTextBox;

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

		// sidebar
		int wSidebar = 36;
		int hSidebar = guiPanel.h - 10;
		GUIPanel sidebar = new MapSidebar(guiPanel.w - wSidebar - 5, 5, wSidebar, hSidebar,
		                                  this);
		guiPanel.add(sidebar);

		GUIPanel previewPanel = new MapPreview(5, 5, guiPanel.w - sidebar.w - 15, guiPanel.h - 10,
		                                       this);
		guiPanel.add(previewPanel);
	}

	public void tick() {
		int moveSpeed = 2;
		if(moveUp.isKeyDown()) yOffset -= moveSpeed;
		if(moveLeft.isKeyDown()) xOffset -= moveSpeed;
		if(moveDown.isKeyDown()) yOffset += moveSpeed;
		if(moveRight.isKeyDown()) xOffset += moveSpeed;
	}

	public void resizeMap() {
		String wText = wTextBox.text;
		String hText = hTextBox.text;

		int w, h;
		if(!wText.equals("")) w = Integer.parseInt(wText);
		else w = 0;

		if(!hText.equals("")) h = Integer.parseInt(hText);
		else h = 0;

		wTextBox.text = w + "";
		hTextBox.text = h + "";

		Map oldMap = editorPanel.game.map;
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
