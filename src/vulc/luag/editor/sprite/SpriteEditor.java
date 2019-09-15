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
import vulc.luag.editor.sprite.gui.SpritePreview;
import vulc.luag.editor.sprite.gui.SpriteToolbar;
import vulc.luag.editor.sprite.tool.SpriteToolkit;
import vulc.luag.game.Game;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;
import vulc.luag.gfx.gui.GUIPanel;
import vulc.luag.gfx.gui.GUITextBox;
import vulc.luag.gfx.panel.EditorPanel;

public class SpriteEditor extends Editor {

	// preview and atlas
	public final Bitmap atlas;
	public Bitmap preview;
	public final int previewScale = 6;
	public int spriteID = 0;
	public int scope = 1;
	public int atlasOffset = 0;

	public final SpriteToolkit toolkit = new SpriteToolkit(this);

	// select color and color history
	public int selectedColor = 0xffffff;
	public final List<Integer> lastColors = new ArrayList<Integer>();
	public final int historyColors = 8;

	// editing history
	public final int historySize = 100;
	public final List<Bitmap> history = new ArrayList<Bitmap>();
	public boolean isEditing = false, wasEditing = false;
	public boolean shouldSaveContent = false;

	public final GUITextBox selectColorTxt;

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

		// INTERFACE
		guiPanel.background = 0x000000;

		int previewSize = 8 * previewScale;
		GUIComponent sprPreview = new SpritePreview((guiPanel.w - previewSize) / 2, 5,
		                                            previewSize, previewSize,
		                                            this);
		guiPanel.add(sprPreview);

		int verticalTiles = 8;
		int hAtlasPreview = 8 * verticalTiles;
		GUIComponent atlasPreview = new GUIComponent((guiPanel.w - atlas.width) / 2, guiPanel.h - hAtlasPreview - 5,
		                                             atlas.width, hAtlasPreview) {
			public void render(Screen screen) {
				screen.draw(atlas.getSubimage(0, atlasOffset * 8, atlas.width, hAtlasPreview), x, y);
			}

			public void onPress(int xMouse, int yMouse) {
				int xs = xMouse / 8;
				int ys = yMouse / 8 + atlasOffset;

				int id = xs + ys * 16; // 16 = atlas.width (in sprites)
				spriteID = id;

				preview = editorPanel.game.getSprite(id, scope, scope);
			}

			public void onMouseScroll(int xMouse, int yMouse, int count) {
				System.out.println("s");
				int newOffset = atlasOffset + count;
				if(newOffset >= 0 && newOffset + verticalTiles <= 16) {
					atlasOffset = newOffset;
				}
				System.out.println(newOffset + verticalTiles);
			}
		};
		guiPanel.add(atlasPreview);

		int hToolbar = 9 * 5 + 1;
		GUIPanel toolbar = new SpriteToolbar(atlasPreview.x, sprPreview.y + (sprPreview.h - hToolbar) / 2,
		                                     10, hToolbar,
		                                     this);
		guiPanel.add(toolbar);

		int xColorbar = sprPreview.x + sprPreview.w + 5;
		GUIPanel colorbar = new GUIPanel(xColorbar, 5,
		                                 guiPanel.w - xColorbar - 5, sprPreview.h);
		{
			colorbar.background = panel.primaryColor;

			GUIComponent colorPreview = new GUIComponent((colorbar.w - 16) / 2, 1, 16, 16) {
				public void render(Screen screen) {
					screen.fill(x, y, x + w - 1, y + h - 1, selectedColor);
				}
			};
			colorbar.add(colorPreview);

			selectColorTxt = new GUITextBox(1, 18, colorbar.w - 2, 10) {
				public void onEnterPress() {
					super.onEnterPress();
					selectColor(Integer.parseInt(text, 16));
				}
			};
			selectColorTxt.opaque = true;
			selectColorTxt.background = 0xffffff;
			selectColorTxt.textColor = 0x000000;
			selectColorTxt.nChars = 6;
			selectColorTxt.acceptedText = GUITextBox.HEX_ONLY;
			colorbar.add(selectColorTxt);

			int historyColumns = 4;
			int hHistory = 9 * (historyColors / historyColumns) + 1;
			int wHistory = 9 * historyColumns + 1;
			GUIPanel history = new GUIPanel((colorbar.w - wHistory) / 2, colorbar.h - hHistory - 1,
			                                wHistory, hHistory);
			{
				history.background = panel.secondaryColor;

				for(int i = 0; i < historyColors; i++) {
					int id = i;
					int xt = (id % historyColumns);
					int yt = (id / historyColumns);

					GUIComponent comp = new GUIComponent(1 + xt * 9, 1 + yt * 9, 8, 8) {
						public void render(Screen screen) {
							screen.fill(x, y, x + w - 1, y + h - 1, lastColors.get(id));
						}

						public void onPress(int xMouse, int yMouse) {
							selectColor(lastColors.get(id));
						}
					};
					history.add(comp);
				}
			}
			colorbar.add(history);
		}
		guiPanel.add(colorbar);

		toolkit.setTool(toolkit.pencil);
	}

	public void tick() {
		boolean shouldSaveHistory = wasEditing && !isEditing;
		wasEditing = isEditing;
		isEditing = false;

		if(shouldSaveHistory) {
			System.out.println("saving");
			history.add(preview.getScaled(1)); // clones the img
			if(history.size() > historySize) {
				history.remove(0);
			}
			atlas.draw(preview, (spriteID % 16) * 8, (spriteID / 16) * 8);
		}
	}

	public String getTitle() {
		return "Sprite Editor";
	}

	public void selectColor(int color) {
		if(!lastColors.contains(selectedColor)) {
			lastColors.add(0, selectedColor);
			lastColors.remove(historyColors);
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
			BufferedImage img = new BufferedImage(atlas.width, atlas.height, BufferedImage.TYPE_INT_RGB);
			img.setRGB(0, 0, atlas.width, atlas.height, atlas.pixels, 0, atlas.width);

			ImageIO.write(img, "png", new File(Game.ATLAS_FILE));
			shouldSaveContent = false;
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
