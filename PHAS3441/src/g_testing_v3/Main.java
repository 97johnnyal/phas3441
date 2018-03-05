package g_testing_v3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		//Collect position data as a text file
    	//print particle location to file //initiate file output data
    	File outputfileVel= new File("D:"+ File.separator +"Frames" + File.separator +"Velocity Data" +".csv");
    	FileWriter fwPVel ;
		BufferedWriter bVel = null ;
		PrintWriter pwVel = null ;
		
    	// initiates writers
		try {
			fwPVel = new FileWriter(outputfileVel);
			bVel = new BufferedWriter(fwPVel);
			pwVel = new PrintWriter(bVel);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		for(double perpvel = 0.0005; perpvel<=0.002; perpvel = perpvel + 0.00001) {
			ArrayList<OilDrop> droplets = new ArrayList<OilDrop>();
			
			TwoVector startPos = new TwoVector(0,0);
			TwoVector velocity = new TwoVector(0,0);
			droplets.add(new OilDrop(startPos,velocity));
			
			SimulationFrameMaker simulator = new SimulationFrameMaker(1000,1000,4, 10000,droplets);
			// new SimulationFrameMaker(1000,1000,4, 25000,droplets); working parameters
			simulator.perturbVel = perpvel;
			simulator.Simulate();
			
			pwVel.println(Double.toString(perpvel)+","+simulator.droplets.get(0).velocity);
		}
		
		try {
			bVel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
