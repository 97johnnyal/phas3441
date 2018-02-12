package g_testing_v2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class SimulationDataCollector {
	ArrayList<Pixel> allPixels = new ArrayList<Pixel>();
    int nThreads = 8;
    int size =2;
    ArrayList<OilDrop> residualDrops = new ArrayList<OilDrop>();
    double frequency;
    OilDrop ogDrop;
    double time =0;
    
    public SimulationDataCollector(TwoVector start, TwoVector dropvelocity, double waveVelocity, double frequency) {
    	this.frequency = frequency;
    	ogDrop = new OilDrop(start, waveVelocity, frequency,0);
    	ogDrop.updateParam(dropvelocity, time);
    	residualDrops.add(ogDrop);
    	//residualDrops.add(ogDrop.mirrorDrop(0));
    }
	
    public void outputData(double maxCalc, String filename) {
    	System.out.println(filename + " STARTED");
    	
    	//initiate file output data
    	File outputfile = new File(System.getProperty("user.home") + File.separator +filename +".txt");
    	FileWriter fw ;
		BufferedWriter b = null ;
		PrintWriter pw = null ;
		
    	// initiates writers
		try {
			fw = new FileWriter(outputfile);
			b = new BufferedWriter(fw);
			pw = new PrintWriter(b);
		} catch (Exception e) {
			System.out.println(e);
		}
    	
		// determine simulation start time
		long startSim = System.currentTimeMillis();
		
    	for (int calculationRound = 0; calculationRound <= maxCalc ; calculationRound++) {
    		// Det change in velocity = g * gradient * 0.01 / (2piOmega)
			//TwoVector dVel = OilDrop.gradient(this.residualDrops, ogDrop.currPos).multiply(100 * 0.1 / (2*Math.PI*this.frequency));
			TwoVector dVel = gradCalc();
    		
			//print to data file
			pw.println(calculationRound+"    "+ogDrop.vel+"    "+ogDrop.currPos+"    "+ this.residualDrops.size());
			ogDrop.updateParam(dVel, time);
			for (OilDrop o : residualDrops){
				o.decayUpdate(1);
			}
			
			residualDrops.add(ogDrop);
	    	//residualDrops.add(ogDrop.mirrorDrop(0));
    	}
    	// determine end time and time taken
		long endSim = System.currentTimeMillis();
		long timeElapsed2 = endSim - startSim;
    	try {
			b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("DONE, simulation time = " + timeElapsed2 + "ms" );
    }
	
    //gradient optimiser
    class GradientOptimiser implements Callable<TwoVector>{
    	//initiate parameters
    	int startCounter;
    	int endCounter;
    	
    	//constructor
    	public GradientOptimiser(int startCounter, int endCounter) {
    		this.startCounter = startCounter;
    		this.endCounter = endCounter;
    	}

		//each Thread
		public TwoVector call() throws Exception {
			ArrayList<OilDrop> dropsThread = new ArrayList<OilDrop>();
			while(startCounter<=endCounter) {
				dropsThread.add(residualDrops.get(startCounter));
				startCounter++;
			}
			TwoVector grad = OilDrop.gradient(dropsThread, ogDrop.currPos);
						
			return grad;
		}
    }
    
    //grad calculator for 8 threads
    public TwoVector gradCalc() {
    	TwoVector dVel = new TwoVector(0,0);
    	
    	ExecutorService threadPool = Executors.newFixedThreadPool(8);
		List<Future<TwoVector>> futures = new ArrayList<Future<TwoVector>>();
		int size = this.residualDrops.size();
		
		if (size > 7) {
			int interval = (int) size/8;
			//System.out.println("Interval"+ interval);
			for (int iThread = 0; iThread<7; ++iThread) {
				GradientOptimiser gradThread = new GradientOptimiser(iThread*interval, ((iThread +1)*interval)-1);
				Future<TwoVector> future = threadPool.submit(gradThread);
				futures.add(future);
			}
			GradientOptimiser gradThread = new GradientOptimiser(7*interval, size-1);
			Future<TwoVector> future = threadPool.submit(gradThread);
			futures.add(future);
			
			// performs calculations and adds all dVel together
			for (int iThread = 0; iThread < nThreads; ++iThread) {
				try {
					TwoVector dGrad = futures.get(iThread).get();
					dVel = dVel.add(dGrad);
				} catch (Exception e) {
					System.out.println(e);
				}
			}
			
			//ensures threadPool is shutdown;
			threadPool.shutdown();
			threadPool.shutdownNow();
	    	return dVel.multiply(100 * 0.1 / (2*Math.PI*this.frequency));
		}
		
		else {
			// Det change in velocity = g * gradient * 0.01 / (2piOmega)
			dVel = OilDrop.gradient(this.residualDrops, ogDrop.currPos).multiply(100 * 0.1 / (2*Math.PI*this.frequency));
			return dVel;
		}
    }
    
	public static void main(String[] args) {
		double xVel = 100;
		SimulationDataCollector simulator;
		while (xVel < 101) {
			simulator = new SimulationDataCollector(new TwoVector(0,0), 
													new TwoVector(xVel,0), 
													1350, 80);
			String fileName = "vel"+Double.toString(xVel);
			simulator.outputData(30000, fileName);
			xVel = xVel + 10;
		}
	}
}
