package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.editor.sprite.tool.SpriteTool;
import vulc.luag.gfx.gui.GUIButton;

public class SpriteToolButton extends GUIButton {

	public SpriteToolButton(int x, int y, int w, int h, SpriteEditor editor, SpriteTool tool) {
		super(x, y, w, h);

		this.action = () -> {
			editor.currentTool = tool;
		};
	}

}
