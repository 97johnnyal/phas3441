package prototype;

import java.awt.Color;

public class ColorValue {
	
	double value;
	Color colour;
	public ColorValue(double value, Color colour) {
		this.value = value;
		this.colour = colour;
	}
	
	public double getValue() {
		return value;
	}
	public Color getColour() {
		return colour;
	}

}
