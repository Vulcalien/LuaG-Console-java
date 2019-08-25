package vulc.luag.editor.map.gui;

import vulc.luag.editor.map.MapEditor;
import vulc.luag.gfx.gui.GUILabel;
import vulc.luag.gfx.gui.GUIPanel;
import vulc.luag.gfx.gui.GUITextBox;
import vulc.luag.gfx.panel.EditorPanel;

public class MapSizePanel extends GUIPanel {

	public static final boolean WIDTH = false, HEIGHT = true;

	public MapSizePanel(int x, int y, int w, int h, MapEditor editor, boolean flag) {
		super(x, y, w, h);

		EditorPanel editorPanel = editor.editorPanel;

		opaque = true;
		background = editorPanel.secondaryColor;

		GUILabel label = new GUILabel(1, 1, this.w, 10);
		label.textColor = editorPanel.secondaryTextColor;
		this.add(label);

		GUITextBox textBox = new MapSizeTextBox(2, label.h + 1, this.w - 4, 10, editor, flag);
		add(textBox);

		if(flag == WIDTH) {
			label.text = "Map W";
			editor.wTextBox = textBox;
		} else {
			label.text = "Map H";
			editor.hTextBox = textBox;
		}
	}

}
