package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;
import vulc.luag.gfx.gui.GUIPanel;
import vulc.luag.gfx.gui.GUITextBox;

public class SpriteColorbar_ extends GUIPanel {

	public SpriteColorbar_(int x, int y, int w, int h, SpriteEditor editor) {
		super(x, y, w, h);

		this.background = Colors.BACKGROUND_0;

		GUIComponent colorPreview = new GUIComponent((w - 16) / 2, 1, 16, 16) {
			public void render(Screen screen) {
				screen.fill(x, y, x + w - 1, y + h - 1, editor.selectedColor);
			}
		};
		this.add(colorPreview);

		GUITextBox selectColorTxt = new GUITextBox(1, 18, w - 2, 10) {
			public void onEnterPress() {
				super.onEnterPress();
				editor.selectColor(Integer.parseInt(text, 16));
			}
		};
		selectColorTxt.opaque = true;
		selectColorTxt.background = 0xffffff;
		selectColorTxt.textColor = 0x000000;
		selectColorTxt.nChars = 6;
		selectColorTxt.acceptedText = GUITextBox.HEX_ONLY;
		editor.selectColorTxt = selectColorTxt;
		this.add(selectColorTxt);

		int historyColumns = 4;
		int hHistory = 9 * (SpriteEditor.PALETTE_SIZE / historyColumns) + 1;
		int wHistory = 9 * historyColumns + 1;
		GUIPanel history = new SpriteColorbarPalette((w - wHistory) / 2, h - hHistory - 1,
		                                             wHistory, hHistory,
		                                             editor, historyColumns);
		this.add(history);
	}

}
