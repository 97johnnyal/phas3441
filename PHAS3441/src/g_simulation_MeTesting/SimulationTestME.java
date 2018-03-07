package g_simulation_MeTesting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

//No PIXELS
public class SimulationTestME {
	// initiate simulator properties
	ArrayList<OilDrop> droplets = new ArrayList<OilDrop>();
	ArrayList<Waveform> waveField = new ArrayList<Waveform>();
	
	//initiate timestep counter n
	double n =0;
	
	//initiate perturbation velocity
    double perturbVel=0.0005;
	
	
	//Constructor
	public SimulationTestME(ArrayList<OilDrop> droplets) {
		this.droplets = droplets;
		
		//Creates new field (for 300 static fields)
    	while (n<300) {
    		for (int i = 0; i < droplets.size(); i++) {
    			OilDrop o = droplets.get(i);
    			waveField.add(new Waveform(o,n));
    		}
    		n = n+1;
    	}
	}
	
	//Simulation run outputs data file for particle position 
	public void runSimulation() {

    	//Collect position data as a text file
    	//print particle location to file //initiate file output data
    	File outputfileParticle = new File("D:"+ File.separator +"Frames" + File.separator +"Particle Data" 
    										+Double.toString(droplets.get(0).Me)+".csv");
    	FileWriter fwParticle ;
		BufferedWriter bParticle = null ;
		PrintWriter pwParticle = null ;
		
    	// initiates writers
		try {
			fwParticle = new FileWriter(outputfileParticle);
			bParticle = new BufferedWriter(fwParticle);
			pwParticle = new PrintWriter(bParticle);
		} catch (Exception e) {
			System.out.println(e);
		}
    	
		pwParticle.println("#n,#posx,#posy,#velx,#vely,#time");
		
		//adds pertubation after 300th waveform
		for(OilDrop o : droplets) {
			o.velocity = new TwoVector (this.perturbVel, 0);
		}
		
		//performs simulation with timestep T_F
		while (n<500) {
			//determines time
			double time = n*droplets.get(0).T_F;
			
			//updates all droplets and assumes the interact with the field at the given time
			for (OilDrop o : droplets) {
				//calculates gradient and using that the change in velocity due to interaction with the gradient of the particle
	    		TwoVector gradient = Waveform.fieldGradient(waveField, o.currentPos, time);
	    		TwoVector dVel = gradient.multiply( - o.T_F*0.01*o.F/o.mass_droplet);
	    		o.update(dVel);
			}
			
			//Creates new field
			for (int i = 0; i < droplets.size(); i++) {
				OilDrop o = droplets.get(i);
				waveField.add(new Waveform(o,n));
				pwParticle.println(n+ "," +o.currentPos + "," +o.velocity + ","+ time);
			}
			
			// removes any waveform from 200T_F s ago.
			if(waveField.size()>300) {
				waveField.remove(0);
			}
			
			//increase time step
			n= n+1;
		}
		
		try {
			bParticle.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	}
	
}
