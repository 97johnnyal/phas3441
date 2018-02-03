package g_testing;

import java.util.Random;

public class Pixel {
	//Defining member variable
	TwoVector position;
	double amplitude;
	int colorIntensity;
	boolean reverse;
	int time=0;
	Random rand = new Random();
	double positionGuess;
	
	//Constructor
	public Pixel(TwoVector position, double amplitude) {
		this.position = position;
		this.amplitude = amplitude;
		this.colorIntensity = 0;
	}
	
	//testing
	public void WaveAnimate() {
		this.time = this.time + 1000;
		this.colorIntensity = (int) (Math.pow((Math.cos((2*Math.PI / 100000) * (this.amplitude + this.time))),2) * 255);
	}

	public void evoAmplitude(double amplitude) {
		this.amplitude = amplitude;
		this.colorIntensity = (int) (amplitude*amplitude * 255);
		
		//random position calculator?
		this.positionGuess = amplitude*amplitude*rand.nextDouble();
	}
	
	
	
}
