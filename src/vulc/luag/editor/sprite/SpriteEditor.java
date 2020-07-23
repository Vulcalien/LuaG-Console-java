package vulc.luag.editor.sprite;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import vulc.bitmap.Bitmap;
import vulc.luag.editor.Editor;
import vulc.luag.editor.sprite.gui.SpriteAtlasPreview;
import vulc.luag.editor.sprite.gui.SpriteColorbar;
import vulc.luag.editor.sprite.gui.SpritePreview;
import vulc.luag.editor.sprite.gui.SpriteScopeSelector;
import vulc.luag.editor.sprite.gui.SpriteToolbar;
import vulc.luag.editor.sprite.history.History;
import vulc.luag.editor.sprite.tool.SpriteToolkit;
import vulc.luag.game.Game;
import vulc.luag.gfx.gui.GUIComponent;
import vulc.luag.gfx.gui.GUIPanel;
import vulc.luag.gfx.gui.GUITextBox;
import vulc.luag.gfx.panel.EditorPanel;
import vulc.luag.input.InputHandler.Key;
import vulc.luag.input.InputHandler.KeyType;

public class SpriteEditor extends Editor {

	public static final int PALETTE_SIZE = 8;

	public static final int DEFAULT_SCALE = 6;

	// preview and atlas
	public final SpriteAtlasPreview atlasPreview;
	public final Bitmap<Integer> atlas;
	public Bitmap<Integer> preview;
	public int spriteID = 0;
	public int scope = 1;

	public final SpriteToolkit toolkit = new SpriteToolkit();
	private final Key ctrl, p, f, k, s,
	        z, y, c, v,
	        up, left, down, right;

	// select color and last colors
	public int selectedColor;
	public final List<Integer> lastColors = new ArrayList<Integer>();

	// editing history
	public final History history = new History(this, 100);
	public boolean isEditing = false, wasEditing = false;
	public boolean shouldSaveContent = false;

	// selection and copy/paste
	public int selx0, sely0, selx1, sely1;
	public boolean pasteMode = false;
	public Bitmap<Integer> copied;
	public int xPasted, yPasted;

	public GUITextBox selectColorTxt;

