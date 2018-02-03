package g_testing_v2;

public class Pixel {
	//Defining member variable
	TwoVector position;
	double amplitude;
	int colorIntensity;
	boolean reverse;
	int time=0;
	
	//Constructor
	public Pixel(TwoVector position, double amplitude) {
		this.position = position;
		this.amplitude = amplitude;
		this.colorIntensity = 100;
	}
	
	public void WaveAnimate() {
		this.time = this.time + 1000;
		this.colorIntensity = (int) (Math.pow((Math.cos((2*Math.PI / 100000) * (this.amplitude + this.time))),2) * 255);
		
		
	}
	
}
