package vulc.jlconsole.editor.map.gui;

import vulc.jlconsole.editor.map.compiler.MapCompiler;
import vulc.jlconsole.gfx.gui.GUIButton;
import vulc.jlconsole.gfx.panel.EditorPanel;

public class SaveMapButton extends GUIButton {

	public SaveMapButton(int x, int y, int w, int h, EditorPanel panel) {
		super(x, y, w, h);

		action = () -> {
			MapCompiler.compile(panel.game.map);
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
