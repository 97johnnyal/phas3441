package g_testing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NextPosGenerator{
	// initiating variables
	int precision;
	int nThreads;
	Random rand = new Random();
	OilDrop droplet;
	double time;
	
	//constructor
	public NextPosGenerator(int precision, int nThreads, OilDrop droplet, double time) {
		this.precision = precision;
		this.nThreads = nThreads;
		this.droplet = droplet;
		this.time = time;
	}
	
	public TwoVector generate() {
		ExecutorService threadPool = Executors.newFixedThreadPool(this.nThreads);
		List<Future<SimPoints>> futures = new ArrayList<Future<SimPoints>>();
		ArrayList<SimPoints> shortListPoints = new ArrayList<SimPoints>();
		
		//creates task per thread
		for (int iThread = 0; iThread < this.nThreads; ++iThread) {
			SubGenerator task = new SubGenerator(iThread*1000/this.nThreads, 
													(iThread+1)*1000/this.nThreads, 
													0,1000);
			Future<SimPoints> future = threadPool.submit(task);
			futures.add(future);
		}
		
		// performs animation
		for (int iThread = 0; iThread < nThreads; ++iThread) {
			try {
				shortListPoints.add(futures.get(iThread).get());
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		threadPool.shutdown();
		threadPool.shutdownNow();
		
		double sumSim = 0;
		for (SimPoints p : shortListPoints) {
			sumSim = sumSim + p.pdf;
		}
		
		//gen rand number between 0 and 1 and multiply by sumPointsProb to determine point value for random gen point
		double value = rand.nextDouble() * sumSim;
		//initiate counter
		int counter =-1;
		//det which simPoint it is at
		while (value>0) {
			counter = counter +1;
			value = value - shortListPoints.get(counter).pdf;
		} 
		
		return new TwoVector(shortListPoints.get(counter).getX(),shortListPoints.get(counter).getY());
		
	}
	
	
	class SubGenerator implements Callable<SimPoints>{
		//intitiating variable
		ArrayList<SimPoints> points = new ArrayList<SimPoints>();
		double rangeXUpper; 
		double rangeXLower; 
		double rangeYUpper; 
		double rangeYLower;
		
		public SubGenerator(double rangeXLower, double rangeXUpper, double rangeYLower, double rangeYUpper) {
			this.rangeXUpper=rangeXUpper; 
			this.rangeXLower=rangeXLower; 
			this.rangeYUpper=rangeYUpper; 
			this.rangeYLower=rangeYLower;
			

		}
		
		// perform generation within range
		public SimPoints call() throws Exception {
			double sumPointsProb =0;
			
			//initiate counter
			double i = this.rangeXLower;
			
			//generate arraylist of points to be simulated
			while(i<rangeXUpper) {
				double j = this.rangeYLower;
				while(j<rangeYUpper) {
					SimPoints simPoint = new SimPoints(i,j,droplet.pdAtVector(new TwoVector(i,j), time));
					points.add(simPoint);
					sumPointsProb = sumPointsProb + simPoint.pdf;
					j = j+precision;
				}
				i = i+precision;
			}
			
			//gen rand number between 0 and 1 and multiply by sumPointsProb to determine point value for random gen point
			double value = rand.nextDouble() * sumPointsProb;
			//initiate counter
			int counter =-1;
			//det which simPoint it is at
			while (value>0) {
				counter = counter +1;
				value = value - points.get(counter).pdf;
				
			}
			return new SimPoints(points.get(counter).getX(),points.get(counter).getY(),sumPointsProb);
		}
		
	}
	

}
