import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import java.util.ArrayList;

/*
	..........  dialog system
	**** spherical geometry
 */


/**
 * Thanks to our hero Abe Pazos at https://vimeo.com/channels/p5idea, who
 * teaches us how to use Processing inside IDEA
 */
public class metroidNetworkStation extends PApplet {
	// our font
	PFont font;
	// our list of passages
	String[] textList = new String[6];
	// our highlight list, keeping track of which indices to highlight
	ArrayList<int[][]> highlightList = new ArrayList<>();
	// a list keeping track of the milliseconds per each passage
	int[] msPerPassage = new int[6];
	// our usual camera for 3D visualizations
	PeasyCam cam;
	// our text frame
	PImage textFrame;
	// our dialog system
	DialogBox dialogBox;
	// setups for our hues, saturations, and brightnesses for our blender axis
	int red_hue = 0;
	int red_sat = 100;
	int blue_hue = 215;
	int blue_sat = 80;
	int green_hue = 90;
	int green_sat = 90;
	int dark = 50;
	int light = 80;
	// although the before was for dialog system...
	// ...we still need to do our spherical things.
	// let's set up our globe, our detail, and our radius.

	int detail = 32;
	int r = 100;
	PVector[][] globe = new PVector[detail+1][detail+1];


	public static void main(String[] args) {
		PApplet.main(new String[]{metroidNetworkStation.class.getName()});
	}

	@Override
	public void settings() {
		size(640, 360, P3D);
	}

	// let's setup our globe
	public void setupGlobe() {
		// our variables, longitude, latitude, x coordinate, y coordinate,
		// and z coordinate.
		double φ, θ;
		double x, y, z;

		// let's rest our stroke and fill.
		stroke(234, 34, 24);
		fill(234, 34, 24);

		// It's time to fill our 2D array with PVectors!
		for (int i = 0; i < globe.length; i++) {
			// let's define our longitude here!
			// φ ranges from 0 to TAU.
			φ = map(i, 0, globe.length-1, 0, PI);
			for (int j = 0; j < globe.length; j++) {
				// let's define our latitude here!
				// θ ranges from 0 to PI.
				θ = map(j, 0, globe[i].length-1, 0, PI);

				// now we can use our formulas to compute our x, y, and z
				// coordinates.
				x = r*sin((float) φ)*cos((float) θ);
				y = r*sin((float) φ)*sin((float) θ);
				z = r*cos((float) φ);

				// Now we can set our vectors to globe[i][j].
				globe[i][j] = new PVector((float) x, (float) y, (float) z);
			}
		}
	}

	@Override
	public void setup() {
		rectMode(RADIUS);
		colorMode(HSB, 360f, 100f, 100f, 100f);

		// this way, we come to the outer json variable, set it in the
		// function, and make the way back out.
		JSONArray json = null;
		loadData(json);
		setupGlobe();
	}

	public void loadData(JSONArray json) {
		// add peasycam
		cam = new PeasyCam(this, 0, 0, 0, 500);
		// our font
		font = createFont("gigamarujr.ttf", 14);
		textFont(font, 14);

		json = loadJSONArray("passages.json");
//		System.out.println(json);
		for (int i = 0; i < json.size(); i++) {
			textList[i] = json.getJSONObject(i).getString("text");
			msPerPassage[i] = json.getJSONObject(i).getInt("ms");
			int[][] lst = new int[json.getJSONObject(i).size()-1][2];
			for (int j = 0; j < json.getJSONObject(i).size(); j++) {
				JSONArray highlights = json.getJSONObject(i).getJSONArray(
						"highlightIndices");
				for (int k = 0; k < highlights.size(); k++) {
					lst[k][0] = highlights.getJSONObject(k).getInt("start");
					lst[k][1] = highlights.getJSONObject(k).getInt("end");
				}
			}
			highlightList.add(lst);
		}
/*
		for (int[][] h : highlightList) {
			System.out.println(Arrays.deepToString(h));
		}
*/
		textFrame = loadImage("textFrame.png");

		dialogBox = new DialogBox(this, textList, highlightList, msPerPassage,
				textFrame);


	}

