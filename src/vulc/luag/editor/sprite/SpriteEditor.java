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
	private final Key ctrl, p, f, k, z, y, s;

	// select color and last colors
	public int selectedColor;
	public final List<Integer> lastColors = new ArrayList<Integer>();

	// editing history
	public final History history = new History(this, 100);
	public boolean isEditing = false, wasEditing = false;
	public boolean shouldSaveContent = false;

	public GUITextBox selectColorTxt;

	public SpriteEditor(EditorPanel panel, int x, int y, int w, int h) {
		super(panel, x, y, w, h);
		this.atlas = panel.game.atlas;

		// keys
		this.ctrl = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_CONTROL);
		this.p = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_P);
		this.f = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_F);
		this.k = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_K);
		this.z = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_Z);
		this.y = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_Y);
		this.s = input.new Key(KeyType.KEYBOARD, KeyEvent.VK_S);

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
		boolean shouldSaveHistory = wasEditing && !isEditing;
		wasEditing = isEditing;
		isEditing = false;

		if(shouldSaveHistory) {
			updateAtlas();
			history.save();
		}

		if(ctrl.isKeyDown()) {
			if(z.isPressed()) undo();
			if(y.isPressed()) redo();
		} else {
			if(p.isPressed()) toolkit.setTool(toolkit.pencil);
			if(f.isPressed()) toolkit.setTool(toolkit.bucket);
			if(k.isPressed()) toolkit.setTool(toolkit.pickup);
			if(s.isPressed()) toolkit.setTool(toolkit.select);
		}
	}

	public void updatePreview() {
		preview = atlasPreview.getPreview();
	}

	private void updateAtlas() {
		atlas.draw(preview, (spriteID % 16) * Game.SPR_SIZE, (spriteID / 16) * Game.SPR_SIZE);
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

	public void setScope(int scope) {
		this.scope = scope;

		atlasPreview.setScope(scope);

		updatePreview();
	}

}
