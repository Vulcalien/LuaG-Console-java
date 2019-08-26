package vulc.luag.editor.map.gui;

import vulc.luag.editor.map.MapEditor;
import vulc.luag.gfx.gui.GUIButton;

public class SaveMapButton extends GUIButton {

	public SaveMapButton(int x, int y, int w, int h, MapEditor editor) {
		super(x, y, w, h);

		action = () -> {
			editor.onSave();
			setContentModified(false);
		};
		opaque = true;
		text = "SAVE";
		setContentModified(false);
	}

	public void setContentModified(boolean flag) {
		if(flag) {
			background = 0xffff66;
		} else {
			background = 0xbbbbbb;
		}
	}

}
