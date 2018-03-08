package g_simulation_MeTesting;

import java.util.ArrayList;

//No PIXELS
public class SimulationTestME {
	// initiate simulator properties
	OilDrop droplet;
	ArrayList<Waveform> waveField = new ArrayList<Waveform>();
	
	//initiate timestep counter n
	double n =0;
	
	//initiate perturbation velocity
    double perturbVel=0.0005;
	
	
	//Constructor
	public SimulationTestME(OilDrop droplet) {
		this.droplet = droplet;
		
		//Creates new field (for 300 static fields)
    	while (n<300) {
    		waveField.add(new Waveform(this.droplet,n));
    		n = n+1;
    	}
	}
	
	//Simulation run outputs data file for particle position 
	public DataObject runSimulation() {
		
		// change velocity to pertubation velocity
		this.droplet.velocity = new TwoVector (this.perturbVel, 0);
		
		//constant velocity checker counter
		boolean steadyStateReached = false;
		int steadyStateCounter = 0;
		DataObject steadyStateData = new DataObject(new TwoVector (0,0),0);
		
		//performs simulation with timestep T_F
		while (n<10000 && steadyStateReached == false) {
			//determines time
			double time = n*this.droplet.T_F;
			
			//calculates gradient and using that the change in velocity due to interaction with the gradient of the particle
    		TwoVector gradient = Waveform.fieldGradient(waveField, this.droplet.currentPos, time);
    		TwoVector dVel = gradient.multiply( - this.droplet.T_F*0.01*this.droplet.F/this.droplet.mass_droplet);
    		this.droplet.update(dVel);
    		
    		if(dVel.getX() == 0 && Math.abs(this.droplet.currentPos.getX())> (2*Math.PI/this.droplet.K_F)) {
    			if (steadyStateCounter == 0) {
    				steadyStateData.steadyVel = this.droplet.velocity;
        			steadyStateData.time = time;
    			}
    			else if(steadyStateCounter == 40) {
    				steadyStateReached = true;
    				steadyStateData.steadyStateReached = true;
    			}
    			steadyStateCounter ++;
    		}
    		
    		//resets steady state checker
    		else {
    			steadyStateData.steadyVel = new TwoVector (0,0);
    			steadyStateData.time = 0;
    			steadyStateCounter = 0;
    			steadyStateReached = false;
    		}
    		
    		//adds wave due to that impact
    		waveField.add(new Waveform(this.droplet,n));
    		
			// removes any waveform from 200T_F s ago.
			if(waveField.size()>300) {
				waveField.remove(0);
			}
			
			//increase time step
			n= n+1;
		}
		
		if (steadyStateCounter < 20){
			steadyStateData.steadyVel = new TwoVector (0,0);
			steadyStateData.time = 0;
			steadyStateCounter = 0;
		}
		
		return steadyStateData;
	}
	
}
