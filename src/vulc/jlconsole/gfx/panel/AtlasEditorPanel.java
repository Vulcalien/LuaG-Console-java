package vulc.jlconsole.gfx.panel;

import vulc.jlconsole.Console;
import vulc.jlconsole.atlas_editor.AtlasEditor;

public class AtlasEditorPanel extends BootablePanel {

	public final AtlasEditor editor = new AtlasEditor();

	public AtlasEditorPanel(Console console) {
		super(console);
	}

	public void preInit() {
		editor.init();
	}

	public void tick() {
		editor.tick();
	}

}
