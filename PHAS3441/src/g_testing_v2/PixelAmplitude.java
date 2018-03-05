package g_testing_v2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PixelAmplitude {
	//Initiating variables
	ArrayList<Pixel> pixelList;
	int nThreads;
	ArrayList<OilDrop> droplets;
	double time;
	int xUpper; 
	int xLower;
	int yUpper; 
	int yLower;
	int pixelSize;
	
	//Constructor
	public PixelAmplitude(int nThreads, ArrayList<OilDrop> droplets, double time, int xUpper, int xLower, int yUpper, int yLower, 	int pixelSize) {
		this.nThreads = nThreads;
		this.droplets = droplets;
		this.time = time;
		this.xUpper = xUpper;
		this.xLower = xLower;
		this.yUpper = yUpper;
		this.yLower = yLower;
		this.pixelSize = pixelSize;
	}

	//Updates the list input
	public ArrayList<Pixel> UpdatedPixelList(){
		ArrayList<Pixel> updatedList = new ArrayList<Pixel>();
		ExecutorService threadPool = Executors.newFixedThreadPool(this.nThreads);
		List<Future<ArrayList<Pixel>>> futures = new ArrayList<Future<ArrayList<Pixel>>>();
		int xRange = (this.xUpper - this.xLower) / this.nThreads;
		
		//creates task per thread
		for (int iThread = 0; iThread < this.nThreads; ++iThread) {
			UpdaterTask task = new UpdaterTask((this.xLower + (iThread * xRange)),((iThread+1)*xRange),this.yLower,this.yUpper);
			Future< ArrayList<Pixel>> future = threadPool.submit(task);
			futures.add(future);
		}
		
		// performs calculations and adds all pixels generated on everythread to updatedList ArrayList
		for (int iThread = 0; iThread < nThreads; ++iThread) {
			try {
				updatedList.addAll(futures.get(iThread).get());
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		//ensures threadPool is shutdown;
		threadPool.shutdown();
		threadPool.shutdownNow();
		return updatedList;
	}
	
	//Class for each thread
	class UpdaterTask implements Callable<ArrayList<Pixel>>{
		//intitiating variable
		ArrayList<Pixel> pixelPoints = new ArrayList<Pixel>();
		double rangeXUpper; 
		double rangeXLower; 
		double rangeYUpper; 
		double rangeYLower;
		
		//constructor
		public UpdaterTask(double rangeXLower, double rangeXUpper, double rangeYLower, double rangeYUpper) {
			this.rangeXUpper=rangeXUpper; 
			this.rangeXLower=rangeXLower; 
			this.rangeYUpper=rangeYUpper; 
			this.rangeYLower=rangeYLower;
		}
		
		// perform generation within range
		public ArrayList<Pixel> call() throws Exception {
			//initiate counter
			double i = this.rangeXLower;
			
			//generate arraylist of new pixels with updated amplitude
			while(i<rangeXUpper) {
				double j = this.rangeYLower;
				while(j<rangeYUpper) {
					TwoVector position = new TwoVector(i,j);
					double amp = 0;
					for(OilDrop drop: droplets) {
						amp = amp + drop.strengthAtPt(position);
					}
					//System.out.println(amp);
					Pixel updatePoint = new Pixel(position, amp);
					this.pixelPoints.add(updatePoint);
					j = j+pixelSize;
				}
				i = i+pixelSize;
			}
			
			return this.pixelPoints;
		}
		
	}
		
}
