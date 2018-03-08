package g_simulation_MeTesting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

	public static void main(String[] args) {
		
		//Collect position data as a text file
    	//print particle location to file //initiate file output data
    	File outputfileData = new File("D:"+ File.separator +"Frames" + File.separator +"Me Data" +".csv");
    	FileWriter fwData ;
		BufferedWriter bData = null ;
		PrintWriter pwData = null ;
		
    	// initiates writers
		try {
			fwData = new FileWriter(outputfileData);
			bData = new BufferedWriter(fwData);
			pwData = new PrintWriter(bData);
		} catch (Exception e) {
			System.out.println(e);
		}
    	
		pwData.println("#Me,#velx,#vely,#time,#reachedtrue/false");
		
		
		for(double Me = 45; Me<=85 ; Me = Me + 0.01) {
			//initiate oil drop with memory Me
			TwoVector startPos = new TwoVector(0,0);
			TwoVector velocity = new TwoVector(0,0);
			OilDrop droplet = new OilDrop(startPos,velocity);
			droplet.Me = Me;
			
			SimulationTestME tester = new SimulationTestME(droplet);
			DataObject dataForTest = tester.runSimulation();
			
			if(dataForTest.steadyStateReached == true) {
				pwData.println(Double.toString(Me) +","+ dataForTest.steadyVel+","+dataForTest.time+","+Integer.toString(1));
			}
			else {
				pwData.println(Double.toString(Me) +","+ dataForTest.steadyVel+","+dataForTest.time+","+Integer.toString(0));
			}
			System.out.println(Me + "done" + dataForTest.steadyStateReached);
		}
		try {
			bData.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
