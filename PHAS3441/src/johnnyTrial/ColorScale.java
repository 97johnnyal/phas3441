package johnnyTrial;

import java.awt.Color;
import java.util.ArrayList;

public class ColorScale {

	double max;
	double min;
	double zero;
	int resolution;
	ArrayList<ColorValue> scale;

	public ColorScale(double max, double min, double zero, int resolution) {
		this.max = max;
		this.min = min;
		this.zero = zero;
		this.resolution = resolution;
		ArrayList<ColorValue> scale = new ArrayList<ColorValue>();
		// adds for negative values
		for (int i = 0; i < resolution; i++) {
			double thisValue = min + i*((zero-min)/resolution);
			Color thisColor = new Color(0,0,255,i*100/resolution);
			scale.add(new ColorValue(thisValue, thisColor));
		}
		for (int i = 0; i < resolution; i++) {
			double thisValue = max + i*((max-zero)/resolution);
			Color thisColor = new Color(255,0,0,i*100/resolution);
			scale.add(new ColorValue(thisValue, thisColor));		
		}
		this.scale = scale;
	}

	public Color getColour(double z) {
		for (int i = 0; i < this.scale.size(); i++) {
			if (this.scale.get(i).getValue() <= z && z < this.scale.get(i+1).getValue()) {
				return this.scale.get(i).getColour();
			}
		}
		return this.scale.get(this.scale.size()-1).getColour();


	}

}