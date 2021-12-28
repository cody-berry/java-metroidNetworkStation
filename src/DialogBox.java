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

	DialogBox(PApplet app, String[] textList,
			  ArrayList<int[][]> highlightIndices,
			  int[] msPerPassage, PImage textFrame) {
		this.textList = textList;
		this.highlightIndices = highlightIndices;
		this.msPerPassage = msPerPassage;
		this.textFrame = textFrame;
		this.app = app;
		this.currentIndex = 0;
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
		int topMargin = 260;
		// our positions
		int x = leftMargin;
		int y = topMargin;
		for (int i = 0; i < currentPassage.length(); i++) {
			char c = currentPassage.charAt(i);
			app.textSize(14);
			app.text(c, x, y);
			x += app.textWidth(c);
		}
	}
}
