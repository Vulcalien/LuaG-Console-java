package vulc.luag.editor.sprite.tool;

import java.util.ArrayList;
import java.util.List;

import vulc.luag.editor.sprite.gui.SpriteToolButton;
import vulc.luag.gfx.Colors;

public class SpriteToolkit {

	public final List<SpriteToolButton> buttons = new ArrayList<SpriteToolButton>();

	public final SpriteTool pencil = new PencilTool();
	public final SpriteTool bucket = new BucketTool();
	public final SpriteTool pickup = new PickupTool();
	public final SpriteTool select = new SelectTool();

	public SpriteTool currentTool;

	public void setTool(SpriteTool tool) {
		currentTool = tool;

		for(int i = 0; i < buttons.size(); i++) {
			SpriteToolButton button = buttons.get(i);

			if(button.tool == tool) {
				button.colorAsBool = Colors.FOREGROUND_HIGHLIGHT;
			} else {
				button.colorAsBool = Colors.FOREGROUND_1;
			}
		}
	}

}
