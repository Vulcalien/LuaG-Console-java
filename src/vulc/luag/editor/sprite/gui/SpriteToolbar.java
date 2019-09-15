package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.editor.sprite.tool.SpriteToolkit;
import vulc.luag.gfx.Icons;
import vulc.luag.gfx.gui.GUIPanel;

public class SpriteToolbar extends GUIPanel {

	public SpriteToolbar(int x, int y, int w, int h, SpriteEditor editor) {
		super(x, y, w, h);

		this.background = editor.editorPanel.primaryColor;

		SpriteToolkit toolkit = editor.toolkit;

		SpriteToolButton pencilButton = new SpriteToolButton(1, 1, 8, 8, editor, toolkit.pencil);
		pencilButton.opaque = true;
		pencilButton.background = editor.editorPanel.secondaryColor;
		pencilButton.setImage(Icons.PENCIL_TOOL, editor.editorPanel.primaryTextColor);
		this.add(pencilButton);

		SpriteToolButton bucketButton = new SpriteToolButton(1, 10, 8, 8, editor, toolkit.bucket);
		bucketButton.opaque = true;
		bucketButton.background = editor.editorPanel.secondaryColor;
		bucketButton.setImage(Icons.BUCKET_TOOL, editor.editorPanel.primaryTextColor);
		this.add(bucketButton);

		SpriteToolButton pickupButton = new SpriteToolButton(1, 19, 8, 8, editor, toolkit.pickup);
		pickupButton.opaque = true;
		pickupButton.background = editor.editorPanel.secondaryColor;
		pickupButton.setImage(Icons.PICKUP_TOOL, editor.editorPanel.primaryTextColor);
		this.add(pickupButton);
	}

}
