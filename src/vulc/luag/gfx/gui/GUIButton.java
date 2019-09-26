package vulc.luag.gfx.gui;

public class GUIButton extends GUILabel {

	public Runnable action = null;

	public GUIButton(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	public void onMouseDown(int xMouse, int yMouse) {
		if(action != null) action.run();
	}

}
