package vulc.luag.editor.map.gui;

import vulc.luag.editor.map.MapEditor;
import vulc.luag.gfx.gui.GUIComponent;
import vulc.luag.gfx.gui.GUILabel;
import vulc.luag.gfx.gui.GUIPanel;
import vulc.luag.gfx.gui.GUITextBox;

public class MapSelectTilePanel extends GUIPanel {

	public MapSelectTilePanel(int x, int y, int w, int h, MapEditor editor) {
		super(x, y, w, h);

		opaque = true;
		background = editor.editorPanel.secondaryColor;

		GUILabel label = new GUILabel(1, 1, this.w, 10);
		label.textColor = editor.editorPanel.secondaryTextColor;
		label.text = "Tile";
		this.add(label);

		GUITextBox textBox = new MapSelectTileTextBox(2, label.h + 1, this.w - 4, 10, editor);
		this.add(textBox);
		editor.selectTileTextBox = textBox;

		GUIComponent tilePreview = new MapTilePreview((this.w - 16) / 2,
		                                              textBox.y + textBox.h + 1,
		                                              16, 16,
		                                              editor);
		this.add(tilePreview);

	}

}
