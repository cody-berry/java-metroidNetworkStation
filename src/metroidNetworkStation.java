import peasy.PeasyCam;
import processing.core.PApplet;
import processing.data.JSONArray;


/**
 * Thanks to our hero Abe Pazos at https://vimeo.com/channels/p5idea, who
 * teaches us how to use Processing inside IDEA
 */
public class metroidNetworkStation extends PApplet {
	// our list of passages
	String[] textList = new String[6];
	// our highlight list, keeping track of which indices to highlight
	int[][] highlightList;
	// a list keeping track of the milliseconds per each passage
	int[] msPerPassage;
	// our usual camera for 3D visualizations
	PeasyCam cam;
	public static void main(String[] args) {
		PApplet.main(new String[]{metroidNetworkStation.class.getName()});
	}

	@Override
	public void settings() {
		size(640, 360);
	}

	@Override
	public void setup() {
		rectMode(RADIUS);
		colorMode(HSB, 360f, 100f, 100f, 100f);

		// this way, we come to the outer json variable, set it in the
		// function, and make the way back out.
		JSONArray json = null;
		loadData(json);
	}

	public void loadData(JSONArray json) {
		json = loadJSONArray("passages.json");
//		System.out.println(json);
		for (int i = 0; i < json.size(); i++) {
			textList[i] = json.getJSONObject(i).getString("text");
			System.out.println(json.getJSONObject(i).getString("text"));
		}
	}

	@Override
	public void draw() {
		background(210, 100, 30, 100);
	}

	@Override
	public void mousePressed() {
		System.out.println(mouseX);
	}
}