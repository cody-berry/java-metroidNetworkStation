import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class DialogBox {
	String[] textList;
	ArrayList<int[][]> highlightIndices;
	int[] msPerPassage;
	PImage textFrame;
	PApplet app;

	DialogBox(PApplet app, String[] textList,
			  ArrayList<int[][]> highlightIndices,
			  int[] msPerPassage, PImage textFrame) {
		this.textList = textList;
		this.highlightIndices = highlightIndices;
		this.msPerPassage = msPerPassage;
		this.textFrame = textFrame;
		this.app = app;
	}

	// draw our text frame
	public void draw2DTextFrame() {
		app.image(textFrame, 0, 0, app.width, app.height);
	}
}