	// after that big setup, now we need to display our globe
	public void displayGlobe() {
		for (int i = 0; i < globe.length-1; i++) {
			for (int j = 0; j < globe[i].length-1; j++) {
				// now we can draw a quadrilateral with these
				beginShape();
				PVector v1 = globe[i][j];
				PVector v2 = globe[i][j+1];
				PVector v3 = globe[i+1][j+1];
				PVector v4 = globe[i+1][j];
				// our faces need to be oscillating. We can add a little
				// offset for the oscillation.
				// our average
				PVector avg = new PVector(
						(v1.x + v2.x + v3.x + v4.x)/4,
						(v1.y + v2.y + v3.y + v4.y)/4,
						(v1.z + v2.z + v3.z + v4.z)/4);

				// our offset
				double offset = dist(0, 0, avg.x, avg.z);

				// and our scale factor.
				double psf = 1 + 0.05*sin((float) (frameCount/20 + offset));

				// let's multiply all of our vertices
				// wait, now. we can't. that'll modify our globe position.
//				v1.mult((float) psf);
//				v2.mult((float) psf);
//				v3.mult((float) psf);
//				v4.mult((float) psf);

				vertex((float) (v1.x*psf), (float) (v1.y*psf),
						(float) (v1.z*psf));
				vertex((float) (v2.x*psf), (float) (v2.y*psf),
						(float) (v2.z*psf));
				vertex((float) (v3.x*psf), (float) (v3.y*psf),
						(float) (v3.z*psf));
				vertex((float) (v4.x*psf), (float) (v4.y*psf),
						(float) (v4.z*psf));
				endShape(CLOSE);

				// we can draw our pyramids now
				// after filling with the right color
				fill(184, 57, 95);
				stroke(184, 57, 95);
				beginShape();
				vertex((float) (v1.x*psf), (float) (v1.y*psf),
						(float) (v1.z*psf));
				vertex(0, 0, 0);
				vertex((float) (v2.x*psf), (float) (v2.y*psf),
						(float) (v2.z*psf));

				endShape(CLOSE);
				beginShape();
				vertex((float) (v2.x*psf), (float) (v2.y*psf),
						(float) (v2.z*psf));

				vertex(0, 0, 0);
				vertex((float) (v3.x*psf), (float) (v3.y*psf),
						(float) (v3.z*psf));

				endShape(CLOSE);
				beginShape();
				vertex((float) (v3.x*psf), (float) (v3.y*psf),
						(float) (v3.z*psf));

				vertex(0, 0, 0);
				vertex((float) (v4.x*psf), (float) (v4.y*psf),
						(float) (v4.z*psf));

				endShape(CLOSE);
				beginShape();
				vertex((float) (v4.x*psf), (float) (v4.y*psf),
						(float) (v4.z*psf));

				vertex(0, 0, 0);
				vertex((float) (v1.x*psf), (float) (v1.y*psf),
						(float) (v1.z*psf));
				endShape(CLOSE);

				// and then we should reset our fill and stroke
				fill(210, 100, 20);
			}
		}
		// this doesn't display a circle at the back, though. we'll add that
		// in Adam.

	}

	@Override
	public void draw() {
		background(210, 100, 30, 100);

		// now that we've set up our globe, we can iterate through it to get
		// points
		displayGlobe();
		drawBlenderAxis();
		cam.beginHUD();
		fill(0, 0, 100);
		dialogBox.draw2DTextFrame();
		dialogBox.render();
		cam.endHUD();
	}

	// draws our blender axis
	public void drawBlenderAxis() {
		// x
		// pos
		stroke(red_hue, red_sat, light);
		line(10000, 0, 0, 0, 0, 0);
		// neg
		stroke(red_hue, red_sat, dark);
		line(-10000, 0, 0, 0, 0, 0);
		// y
		// pos
		stroke(green_hue, green_sat, light);
		line(0, 10000, 0, 0, 0, 0);
		// neg
		stroke(green_hue, green_sat, dark);
		line(0, -10000, 0, 0, 0, 0);
		// z
		// pos
		stroke(blue_hue, blue_sat, light);
		line(0, 0, 10000, 0, 0, 0);
		// neg
		stroke(blue_hue, blue_sat, dark);
		line(0, 0, -10000, 0, 0, 0);
	}
}