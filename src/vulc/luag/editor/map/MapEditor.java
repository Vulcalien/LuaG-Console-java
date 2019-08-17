package vulc.luag.editor.map;

import java.awt.event.KeyEvent;

import vulc.luag.Console;
import vulc.luag.editor.Editor;
import vulc.luag.editor.map.gui.SaveMapButton;
import vulc.luag.game.Game;
import vulc.luag.game.map.Map;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;
import vulc.luag.gfx.gui.GUILabel;
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

	private final GUITextBox wTextBox, hTextBox, selectTileTextBox;
	private final SaveMapButton saveButton;

	private int xOffset = 0, yOffset = 0;
	private int selectedTile = 0;

	public MapEditor(Console console, EditorPanel editorPanel, int x, int y, int w, int h) {
		super(console, editorPanel, x, y, w, h);

		moveUp = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_W);
		moveLeft = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_A);
		moveDown = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_S);
		moveRight = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_D);

		// INTERFACE
		guiPanel.opaque = true;
		guiPanel.background = 0x000000;

		// sidebar
		GUIPanel sidebar;
		{
			int sidebarElementSpace = 5;

			int wSidebar = 36;
			int hSidebar = guiPanel.h - 10;

			sidebar = new GUIPanel(guiPanel.w - wSidebar - 5, 5, wSidebar, hSidebar);
			sidebar.opaque = true;
			sidebar.background = editorPanel.secondaryColor;
			guiPanel.add(sidebar);

			GUIPanel widthPanel;
			{
				widthPanel = new GUIPanel(1, 1, wSidebar - 2, 23);
				widthPanel.opaque = true;
				widthPanel.background = editorPanel.secondaryColor;

				GUILabel widthLabel = new GUILabel(1, 1, widthPanel.w, 10);
				widthLabel.textColor = editorPanel.secondaryTextColor;
				widthLabel.text = "Map W";
				widthPanel.add(widthLabel);

				wTextBox = new GUITextBox(2, widthLabel.h + 1, widthPanel.w - 4, 10) {
					public void onLostFocus() {
						text = editorPanel.game.map.width + "";
					}

					public void onEnterPress() {
						super.onEnterPress();
						resizeMap();
					}
				};
				wTextBox.numbersOnly = true;
				wTextBox.nChars = 4;
				wTextBox.opaque = true;
				wTextBox.background = 0xffffff;
				wTextBox.textColor = 0x000000;
				wTextBox.text = editorPanel.game.map.width + "";
				widthPanel.add(wTextBox);
			}
			sidebar.add(widthPanel);

			GUIPanel heightPanel;
			{
				heightPanel = new GUIPanel(1, widthPanel.y + widthPanel.h + sidebarElementSpace,
				                           wSidebar - 2, 23);
				heightPanel.opaque = true;
				heightPanel.background = editorPanel.secondaryColor;

				GUILabel heightLabel = new GUILabel(1, 1, heightPanel.w, 10);
				heightLabel.textColor = editorPanel.secondaryTextColor;
				heightLabel.text = "Map H";
				heightPanel.add(heightLabel);

				hTextBox = new GUITextBox(2, heightLabel.h + 1, widthPanel.w - 4, 10) {
					public void onLostFocus() {
						text = editorPanel.game.map.height + "";
					}

					public void onEnterPress() {
						super.onEnterPress();
						resizeMap();
					}
				};
				hTextBox.numbersOnly = true;
				hTextBox.nChars = 4;
				hTextBox.opaque = true;
				hTextBox.background = 0xffffff;
				hTextBox.textColor = 0x000000;
				hTextBox.text = editorPanel.game.map.height + "";
				heightPanel.add(hTextBox);
			}
			sidebar.add(heightPanel);

			GUIPanel selectTilePanel;
			{
				selectTilePanel = new GUIPanel(1, heightPanel.y + heightPanel.h + sidebarElementSpace,
				                               wSidebar - 2, 39);
				selectTilePanel.opaque = true;
				selectTilePanel.background = editorPanel.secondaryColor;

				GUILabel selectTileLabel = new GUILabel(1, 1, selectTilePanel.w, 10);
				selectTileLabel.textColor = editorPanel.secondaryTextColor;
				selectTileLabel.text = "Tile";
				selectTilePanel.add(selectTileLabel);

				selectTileTextBox = new GUITextBox(2, selectTileLabel.h + 1, selectTilePanel.w - 4, 10) {
					public void onLostFocus() {
						text = selectedTile + "";
					}

					public void onEnterPress() {
						super.onEnterPress();

						int id;
						if(selectTileTextBox.text.equals("")) id = 0;
						else id = Integer.parseInt(selectTileTextBox.text);
						if(id > 255) id = 255;

						selectedTile = id;
						text = id + "";
					}
				};
				selectTileTextBox.text = selectedTile + "";
				selectTileTextBox.nChars = 3;
				selectTileTextBox.numbersOnly = true;
				selectTileTextBox.opaque = true;
				selectTileTextBox.background = 0xffffff;
				selectTileTextBox.textColor = 0x000000;
				selectTilePanel.add(selectTileTextBox);

				GUIComponent tilePreviewComp = new GUIComponent((selectTilePanel.w - 16) / 2,
				                                                selectTileTextBox.y + selectTileTextBox.h + 1,
				                                                16,
				                                                16) {
					public void render(Screen screen) {
						super.render(screen);
						screen.draw(editorPanel.game.getSprite(selectedTile, 1, 1).getScaled(2), x, y);
					}
				};
				selectTilePanel.add(tilePreviewComp);
			}
			sidebar.add(selectTilePanel);

			saveButton = new SaveMapButton(1, hSidebar - 11, wSidebar - 2, 10, editorPanel);
			sidebar.add(saveButton);
		}

		GUIPanel levelPanel = new GUIPanel(5, 5, guiPanel.w - sidebar.w - 15, guiPanel.h - 10) {
			protected void drawComponents() {
				super.drawComponents();
				editorPanel.game.map.render(this.screen, editorPanel.game, xOffset, yOffset, 1);
			}

			public void press(int x, int y) {
				super.press(x, y);

				Game game = editorPanel.game;

				int xt = (x + xOffset - this.x) / 8;
				int yt = (y + yOffset - this.y) / 8;

				if(xt < 0 || yt < 0 || xt >= game.map.width || yt >= game.map.height) return;

				game.map.setTile(xt, yt, selectedTile);
				saveButton.setContentModified(true);
			}
		};
		levelPanel.background = editorPanel.secondaryColor;
		guiPanel.add(levelPanel);
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

		saveButton.setContentModified(true);
	}

	public String getTitle() {
		return "Map Editor";
	}

}
