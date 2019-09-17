package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.editor.sprite.tool.SpriteToolkit;
import vulc.luag.gfx.Icons;
import vulc.luag.gfx.gui.GUIButton;
import vulc.luag.gfx.gui.GUIPanel;

public class SpriteToolbar extends GUIPanel {

	private boolean undoIsPressed = false;
	private boolean redoIsPressed = false;
	private boolean undoWasPressed = false;
	private boolean redoWasPressed = false;

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

		GUIButton undoButton = new GUIButton(1, 28, 8, 8);
		undoButton.opaque = true;
		undoButton.background = editor.editorPanel.secondaryColor;
		undoButton.setImage(Icons.UNDO, editor.editorPanel.primaryTextColor);
		undoButton.action = () -> {
			undoIsPressed = true;
			if(undoWasPressed) return;
			editor.undo();
		};
		this.add(undoButton);

		GUIButton redoButton = new GUIButton(1, 37, 8, 8);
		redoButton.opaque = true;
		redoButton.background = editor.editorPanel.secondaryColor;
		redoButton.setImage(Icons.REDO, editor.editorPanel.primaryTextColor);
		redoButton.action = () -> {
			redoIsPressed = true;
			if(redoWasPressed) return;
			editor.redo();
		};
		this.add(redoButton);
	}

	public void tick() {
		super.tick();
		undoWasPressed = undoIsPressed;
		redoWasPressed = redoIsPressed;

		undoIsPressed = false;
		redoIsPressed = false;
	}

}
