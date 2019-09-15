package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.editor.sprite.tool.SpriteTool;
import vulc.luag.editor.sprite.tool.SpriteToolkit;
import vulc.luag.gfx.gui.GUIButton;

public class SpriteToolButton extends GUIButton {

	public final SpriteTool tool;

	public SpriteToolButton(int x, int y, int w, int h, SpriteEditor editor, SpriteTool tool) {
		super(x, y, w, h);

		this.tool = tool;

		SpriteToolkit toolkit = editor.toolkit;
		toolkit.buttons.add(this);

		this.action = () -> {
			toolkit.setTool(tool);
		};
	}

}
