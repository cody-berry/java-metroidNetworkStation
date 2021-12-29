import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class DialogBox {
	String[] textList;
	ArrayList<int[][]> highlightIndices;
	int[] msPerPassage;
	PImage textFrame;
	PApplet app;
	int currentIndex;
	// the current advancing character
	int currentChar;
	// the last time we have advanced
	int lastAdvanced;

	DialogBox(@NotNull PApplet app, String[] textList,
			  ArrayList<int[][]> highlightIndices,
			  int[] msPerPassage, PImage textFrame) {
		this.textList = textList;
		this.highlightIndices = highlightIndices;
		this.msPerPassage = msPerPassage;
		this.textFrame = textFrame;
		this.app = app;
		currentIndex = 0;
		currentChar = 0;
		lastAdvanced = app.millis();
	}

	// draw our text frame
	public void draw2DTextFrame() {
		app.image(textFrame, 0, 0, app.width, app.height);
	}

	// draws our text
	public void render() {
		// our current passage
		String currentPassage = this.textList[this.currentIndex];
		// our margins
		int leftMargin = 70;
		int topMargin = 280;
		// our positions
		int x = leftMargin;
		int y = topMargin;
		for (int i = 0; i < currentChar; i++) {
			// get our current character
			char c = currentPassage.charAt(i);
			// if i is one of the indices of one of our highlight tuples...
			for (int[] h : highlightIndices.get(currentIndex)) {
				// ...for start, we fill with yellow...
				if (i == h[0]) {
					app.fill(63, 60, 75);
				}
				// ...and finally, for end, we fill with white.
				if (i == h[1]) {
					app.fill(0, 0, 100);
				}
			}
			// let's do word wrap!
			boolean wrap = false;
			// if our current character is a space...
			if (c == ' ') {
				// ...we should find the rest of the passage...
				String restOfPassage = currentPassage.substring(i+1);
				// ...the next delimiter index...
				int nextDelimiterIndex = restOfPassage.indexOf(' ') + i+1;
				// ...our current word..
				String currentWord = currentPassage.substring(i,
						nextDelimiterIndex);
				// ...the text width of the current word...
				float textWidth = app.textWidth(currentWord);
				// ...and finally, if x plus the text width of the current
				// word is equal to an  x wrap defined below...
				int x_wrap = app.width - leftMargin;
				if (x + textWidth > x_wrap) {
					wrap = true;
				}
			}
			// draw our text
			// wait, but if the character is a space, we should just increase
			// our textwidth because then the space will be way too large!
			if (c != ' ') {
				app.text(c, x, y);
				x += app.textWidth(c);
			} else {

				x += 5;
			}
			if (wrap) {
				x = leftMargin;
				y += app.textAscent() + app.textDescent() + 6;
			}
		}
		// if the current character is not already done, we increment it
		if (currentChar < currentPassage.length()) {
			currentChar++;
		}
		// if it has been long enough since we last advanced, we should
		// advance now
		int advance = msPerPassage[currentIndex];
		if (app.millis() > advance + lastAdvanced) {
			currentIndex++;
			currentChar = 0;
			lastAdvanced = app.millis();
		}
		app.fill(185, 30, 100);
		// we need to draw our ADAM text.
		app.text("ADAM", leftMargin-10, topMargin-25);
		// and we reset our fill.
		app.fill(0, 0, 100);
	}
}
