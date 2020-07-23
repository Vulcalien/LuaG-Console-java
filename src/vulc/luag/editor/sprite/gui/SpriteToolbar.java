package vulc.luag.editor.sprite.gui;

import vulc.luag.editor.sprite.SpriteEditor;
import vulc.luag.editor.sprite.tool.SpriteTool;
import vulc.luag.editor.sprite.tool.SpriteToolkit;
import vulc.luag.gfx.Colors;
import vulc.luag.gfx.Icons;
import vulc.luag.gfx.gui.GUIButton;
import vulc.luag.gfx.gui.GUIPanel;

public class SpriteToolbar extends GUIPanel {

	public SpriteToolbar(int x, int y, int w, int h, SpriteEditor editor) {
		super(x, y, w, h);

		this.background = Colors.BACKGROUND_0;

		SpriteToolkit toolkit = editor.toolkit;

		SpriteToolButton pencilButton = createToolButton(0, 0, editor, toolkit.pencil);
		pencilButton.setImage(Icons.PENCIL_TOOL, Colors.FOREGROUND_1);
		this.add(pencilButton);

		SpriteToolButton bucketButton = createToolButton(0, 1, editor, toolkit.bucket);
		bucketButton.setImage(Icons.BUCKET_TOOL, Colors.FOREGROUND_1);
		this.add(bucketButton);

		SpriteToolButton pickupButton = createToolButton(0, 2, editor, toolkit.pickup);
		pickupButton.setImage(Icons.PICKUP_TOOL, Colors.FOREGROUND_1);
		this.add(pickupButton);

		GUIButton undoButton = createGUIButton(0, 3);
		undoButton.setImage(Icons.UNDO, Colors.FOREGROUND_1);
		undoButton.onMousePressAction = () -> {
			editor.undo();
		};
		this.add(undoButton);

		GUIButton redoButton = createGUIButton(0, 4);
		redoButton.setImage(Icons.REDO, Colors.FOREGROUND_1);
		redoButton.onMousePressAction = () -> {
			editor.redo();
		};
		this.add(redoButton);

		SpriteToolButton selectButton = createToolButton(1, 0, editor, toolkit.select);
		selectButton.setImage(Icons.SELECT_TOOL, Colors.FOREGROUND_1);
		this.add(selectButton);
	}

	private static SpriteToolButton createToolButton(int x, int y, SpriteEditor editor, SpriteTool tool) {
		int[] b = getButtonBounds(x, y);
		return (SpriteToolButton) setAttributes(new SpriteToolButton(b[0], b[1], b[2], b[3], editor, tool));
	}

	private static GUIButton createGUIButton(int x, int y) {
		int[] b = getButtonBounds(x, y);
		return setAttributes(new GUIButton(b[0], b[1], b[2], b[3]));
	}

	private static int[] getButtonBounds(int x, int y) {
		int[] result = new int[4];
		result[0] = 1 + 9 * x;
		result[1] = 1 + 9 * y;
		result[2] = 8;
		result[3] = 8;
		return result;
	}

	private static GUIButton setAttributes(GUIButton button) {
		button.opaque = true;
		button.background = Colors.BACKGROUND_1;
		return button;
	}

}
