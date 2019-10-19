package vulc.luag.editor.map.gui;

import vulc.luag.editor.map.MapEditor;
import vulc.luag.game.map.Map;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUILabel;
import vulc.luag.gfx.gui.GUIPanel;

public class MapSizePanel extends GUIPanel {

	public MapSizePanel(int x, int y, int w, int h, MapEditor editor) {
		super(x, y, w, h);

		Map map = editor.editorPanel.game.map;

		int textBoxWidth = (Screen.FONT.widthOf(' ') + Screen.FONT.getLetterSpacing()) * MapSizeTextBox.N_CHARS + 1;

		GUIPanel wPanel = new GUIPanel(0, 0, w / 2, h);
		{
			wPanel.background = Colors.BACKGROUND_0;

			GUILabel label = new GUILabel(0, 0, Screen.FONT.widthOf("width"), h);
			label.textColor = Colors.FOREGROUND_0;
			label.text = "width";
			wPanel.add(label);

			MapSizeTextBox textBox = new MapSizeTextBox(label.w + 3, 1,
			                                            textBoxWidth, h - 2,
			                                            editor, MapSizeTextBox.WIDTH);
			textBox.text = map.width + "";
			wPanel.add(textBox);
		}
		this.add(wPanel);

		GUIPanel hPanel = new GUIPanel(w / 2, 0, w / 2, h);
		{
			hPanel.background = Colors.BACKGROUND_0;

			GUILabel label = new GUILabel(0, 0, Screen.FONT.widthOf("height"), h);
			label.textColor = Colors.FOREGROUND_0;
			label.text = "height";
			hPanel.add(label);

			MapSizeTextBox textBox = new MapSizeTextBox(label.w + 3, 1,
			                                            textBoxWidth, h - 2,
			                                            editor, MapSizeTextBox.HEIGHT);
			textBox.text = map.height + "";
			hPanel.add(textBox);
		}
		this.add(hPanel);
	}

}
