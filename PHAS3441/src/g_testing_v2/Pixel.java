package g_testing_v2;

import java.util.Random;

public class Pixel {
	//Defining member variable
	TwoVector position;
	double amplitude;
	int colorIntensityR;
	int colorIntensityB;
	boolean reverse;
	int time=0;
	Random rand = new Random();
	double positionGuess;
	
	//Constructor
	public Pixel(TwoVector position, double amplitude) {
		this.position = position;
		this.amplitude = amplitude;
		if (amplitude > 0) {
			this.colorIntensityR = (int) (Math.abs(amplitude) * 255);
			this.colorIntensityB = 0;
		}
		else {
			this.colorIntensityB = (int) (Math.abs(amplitude) * 255);
			this.colorIntensityR = 0;
		}
		
	}
	
	
}
