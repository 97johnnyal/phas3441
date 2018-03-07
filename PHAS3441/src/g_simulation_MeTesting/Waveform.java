package g_simulation_MeTesting;

import java.util.ArrayList;

import org.apache.commons.math3.special.BesselJ;

public class Waveform {
	//Parameters of wavefront
	TwoVector origin; // the centre of the wave
	double n; // the count number of the wave
	double T_F;
	double amplitude; // amplitude of this wave
	BesselJ bessel = new BesselJ(0);// bessel function of the zeroth order
	double ME;
	double K_F;
	
	//constructor creates the residual wave left behind due to previous impact
	public Waveform (OilDrop droplet, double n) {
		this.origin = droplet.prevPos;
		this.n = n;
		this.T_F = droplet.T_F;
		this.amplitude = droplet.Amplitude;
		this.ME = droplet.Me;
		this.K_F = droplet.K_F;
	}
	
	//calculates the waveheight at a given point due to this wave at a given time --> the contribution of this wave to the entire wave field
	public double waveHeight (TwoVector point, double time) {
		TwoVector displacement = point.subtract(this.origin);
		double distance = displacement.magnitude();
		double waveHeight = amplitude * bessel.value(this.K_F * distance) * Math.exp(-(time-(this.n*this.T_F))/(this.T_F*this.ME));
		return waveHeight;
	}
	
	//calculates gradient at a given point given a collection of waveforms by taking 
	//infinitesimal displacements in the x and y direction and calculating the chang over that distance. (displacement accuracy predefined)
	public static TwoVector fieldGradient (ArrayList<Waveform> waveField, TwoVector point, double time) {
		//accuracy of calculation
		double accuracy = 1e-18;
		
		//determines coordinates of infinitesimal displacements
		TwoVector left = point.subtract(new TwoVector(accuracy,0));
		TwoVector right = point.add(new TwoVector(accuracy,0));
		TwoVector bottom = point.subtract(new TwoVector(0,accuracy));
		TwoVector top = point.add(new TwoVector(0,accuracy));
		
		//determines total waveheight at each infinitesimal displacement
		double leftHeight = 0;
		double rightHeight = 0;
		double bottomHeight = 0;
		double topHeight = 0;
		for (Waveform w : waveField) {
			leftHeight = leftHeight + w.waveHeight(left, time);
			rightHeight = rightHeight + w.waveHeight(right, time);
			bottomHeight = bottomHeight + w.waveHeight(bottom, time);
			topHeight = topHeight + w.waveHeight(top, time);
		}
		
		// determines gradient in each direction 
		double gradx = (rightHeight - leftHeight)/ (2* accuracy);
		double grady = (topHeight - bottomHeight)/ (2* accuracy);
		
		return new TwoVector (gradx,grady);
	}

	
}
