package vulc.luag.shell;

import java.util.ArrayList;
import java.util.List;

import vulc.bitmap.font.Font;
import vulc.luag.Console;
import vulc.luag.gfx.Screen;
import vulc.luag.gfx.panel.ShellPanel;
import vulc.luag.shell.command.ShellCommand;

public abstract class Shell {

	public static final int BACKGROUND = 0x000000;
	public static final int DEFAULT_FOREGROUND = 0xffffff;
	public static final int USER_FOREGROUND = 0x00ff00;

	public static final int HORIZONTAL_CHARS = Console.WIDTH / (Screen.FONT.widthOf(' ') + 1);
	public static final int VERTICAL_LINES = Console.HEIGHT / (Screen.FONT.getHeight() + 1);

	private static final int ANIMATION_DELAY = 25;

	public static final List<ShellChar> CONSOLE_BUFFER = new ArrayList<ShellChar>();	// chars written by the console
	public static final List<ShellChar> USER_BUFFER = new ArrayList<ShellChar>();		// chars written by the user

	public static int scrollBuffer = 0;
	public static boolean pressedUP = false, pressedLEFT = false, pressedDOWN = false, pressedRIGHT = false;

	private static final List<ShellRow> CLOSED_ROWS = new ArrayList<ShellRow>();
	private static ShellRow currentLine = new ShellRow();
	private static int currentChar = 0;

	private static final List<ShellRow> COMMAND_HISTORY = new ArrayList<ShellRow>();
	private static int historyPoint = 0;

	private static int renderOffset = 0;
	private static int animationTicks = 0; // cursor animation

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
		if(CONSOLE_BUFFER.size() != 0) {
			ShellChar c = CONSOLE_BUFFER.remove(0);
			receiveInput(c, false);

			animationTicks = ANIMATION_DELAY;
		} else if(USER_BUFFER.size() != 0) {
			ShellChar c = USER_BUFFER.remove(0);
			receiveInput(c, true);

			animationTicks = 0;
		} else {
			animationTicks++;
		}

		// history
		byte historyShift = 0;
		if(pressedUP) {
			pressedUP = false;
			historyShift++;
		}
		if(pressedDOWN) {
			pressedDOWN = false;
			historyShift--;
		}

		if(historyShift != 0) {
			int newHistoryPoint = historyPoint + historyShift;

			if(newHistoryPoint >= 0 && newHistoryPoint < COMMAND_HISTORY.size()) {
				historyPoint = newHistoryPoint;

				// most recent command is 0
				currentLine = COMMAND_HISTORY.get(historyPoint).copy();
				currentChar = currentLine.size();
			}
		}

		if(scrollBuffer != 0) {
			int newOffset = renderOffset + scrollBuffer;

			// if is under the bottom then move back to bottom
			int lowestOffset = lowestOffset();
			if(newOffset > lowestOffset) {
				newOffset = lowestOffset;
			}

			// this may be caused by the previous if block
			if(newOffset < 0) newOffset = 0;

			renderOffset = newOffset;
			scrollBuffer = 0;
		}

