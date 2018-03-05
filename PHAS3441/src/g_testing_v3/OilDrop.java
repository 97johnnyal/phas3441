package g_testing_v3;

import java.util.ArrayList;

import org.apache.commons.math3.special.BesselJ;

public class OilDrop {
	// System parameters
	double drop_radius = 0.4*Math.pow(10,-3);
	double oil_density = 0.971;
	double mass_droplet = 2.6*Math.pow(10,-7);
	double force_freq = 80;
	double T_F = 2/force_freq;
	double k = (4*mass_droplet*(Math.pow(Math.PI,2)))/(Math.pow(0.250,2));
	double g= -9.81;
	// Viscous Damping
	double D = 0;
	// Wave Force
	double F = 1.3174e-6;
	double K_F = 1250;
	double Amplitude = F/(mass_droplet*K_F*-9.81);
	// Memory
	double Me = 150;
	
	//Droplet dynamic paramameters
	TwoVector currentPos;
	TwoVector prevPos;
	TwoVector velocity;
	
	
	//constructor
	public OilDrop(TwoVector startPos,TwoVector startVel) {
		this.currentPos = startPos;
		this.prevPos = startPos;
		this.velocity = startVel;
	}
	
	// droplet updater adds the change in velocity, sets prevPos to the currentPos and updates the currentPos 
	// by adding the displacement due to the new velocity in 1 timestep (1 period)
	public void update(TwoVector dVel) {
		this.velocity = this.velocity.add(dVel);
		this.prevPos = this.currentPos;
		this.currentPos = this.currentPos.add(this.velocity.multiply(T_F));
	}
	
	
	
	
}
