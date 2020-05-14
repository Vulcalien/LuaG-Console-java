package vulc.luag.editor.gui;

import vulc.luag.game.Game;
import vulc.luag.gfx.Icons;
import vulc.luag.gfx.gui.GUIPanel;

public class AtlasPreview extends GUIPanel {

	private final Game game;

	private int animationTicks = 0;
	protected int atlasOffset = 0;
	protected int selected = 0;
	public int scope = 1;

	public AtlasPreview(int x, int y, int w, int h, Game game) {
		super(x, y, w, h);
		this.game = game;
	}

	public void tick() {
		animationTicks++;
	}

	public void drawComponents() {
		screen.draw(game.atlas.getSubimage(0, atlasOffset * Game.SPR_SIZE, w, h), 0, 0);

		int xSpr = selected % 16;
		int ySpr = ((selected / 16) - atlasOffset);

		int transparency = animationTicks / 50 % 2 == 0 ? 0xaa : 0xdd;

		screen.drawBool(Icons.SELECTED.getScaled(scope), 0xffffff, transparency,
		                xSpr * Game.SPR_SIZE,
		                ySpr * Game.SPR_SIZE);
	}

	public void onMouseDown(int xMouse, int yMouse) {
		int xs = xMouse / Game.SPR_SIZE;
		int ys = yMouse / Game.SPR_SIZE + atlasOffset;

		setSelected(xs, ys);
	}

	public void onMouseScroll(int xMouse, int yMouse, int count) {
		int newOffset = atlasOffset + count;
		if(newOffset >= 0 && newOffset + h / Game.SPR_SIZE <= 16) {
			atlasOffset = newOffset;
		}
	}

	public void setSelected(int xs, int ys) {
		if(xs + scope > 16) xs = 16 - scope;
		if(ys + scope > 16) ys = 16 - scope;

		selected = xs + ys * 16; // 16 = atlas.width (in sprites)
	}

	public void setScope(int scope) {
		this.scope = scope;

		// check if the selected area is out of bounds
		int xs = selected % 16;
		int ys = selected / 16;

		setSelected(xs, ys);
	}

}
