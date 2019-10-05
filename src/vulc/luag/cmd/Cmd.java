package vulc.luag.cmd;

import java.util.ArrayList;
import java.util.List;

import vulc.luag.Console;
import vulc.luag.cmd.command.CmdCommand;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.panel.CmdPanel;

public class Cmd {

	private final int background = 0x000000;
	private final int foreground = 0xffffff;

	private final int renderedLines = Console.HEIGHT / (Screen.FONT.getHeight() + 1) + 1;

	public final List<CmdChar> charBuffer = new ArrayList<CmdChar>();
	public int scrollBuffer = 0;

	private final List<String> closedLines = new ArrayList<String>();
	private String currentLine = "";

	private int renderOffset = 0;
	private int animationTicks = 0; // the _ that appears and disappears

	public Console console;
	public CmdPanel cmdPanel;

	public Cmd(Console console) {
		this.console = console;

		write(Console.NAME + "\n");
		write(Console.COPYRIGHT + "\n");
		write("Version: " + Console.VERSION + "\n");
		write("\n");
	}

	public void tick() {
		boolean writing = false;
		if(charBuffer.size() != 0) {
			CmdChar character = charBuffer.remove(0);
			receiveInput(character.val, character.writtenByUser);
			writing = true;
		} else {
			animationTicks++;
		}

		if(scrollBuffer != 0) {
			int newOffset = renderOffset + scrollBuffer;

			if(newOffset > closedLines.size() - renderedLines + 2) {
				newOffset = closedLines.size() - renderedLines + 2;
			}

			// this may be caused by the previous if block
			if(newOffset < 0) newOffset = 0;

			renderOffset = newOffset;
			scrollBuffer = 0;
		}

		console.screen.clear(background);

		String textToRender = "";
		for(int i = 0; i < renderedLines; i++) {
			int line = i + renderOffset;
			if(line > closedLines.size()) { // if line == closedLines.size() then the cmd will render currentLine
				break;
			}

			String text;
			if(line < closedLines.size()) {
				text = closedLines.get(line);
			} else {
				text = currentLine;
				if(!writing && animationTicks / 25 % 2 == 1) {
					text += "_";
				}
			}
			textToRender += text + "\n";
		}
		console.screen.write(textToRender, foreground, 1, 1);
	}

	public void receiveInput(char character, boolean shouldExecute) {
		switch(character) {
			case '\n':
				if(shouldExecute) execute(currentLine);
				closedLines.add(currentLine);
				currentLine = "";

				// Theory is when you know everything but nothing works.
				// Practice is when everything works but you don’t know why.
				// THIS IS PRACTICE
				if(renderOffset < closedLines.size() - renderedLines + 2) {
					renderOffset = closedLines.size() - renderedLines + 2;
				}
				break;

			case '\b':
				if(currentLine.length() > 0) {
					if(cmdPanel.ctrl.isKeyDown()) {
						int lastSpace = currentLine.lastIndexOf(' ');
						if(lastSpace == -1) {
							currentLine = "";
						} else {
							currentLine = currentLine.substring(0, lastSpace);
						}
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

	public void write(String text) {
		for(int i = 0; i < text.length(); i++) {
			charBuffer.add(new CmdChar(text.charAt(i), false));
		}
	}

	public void clear() {
		closedLines.clear();
		currentLine = "";
		renderOffset = 0;
	}

	public void execute(String line) {
		line = line.trim();
		if(!CmdCommand.execute(this, line)) {
			write("unknown command\n\n");
		}
	}

}
