package vulc.luag.editor.sprite;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import vulc.bitmap.Bitmap;
import vulc.luag.Console;
import vulc.luag.editor.Editor;
import vulc.luag.editor.sprite.gui.SpriteAtlasPreview;
import vulc.luag.editor.sprite.gui.SpriteColorbar;
import vulc.luag.editor.sprite.gui.SpritePreview;
import vulc.luag.editor.sprite.gui.SpriteToolbar;
import vulc.luag.editor.sprite.tool.SpriteToolkit;
import vulc.luag.game.Game;
import vulc.luag.gfx.gui.GUIComponent;
import vulc.luag.gfx.gui.GUIPanel;
import vulc.luag.gfx.gui.GUITextBox;
import vulc.luag.gfx.panel.EditorPanel;

public class SpriteEditor extends Editor {

	public static final int PALETTE_SIZE = 8;
	public static final int HISTORY_SIZE = 100;

	// preview and atlas
	public final Bitmap<Integer> atlas;
	public Bitmap<Integer> preview;
	public final int previewScale = 6;
	public int spriteID = 0;
	public int scope = 1; // TODO this can only be set to 1 at the moment
	public int atlasOffset = 0;

	public final SpriteToolkit toolkit = new SpriteToolkit();

	// select color and last colors
	public int selectedColor = 0x000000; // this value will change in constructor
	public final List<Integer> lastColors = new ArrayList<Integer>();

	// editing history
	public List<Bitmap<Integer>> history = new ArrayList<Bitmap<Integer>>();
	public int nextHistoryIndex = 0;
	public boolean isEditing = false, wasEditing = false;
	public boolean shouldSaveContent = false;

	public GUITextBox selectColorTxt;

	public SpriteEditor(Console console, EditorPanel panel, int x, int y, int w, int h) {
		super(console, panel, x, y, w, h);
		this.atlas = panel.game.atlas;

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

		historySave();

		// INTERFACE
		guiPanel.background = 0x000000;

		int previewSize = Game.SPR_SIZE * previewScale;
		GUIComponent sprPreview = new SpritePreview((guiPanel.w - previewSize) / 2 - SpritePreview.BORDER, 5,
		                                            previewSize + SpritePreview.BORDER * 2,
		                                            previewSize + SpritePreview.BORDER * 2,
		                                            this);
		guiPanel.add(sprPreview);

		int hAtlas = 8 * SpriteAtlasPreview.VERTICAL_TILES;
		GUIComponent atlasPreview = new SpriteAtlasPreview((guiPanel.w - atlas.width) / 2, guiPanel.h - hAtlas - 5,
		                                                   atlas.width, hAtlas,
		                                                   this);
		guiPanel.add(atlasPreview);

		int hToolbar = 9 * 5 + 1;
		GUIPanel toolbar = new SpriteToolbar(atlasPreview.x, sprPreview.y + (sprPreview.h - hToolbar) / 2,
		                                     10, hToolbar,
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
			historySave();
			updateAtlas();
		}
	}

	public void historySave() {
		// if UNDOs where done, clear the "future" history
		history = history.subList(0, nextHistoryIndex);

		history.add(preview.getCopy());
		if(history.size() > HISTORY_SIZE) {
			history.remove(0);
		}
		nextHistoryIndex = history.size();
	}

	public void resetHistory() {
		history.clear();
		nextHistoryIndex = 0;

		historySave(); // save the first sprite
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
			for(int i = 0; i < atlas.pixels.length; i++) {
				pixels[i] = atlas.pixels[i];
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
		if(nextHistoryIndex == 1) return;

		preview = history.get(nextHistoryIndex - 2).getCopy();
		nextHistoryIndex--;

		shouldSaveContent = true;
		updateAtlas();
	}

	public void redo() {
		if(nextHistoryIndex == history.size()) return;

		preview = history.get(nextHistoryIndex).getCopy();
		nextHistoryIndex++;

		shouldSaveContent = true;
		updateAtlas();
	}

}
