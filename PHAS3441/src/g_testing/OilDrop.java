package g_testing;

import org.apache.commons.math3.special.BesselJ;

public class OilDrop {
	//initiate variable
	double frequency;
	TwoVector position;
	double velocity = 0;
	
	
	//constructor
	public OilDrop(double frequency, TwoVector position) {
		this.frequency = frequency;
		this.position = position;
	}
	
	//contribution to intensity at a pixel;
	public double contribution(Pixel p, double time) {
		TwoVector displacementFrmDrop = p.position.subtract(this.position);
		double dist = displacementFrmDrop.magnitude()*0.5;
		BesselJ bessel = new BesselJ(0);
		double amplitude = -Math.cos(frequency*time)*bessel.value(this.frequency * dist / 300);
		return amplitude;
	}
	
	//probability at a vector point;
	public double pdAtVector(TwoVector p, double time) {
		TwoVector displacementFrmDrop = p.subtract(this.position);
		double dist = displacementFrmDrop.magnitude();
		BesselJ bessel = new BesselJ(0);
		double amplitude = -Math.cos(frequency*time)*bessel.value(this.frequency * dist / 300);
		return (amplitude*amplitude);
	}
	
}
