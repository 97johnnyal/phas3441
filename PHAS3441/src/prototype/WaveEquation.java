package prototype;

public class WaveEquation {

	double speed;
	double initialHeight;

	/** Wave equation for the particle
	 * 
	 * @param speed wave velocity
	 * @param initialHeight initial wave height h0
	 */
	public WaveEquation(double speed, double initialHeight) {
		this.speed = speed;
		this.initialHeight = initialHeight;
	}

	public double getSpeed() {
		return speed;
	}


	public double getInitialHeight() {
		return initialHeight;
	}

}