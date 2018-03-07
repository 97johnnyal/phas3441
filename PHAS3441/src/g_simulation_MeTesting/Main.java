package g_simulation_MeTesting;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		for(double Me = 65.1; Me<=75 ; Me = Me + 0.1) {
			//initiate oil drop with memory Me
			TwoVector startPos = new TwoVector(0,0);
			TwoVector velocity = new TwoVector(0,0);
			OilDrop droplet = new OilDrop(startPos,velocity);
			droplet.Me = Me;
			
			//create array list of droplets for the simulation (in this case only 1 droplet was used)
			ArrayList<OilDrop> droplets = new ArrayList<OilDrop>();
			droplets.add(droplet);
			
			SimulationTestME tester = new SimulationTestME(droplets);
			tester.runSimulation();
			
		}
		
	}

}