	public SpriteEditor(EditorPanel panel, int x, int y, int w, int h) {
		super(panel, x, y, w, h);
		this.atlas = panel.game.atlas;

		// keys
		this.ctrl = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_CONTROL);
		this.p = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_P);
		this.f = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_F);
		this.k = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_K);
		this.s = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_S);

		this.z = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_Z);
		this.y = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_Y);
		this.c = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_C);
		this.v = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_V);

		this.up = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_UP);
		this.left = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_LEFT);
		this.down = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_DOWN);
		this.right = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_RIGHT);

		preview = panel.game.getSprite(spriteID, scope, scope);

		// default palette
		lastColors.add(0x00_00_00);
		lastColors.add(0xff_ff_ff);
		lastColors.add(0xff_00_00);
		lastColors.add(0x00_ff_00);
		lastColors.add(0x00_00_ff);
		lastColors.add(0xff_ff_00);
		lastColors.add(0xff_00_ff);
		lastColors.add(0x00_ff_ff);

		history.save();

		// INTERFACE
		guiPanel.background = 0x000000;

		int previewSize = Game.SPR_SIZE * DEFAULT_SCALE;
		GUIComponent sprPreview = new SpritePreview((guiPanel.w - previewSize) / 2 - SpritePreview.BORDER, 5,
		                                            previewSize + SpritePreview.BORDER * 2,
		                                            previewSize + SpritePreview.BORDER * 2,
		                                            this);
		guiPanel.add(sprPreview);

		int scopes[] = {1, 2};
		SpriteScopeSelector scopeSelector = new SpriteScopeSelector(sprPreview.x - 9 - 5,
		                                                            (sprPreview.y + sprPreview.h) / 2 - 5,
		                                                            10, 1 + scopes.length * 9,
		                                                            this, scopes);
		guiPanel.add(scopeSelector);

		int hAtlas = 8 * Game.SPR_SIZE;
		atlasPreview = new SpriteAtlasPreview((guiPanel.w - atlas.width) / 2, guiPanel.h - hAtlas - 5,
		                                      atlas.width, hAtlas,
		                                      this);
		guiPanel.add(atlasPreview);

		int hToolbar = 9 * 5 + 1;
		GUIPanel toolbar = new SpriteToolbar(atlasPreview.x, sprPreview.y + (sprPreview.h - hToolbar) / 2,
		                                     19, hToolbar,
		                                     this);
		guiPanel.add(toolbar);

		int xColorbar = sprPreview.x + sprPreview.w + 5;
		GUIPanel colorbar = new SpriteColorbar(xColorbar, 5,
		                                       guiPanel.w - xColorbar - 5, sprPreview.h,
		                                       this);
		guiPanel.add(colorbar);

		toolkit.setTool(toolkit.pencil);
		selectColor(0xffffff);
	}

	public void tick() {
		if(ctrl.isKeyDown()) {
			if(z.isPressed()) undo();
			if(y.isPressed()) redo();

			if(c.isPressed() && toolkit.currentTool == toolkit.select) copy();
			if(v.isPressed()) paste();
		} else {
			if(p.isPressed()) toolkit.setTool(toolkit.pencil);
			if(f.isPressed()) toolkit.setTool(toolkit.bucket);
			if(k.isPressed()) toolkit.setTool(toolkit.pickup);
			if(s.isPressed()) toolkit.setTool(toolkit.select);

			if(up.isPressed()) moveSelected(0, -1);
			if(left.isPressed()) moveSelected(-1, 0);
			if(down.isPressed()) moveSelected(0, +1);
			if(right.isPressed()) moveSelected(+1, 0);
		}

		if(pasteMode && toolkit.currentTool != toolkit.select) {
			endPaste();
		}

		boolean shouldSave = wasEditing && !isEditing;
		wasEditing = isEditing;
		isEditing = false;

		if(shouldSave) {
			saveHistory();
		}
	}

	public void updatePreview() {
		preview = atlasPreview.getPreview();
	}

	private void saveHistory() {
		// update atlas and then history.save will record it
		atlas.draw(preview, (spriteID % 16) * Game.SPR_SIZE, (spriteID / 16) * Game.SPR_SIZE);
		history.save();
	}

	public String getTitle() {
		return "Sprite Editor";
	}

	public void selectColor(int color) {
		if(!lastColors.contains(selectedColor)) {
			lastColors.add(0, selectedColor);
			lastColors.remove(PALETTE_SIZE);
		}
		this.selectedColor = color;

		String colorString = Integer.toString(color, 16);
		while(colorString.length() < 6) {
			colorString = "0" + colorString;
		}
		selectColorTxt.text = colorString;
	}

	public boolean shouldSave() {
		return shouldSaveContent;
	}

	public void onSave() {
		try {
			int[] pixels = new int[atlas.width * atlas.height];
			for(int i = 0; i < atlas.size(); i++) {
				pixels[i] = atlas.raster.getPixel(i);
			}

			BufferedImage img = new BufferedImage(atlas.width, atlas.height, BufferedImage.TYPE_INT_RGB);
			img.setRGB(0, 0, atlas.width, atlas.height, pixels, 0, atlas.width);

			ImageIO.write(img, "png", new File(Game.ATLAS_FILE));
			shouldSaveContent = false;
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void undo() {
		if(history.undo()) {
			updatePreview();
			shouldSaveContent = true;
		}
	}

	public void redo() {
		if(history.redo()) {
			updatePreview();
			shouldSaveContent = true;
		}
	}

	public void copy() {
		if(selx0 >= preview.width || selx1 >= preview.width
		   || sely0 >= preview.height || sely1 >= preview.height) return;

		int x0 = Math.min(selx0, selx1);
		int y0 = Math.min(sely0, sely1);
		int x1 = Math.max(selx0, selx1);
		int y1 = Math.max(sely0, sely1);

		copied = preview.getSubimage(x0, y0, x1 - x0 + 1, y1 - y0 + 1);
	}

	public void paste() {
		if(copied == null) return;
		if(pasteMode) endPaste();

		pasteMode = true;
		toolkit.setTool(toolkit.select);
		xPasted = 0;
		yPasted = 0;

		selx0 = xPasted;
		sely0 = yPasted;
		selx1 = xPasted + copied.width - 1;
		sely1 = yPasted + copied.height - 1;
	}

	public void endPaste() {
		if(!pasteMode) return;

		pasteMode = false;
		preview.draw(copied, xPasted, yPasted);

		saveHistory();
		shouldSaveContent = true;
	}

	public void moveSelected(int x, int y) {
		if(pasteMode) {
			xPasted += x;
			yPasted += y;

			selx0 += x;
			sely0 += y;
			selx1 += x;
			sely1 += y;
		} else if(toolkit.currentTool == toolkit.select) {
			copy();

			pasteMode = true;
			xPasted = selx0;
			yPasted = sely0;

			preview.fill(selx0, sely0, selx1, sely1, 0xff00ff);

			// after setting pasteMode it's necessary to move the pasted bitmap
			moveSelected(x, y);
		}
	}

	public void setScope(int scope) {
		this.scope = scope;

		atlasPreview.setScope(scope);

		updatePreview();
	}

}
