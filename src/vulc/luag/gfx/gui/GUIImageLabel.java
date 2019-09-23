package vulc.luag.gfx.gui;

import vulc.bitmap.Bitmap;
import vulc.luag.gfx.Screen;

public class GUIImageLabel extends GUIComponent {

	public Bitmap<Boolean> boolImage;
	public int colorAsBool;

	public Bitmap<Integer> image;

	public GUIImageLabel(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	public void setImage(Bitmap<Boolean> image, int color) {
		this.boolImage = image;
		this.colorAsBool = color;

		this.image = null;
	}

	public void setImage(Bitmap<Integer> image) {
		this.image = image;

		this.boolImage = null;
	}

	public void render(Screen screen) {
		super.render(screen);
		if(boolImage != null) {
			screen.drawBool(boolImage, colorAsBool,
			                x + (w - boolImage.width) / 2,
			                y + (h - boolImage.height) / 2);
		} else if(image != null) {
			screen.draw(image,
			            x + (w - image.width) / 2,
			            y + (h - image.height) / 2);
		}
	}

}
