package g_testing;

import org.apache.commons.math3.special.BesselJ;

public class OilDrop {
	//initiate variable
	double frequency;
	TwoVector currentPosition;
	TwoVector prevPosition;
	TwoVector velocity = new TwoVector(0,0);
	TwoVector displacement = new TwoVector(750,0);
	double gamma = 0.75;
	final double waveVelocity = 1000;
	
	//reset variables
	double frequencyReset;
	final TwoVector currentPositionReset;
	final TwoVector prevPositionReset;
	final TwoVector velocityReset = new TwoVector(0,0);
	final TwoVector displacementReset = new TwoVector(0,0);
	final double gammaReset = 1;
	
	
	//constructor
	public OilDrop(double frequency, TwoVector position) {
		this.frequency = frequency;
		this.currentPosition = position;
		this.prevPosition = position;
		this.frequencyReset = frequency;
		this.currentPositionReset = position;
		this.prevPositionReset = position;
	}
	
	//contribution to intensity at a pixel;
	public double contribution(Pixel p, double time) {
		/*TwoVector displacementFrmDrop = p.position.subtract(this.position);
		double dist = displacementFrmDrop.magnitude()*0.5;
		BesselJ bessel = new BesselJ(0);
		double amplitude = -Math.cos(frequency*time)*bessel.value(this.frequency * dist / waveVelocity);*/
		TwoVector displacementFrmDrop = p.position.subtract(this.currentPosition);
		double dist = displacementFrmDrop.magnitude();
		double angle = 0;
		if (this.displacement.equalsTo(new TwoVector(0,0)) || displacementFrmDrop.equalsTo(new TwoVector(0,0) )){
			angle = 0;
		}
		else {
			try {
				angle = TwoVector.angle(this.displacement,displacementFrmDrop);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		double xPrimePrime = this.gamma*this.gamma * Math.cos(angle) * dist;
		double yPrimePrime = this.gamma * Math.sin(angle)*dist;
		double rPrimePrime = Math.sqrt((yPrimePrime*yPrimePrime) + (xPrimePrime * xPrimePrime));
		BesselJ bessel = new BesselJ(0);
		double amplitude = -Math.cos(this.frequency*time
								- (Math.pow(gamma/waveVelocity, 2)*this.frequency*this.velocity.magnitude()*this.displacement.magnitude()))
								* bessel.value(this.frequency*rPrimePrime/waveVelocity);
			
		
		return amplitude;
	}
	
	//probability at a vector point;
	public double pdAtVector(TwoVector p, double time) {
		/*TwoVector displacementFrmDrop = p.subtract(this.position);
		double dist = displacementFrmDrop.magnitude();
		BesselJ bessel = new BesselJ(0);
		double amplitude = -Math.cos(frequency*time)*bessel.value(this.frequency * dist / waveVelocity);*/
		TwoVector displacementFrmDrop = p.subtract(this.currentPosition);
		double dist = displacementFrmDrop.magnitude();
		double angle = 0;
		if (this.displacement.equalsTo(new TwoVector(0,0)) || displacementFrmDrop.equalsTo(new TwoVector(0,0) )){
			angle = 0;
		}
		else {
			try {
				angle = TwoVector.angle(this.displacement,displacementFrmDrop);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		double xPrimePrime = this.gamma*this.gamma * Math.cos(angle) * dist;
		double yPrimePrime = this.gamma * Math.sin(angle)*dist;
		double rPrimePrime = Math.sqrt((yPrimePrime*yPrimePrime) + (xPrimePrime * xPrimePrime));
		BesselJ bessel = new BesselJ(0);
		double amplitude = -Math.cos(this.frequency*time
								- (Math.pow(gamma/waveVelocity, 2)*this.frequency*this.velocity.magnitude()*this.displacement.magnitude()))
								* bessel.value(this.frequency*rPrimePrime/waveVelocity);
		return (amplitude*amplitude);
	}
	
	//update details
	public void updateDet(TwoVector newPosition) {
		this.prevPosition = this.currentPosition;
		this.currentPosition = newPosition;
		this.displacement = this.currentPosition.subtract(this.prevPosition);
		this.velocity = this.displacement.multiply(this.frequency/(2*Math.PI));
		System.out.println(this.velocity.magnitude()/this.waveVelocity);
		this.gamma = 1/(Math.sqrt(1-Math.pow(this.velocity.magnitude()/waveVelocity, 2)));
	}
	
	//resets oil droplet
	public void reset() {
		this.frequency=this.frequencyReset;
		this.currentPosition=this.currentPositionReset;
		this.prevPosition=this.prevPositionReset;
		this.velocity = this.velocityReset;
		this.displacement = this.displacementReset;
		this.gamma = this.gammaReset;
	}
	
	
}
