package vulc.luag.shell;

import java.util.ArrayList;
import java.util.List;

import vulc.luag.Console;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.panel.ShellPanel;
import vulc.luag.shell.command.ShellCommand;

public abstract class Shell {

	private static final int BACKGROUND = 0x000000;
	private static final int FOREGROUND = 0xffffff;

	private static final int RENDERED_LINES = Console.HEIGHT / (Screen.FONT.getHeight() + 1);

	public static final List<ShellChar> CHAR_BUFFER = new ArrayList<ShellChar>();
	public static int scrollBuffer = 0;

	private static final List<String> CLOSED_LINES = new ArrayList<String>();
	private static String currentLine = "";

	private static int renderOffset = 0;
	private static int animationTicks = 0; // the _ that appears and disappears

	public static ShellPanel panel;

	private Shell() {
	}

	public static void init() {
		write(Console.NAME + "\n");
		write(Console.COPYRIGHT + "\n");
		write("Version: " + Console.VERSION + "\n");
		write("\n");
	}

	public static void tick() {
		boolean writing = false;
		if(CHAR_BUFFER.size() != 0) {
			ShellChar character = CHAR_BUFFER.remove(0);
			receiveInput(character.val, character.writtenByUser);
			writing = true;
		} else {
			animationTicks++;
		}

		if(scrollBuffer != 0) {
			int newOffset = renderOffset + scrollBuffer;

			// if is under the bottom then move back to bottom
			if(newOffset > CLOSED_LINES.size() - RENDERED_LINES + 1) {
				newOffset = CLOSED_LINES.size() - RENDERED_LINES + 1;
			}

			// this may be caused by the previous if block
			if(newOffset < 0) newOffset = 0;

			renderOffset = newOffset;
			scrollBuffer = 0;
		}

		Console.SCREEN.clear(BACKGROUND);

		String textToRender = "";
		for(int i = 0; i < RENDERED_LINES; i++) {
			int line = i + renderOffset;
			if(line > CLOSED_LINES.size()) { // if line == closedLines.size() then the shell will render currentLine
				break;
			}

			String text;
			if(line < CLOSED_LINES.size()) {
				text = CLOSED_LINES.get(line);
			} else {
				text = currentLine;
				if(!writing && animationTicks / 25 % 2 == 1) {
					text += "_";
				}
			}
			textToRender += text + "\n";
		}
		Console.SCREEN.write(textToRender, FOREGROUND, 1, 1);
	}

	public static void receiveInput(char character, boolean shouldExecute) {
		switch(character) {
			case '\n':
				if(shouldExecute) execute(currentLine);

				CLOSED_LINES.add(currentLine);
				currentLine = "";

				// if is not at bottom then move to bottom
				if(renderOffset < CLOSED_LINES.size() - RENDERED_LINES + 1) {
					renderOffset = CLOSED_LINES.size() - RENDERED_LINES + 1;
				}
				break;

			case '\b':
				if(currentLine.length() > 0) {
					if(panel.ctrl.isKeyDown()) {
						int lastSpace = currentLine.lastIndexOf(' ');

						if(lastSpace == -1) currentLine = "";
						else currentLine = currentLine.substring(0, lastSpace);
					} else {
						currentLine = currentLine.substring(0, currentLine.length() - 1);
					}
				}
				break;

			default:
				if(character >= 32 && character < 127) {
					currentLine += character;
				}
				break;
		}
	}

	public static void write(String text) {
		for(int i = 0; i < text.length(); i++) {
			CHAR_BUFFER.add(new ShellChar(text.charAt(i), false));
		}
	}

	public static void clear() {
		CLOSED_LINES.clear();
		currentLine = "";
		renderOffset = 0;
	}

	public static void execute(String line) {
		line = line.trim();
		if(!ShellCommand.execute(line)) {
			write("unknown command\n\n");
		}
	}

}
