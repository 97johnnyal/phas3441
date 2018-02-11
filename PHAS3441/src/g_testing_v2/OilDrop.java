package g_testing_v2;

import java.util.ArrayList;

import org.apache.commons.math3.special.BesselJ;

public class OilDrop {
	//Initiate variables
	TwoVector currPos;
	TwoVector prevPos;
	TwoVector displacement; //vector from prev pos to curr pos
	TwoVector vel = new TwoVector (0,0);
	Double waveSpeed;
	double gamma;
	double frequency; // angular frequency in rad s-1
	double time = 0;
	double phase = 0; // phase between 0 to 1, where 0 = 0rad, 1 = 2pi
	double decay = 1;
	
	//Constructor
	public OilDrop(TwoVector currPos, Double waveSpeed, double frequency,double phase) {
		this.currPos = currPos;
		this.waveSpeed = waveSpeed;
		this.frequency = frequency;
		this.phase = phase;
	}
	
	//update oil drop position to it's new position
	public void updateParam (TwoVector dVel, double time) {
		this.vel = this.vel.add(dVel);
		this.prevPos = this.currPos;
		this.currPos = this.prevPos.add(vel.multiply(0.9/(2*Math.PI*this.frequency)));
		this.displacement = this.currPos.subtract(prevPos);
		this.gamma = 1/(Math.sqrt(1-Math.pow(this.vel.magnitude()/waveSpeed, 2)));
		this.time = time + (phase/frequency);
	}
	
	//determines field strength at a given TwoVector point
	public double strengthAtPt (TwoVector point) {
		point = point.multiply(1);
		TwoVector displacementFrmDrop = point.subtract(this.currPos);
		double dist = displacementFrmDrop.magnitude();
		//det angle between velocity and point
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
		
		//det amplitude contribution
		double xPrimePrime = this.gamma*this.gamma * Math.cos(angle) * dist;
		double yPrimePrime = this.gamma * Math.sin(angle)*dist;
		double rPrimePrime = Math.sqrt((yPrimePrime*yPrimePrime) + (xPrimePrime * xPrimePrime));
		BesselJ bessel = new BesselJ(0);
		double amplitude = -Math.cos(this.frequency*time
								- (Math.pow(gamma/this.waveSpeed, 2)*this.frequency*this.vel.magnitude()*this.displacement.magnitude()))
								* bessel.value(this.frequency*rPrimePrime/this.waveSpeed);
		
		return amplitude * this.decay;
	}
	
	//calculate the gradient at a given postion (pos)
	public static TwoVector gradient(ArrayList<OilDrop> droplet, TwoVector pos) {
		double xUp =0;
		double xLow = 0;
		double yUp =0;
		double yLow =0;
		double acc = 0.00000001;
		
		for (OilDrop drop : droplet) {
			xUp = xUp + drop.strengthAtPt(pos.add(new TwoVector(acc,0)));
			xLow = xLow + drop.strengthAtPt(pos.subtract(new TwoVector(acc,0)));
			yUp = yUp + drop.strengthAtPt(pos.add(new TwoVector(0,acc)));
			yLow = yLow + drop.strengthAtPt(pos.subtract(new TwoVector(0,acc)));
		}
		
		double xGrad = (xUp - xLow) / (acc*2);
		double yGrad = (yUp - yLow) / (acc*2);
		return new TwoVector(xGrad,yGrad);
	}
	
	//returns mirror drop on plane specified
	public OilDrop mirrorDrop(double mirrorXCoord) {
		TwoVector dropPos = new TwoVector(mirrorXCoord-this.currPos.getX(),this.currPos.getY());
		OilDrop mirrorDrop = new OilDrop(dropPos, this.waveSpeed, this.frequency,1.0);
		mirrorDrop.vel = this.vel.multiply(-1);
		mirrorDrop.prevPos = dropPos.subtract(this.prevPos);
		mirrorDrop.displacement = mirrorDrop.currPos.subtract(mirrorDrop.prevPos);
		mirrorDrop.gamma = 1/(Math.sqrt(1-Math.pow(this.vel.magnitude()/waveSpeed, 2)));
		mirrorDrop.time = time + (Math.PI/frequency);
		return mirrorDrop;
	}
	
	public void decayUpdate(double decayRate) {
		this.decay = this.decay*decayRate;
	}
	
}