		if(pressedLEFT) {
			pressedLEFT = false;
			if(currentChar > 0) {
				if(panel.ctrl.isKeyDown()) {
					currentChar = leftWordPosition();
				} else {
					currentChar--;
				}
			}
		}
		if(pressedRIGHT) {
			pressedRIGHT = false;
			if(currentChar < currentLine.size()) {
				if(panel.ctrl.isKeyDown()) {
					currentChar = rightWordPosition();
				} else {
					currentChar++;
				}
			}
		}
		render();
	}

	private static void render() {
		Console.SCREEN.clear(BACKGROUND);

		int margin = 1;
		int yRender = margin;

		for(int i = 0; i < VERTICAL_LINES; i++) {
			int line = i + renderOffset;

			if(line < CLOSED_ROWS.size()) {
				CLOSED_ROWS.get(line).render(Console.SCREEN, margin, yRender);
				yRender += Screen.FONT.getHeight() + Screen.FONT.getLineSpacing();
			} else {
				ShellRow[] splitCurrent = splitCurrentLine(currentLine);
				for(int a = 0; a < splitCurrent.length; a++) {
					splitCurrent[a].render(Console.SCREEN, margin, yRender);
					yRender += Screen.FONT.getHeight() + Screen.FONT.getLineSpacing();
				}
				break;
			}
		}

		// draw the cursor
		if(animationTicks / ANIMATION_DELAY % 2 == 0) {
			Font font = Screen.FONT;
			Console.SCREEN.write("_", DEFAULT_FOREGROUND,
			                     1 + (font.widthOf(' ') + font.getLetterSpacing())
			                         * (currentChar % HORIZONTAL_CHARS),
			                     1 + (font.getHeight() + font.getLineSpacing())
			                         * (currentChar / HORIZONTAL_CHARS + CLOSED_ROWS.size() - renderOffset));
		}
	}

	public static void receiveInput(ShellChar character, boolean shouldExecute) {
		int lowestOffset;
		switch(character.value) {
			case '\n':
				if(shouldExecute) execute(currentLine);

				ShellRow[] splitCurrent = splitCurrentLine(currentLine);
				for(int i = 0; i < splitCurrent.length; i++) {
					// here the text always ends with '\n'
					CLOSED_ROWS.add(splitCurrent[i]);
				}
				currentLine.clear();
				currentChar = 0;

				// if is not at bottom then move to bottom
				lowestOffset = lowestOffset();
				if(renderOffset < lowestOffset) {
					renderOffset = lowestOffset;
				}
				break;

			case '\b':
				if(currentLine.size() > 0 && currentChar > 0) {
					if(panel.ctrl.isKeyDown()) {
						int oldLength = currentLine.size();
						currentLine = currentLine.subrow(0, leftWordPosition())
						                         .join(currentLine.subrow(currentChar, currentLine.size()));

						currentChar -= oldLength - currentLine.size();
					} else {
						currentLine = currentLine.subrow(0, currentChar - 1)
						                         .join(currentLine.subrow(currentChar, currentLine.size()));
						currentChar--;
					}
				}
				break;

			case 127:
				if(currentLine.size() > 0 && currentChar != currentLine.size()) {
					if(panel.ctrl.isKeyDown()) {
						currentLine = currentLine.subrow(0, currentChar)
						                         .join(currentLine.subrow(rightWordPosition(), currentLine.size()));
					} else {
						currentLine = currentLine.subrow(0, currentChar)
						                         .join(currentLine.subrow(currentChar + 1, currentLine.size()));
					}
				}
				break;

			default:
				if(character.value >= 32 && character.value < 127) {
					currentLine = currentLine.subrow(0, currentChar)
					                         .join(character)
					                         .join(currentLine.subrow(currentChar, currentLine.size()));
					currentChar++;

					// if is not at bottom then move to bottom
					lowestOffset = lowestOffset();
					if(renderOffset < lowestOffset) {
						renderOffset = lowestOffset;
					}
				}
				break;
		}
	}

	public static void write(String text, int color) {
		for(int i = 0; i < text.length(); i++) {
			CONSOLE_BUFFER.add(new ShellChar(text.charAt(i), color));
		}
	}

	public static void write(String text) {
		write(text, DEFAULT_FOREGROUND);
	}

	public static void clear() {
		CLOSED_ROWS.clear();
		currentLine.clear();
		renderOffset = 0;
	}

	public static void execute(ShellRow row) {
		COMMAND_HISTORY.add(0, row.copy());
		historyPoint = -1;

		String line = row.toString();
		line = line.trim().replaceAll(" +", " ");
		if(!ShellCommand.execute(line)) {
			write("unknown command\n\n");
		}
	}

	private static ShellRow[] splitCurrentLine(ShellRow line) {
		int height = (line.size() - 1) / (Shell.HORIZONTAL_CHARS) + 1;

		ShellRow[] splitLine = new ShellRow[height];
		for(int i = 0; i < height; i++) {
			int start = i * Shell.HORIZONTAL_CHARS;

			int end = start;
			if(i != height - 1) {
				end += Shell.HORIZONTAL_CHARS;
			} else {
				// if length == HORIZONTAL_CHARS the % will be 0
				if(line.size() != 0
				   && line.size() % Shell.HORIZONTAL_CHARS == 0) {
					end += Shell.HORIZONTAL_CHARS;
				} else {
					end += line.size() % (Shell.HORIZONTAL_CHARS);
				}
			}
			splitLine[i] = line.subrow(start, end);
		}
		return splitLine;
	}

	private static int lowestOffset() {
		int currentLineHeight = currentLine.size() / HORIZONTAL_CHARS + 1;
		return CLOSED_ROWS.size() - VERTICAL_LINES + currentLineHeight;
	}

	// find left word's position, used for ctrl+left and ctrl+'\b'
	private static int leftWordPosition() {
		int spacePosition = -1;
		boolean foundNonSpace = false;
		for(int i = currentChar - 1; i >= 0; i--) {
			if(currentLine.charAt(i) == ' ') {
				if(foundNonSpace) {
					spacePosition = i;
					break;
				}
			} else {
				foundNonSpace = true;
			}
		}
		return spacePosition + 1;
	}

	// find right word's position, used for ctrl+right and ctrl+del
	private static int rightWordPosition() {
		int nonSpacePosition = currentLine.size();
		boolean foundSpace = false;
		for(int i = currentChar; i < currentLine.size(); i++) {
			if(currentLine.charAt(i) != ' ') {
				if(foundSpace) {
					nonSpacePosition = i;
					break;
				}
			} else {
				foundSpace = true;
			}
		}
		return nonSpacePosition;
	}

}
