import peasy.PeasyCam;
import processing.core.PApplet;
import processing.data.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Thanks to our hero Abe Pazos at https://vimeo.com/channels/p5idea, who
 * teaches us how to use Processing inside IDEA
 */
public class metroidNetworkStation extends PApplet {
	// our list of passages
	String[] textList = new String[6];
	// our highlight list, keeping track of which indices to highlight
	ArrayList<int[][]> highlightList = new ArrayList<>();
	// a list keeping track of the milliseconds per each passage
	int[] msPerPassage = new int[6];
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
		for (int[][] h : highlightList) {
			System.out.println(Arrays.deepToString(h));
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