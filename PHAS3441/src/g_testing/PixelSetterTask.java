package g_testing;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;

public class PixelSetterTask implements Callable<ArrayList<Pixel>> {
	//intitiating variable
	ArrayList<Pixel> pixelList;
	int threadNum;
	int totalThreads;
	OilDrop oilDrop;
	double time;
	Random rand = new Random();
	
	/** Constructs the object to calculate pi using the montecarlo method
	 * @param nPoints long number of points to be generated
	 */
	public PixelSetterTask(ArrayList<Pixel> pixelList,int threadNum, int totalThreads,OilDrop oil,double time) {
		this.pixelList = pixelList;
		this.threadNum = threadNum;
		this.totalThreads = totalThreads;
		this.oilDrop = oil;
		this.time = time;
	}

	public ArrayList<Pixel> call() throws Exception {
		ArrayList<Pixel> animatedPixelList = new ArrayList<Pixel>();
		for (int countNum = (this.threadNum * pixelList.size()/this.totalThreads); 
				countNum< ((this.threadNum+1) * pixelList.size()/this.totalThreads); countNum++) {
			Pixel animatedPixel = pixelList.get(countNum);
			//animatedPixel.WaveAnimate();
			double newAmp = this.oilDrop.contribution(animatedPixel, time);
			animatedPixel.evoAmplitude(newAmp);
			animatedPixelList.add(animatedPixel);
		}
		return animatedPixelList;
	}
}
