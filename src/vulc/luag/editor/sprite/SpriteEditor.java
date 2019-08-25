package vulc.luag.editor.sprite;

import java.util.ArrayList;
import java.util.List;

import vulc.bitmap.Bitmap;
import vulc.luag.Console;
import vulc.luag.editor.Editor;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.gui.GUIComponent;
import vulc.luag.gfx.gui.GUIPanel;
import vulc.luag.gfx.gui.GUITextBox;
import vulc.luag.gfx.panel.EditorPanel;

public class SpriteEditor extends Editor {

	private final Bitmap atlas;
	private Bitmap preview;
	private final int previewScale = 6;
	private int spriteID = 0;
	private int scope = 1;
	private int atlasOffset = 0;

	private int selectedColor = 0xffffff;
	private final List<Integer> lastColors = new ArrayList<Integer>();
	private final int historyColors = 8;

	private final GUITextBox selectColorTxt;

	public SpriteEditor(Console console, EditorPanel panel, int x, int y, int w, int h) {
		super(console, panel, x, y, w, h);
		this.atlas = panel.game.atlas;

		preview = panel.game.getSprite(spriteID, scope, scope);

		// default palette
		lastColors.add(0);
		lastColors.add(0);
		lastColors.add(0);
		lastColors.add(0);
		lastColors.add(0);
		lastColors.add(0);
		lastColors.add(0);
		lastColors.add(0);

		// INTERFACE
		guiPanel.background = 0x000000;

		int previewSize = 8 * previewScale;
		GUIComponent sprPreview = new GUIComponent((guiPanel.w - previewSize) / 2, 5,
		                                           previewSize, previewSize) {
			public void render(Screen screen) {
				screen.draw(preview.getScaled(previewScale),
				            x, y);
			}

			public void press(int x, int y) {
				int xp = x / previewScale;
				int yp = y / previewScale;

				preview.setPixel(xp, yp, selectedColor);
				// TODO undo-redo: make history saving
			}
		};
		guiPanel.add(sprPreview);

		int hAtlasPreview = 8 * 8;
		GUIComponent atlasPreview = new GUIComponent((guiPanel.w - atlas.width) / 2, guiPanel.h - hAtlasPreview - 5,
		                                             atlas.width, hAtlasPreview) {
			public void render(Screen screen) {
				screen.draw(atlas.getSubimage(0, atlasOffset * 8, atlas.width, hAtlasPreview), x, y);
			}
		};
		guiPanel.add(atlasPreview);

		// TODO atlas scroll arrows

		// TODO actionbar's items
		int hActionbar = 9 * 5 + 1;
		GUIPanel actionbar = new GUIPanel(atlasPreview.x, sprPreview.y + (sprPreview.h - hActionbar) / 2,
		                                  10, hActionbar);
		{
			actionbar.background = panel.primaryColor;
		}
		guiPanel.add(actionbar);

		// TODO colorbar
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

						public void press(int x, int y) {
							selectColor(lastColors.get(id));
						}
					};
					history.add(comp);
				}
			}
			colorbar.add(history);
		}
		guiPanel.add(colorbar);
	}

	public String getTitle() {
		return "Sprite Editor";
	}

	private void selectColor(int color) {
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

}
