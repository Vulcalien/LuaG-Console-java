package vulc.luag.gfx.gui;

public class GUIButton extends GUILabel {

	public Runnable action = null;

	public GUIButton(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	public void press(int x, int y) {
		if(action != null) action.run();
	}

}
