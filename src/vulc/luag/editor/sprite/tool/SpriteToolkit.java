package vulc.luag.editor.sprite.tool;

import java.util.ArrayList;
import java.util.List;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.editor.sprite.gui.SpriteToolButton;

public class SpriteToolkit {

	private final SpriteEditor editor;

	public final List<SpriteToolButton> buttons = new ArrayList<SpriteToolButton>();

	public final SpriteTool pencil = new PencilTool();
	public final SpriteTool bucket = new BucketTool();
	public final SpriteTool pickup = new PickupTool();

	public SpriteTool currentTool;

	public SpriteToolkit(SpriteEditor editor) {
		this.editor = editor;
	}

	public void setTool(SpriteTool tool) {
		currentTool = tool;

		for(int i = 0; i < buttons.size(); i++) {
			SpriteToolButton button = buttons.get(i);

			if(button.tool == tool) {
				button.colorAsBool = editor.editorPanel.highlightColor;
			} else {
				button.colorAsBool = editor.editorPanel.primaryTextColor;
			}
		}
	}

}
