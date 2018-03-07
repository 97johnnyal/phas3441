package g_simulation_MeTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Pixel {
	//initiating member variables
	TwoVector pos;
	double amplitude;
	TwoVector displayPos;
	
	//constructor
	public Pixel(TwoVector pos, TwoVector displayPos) {
		this.pos = pos;
		this.amplitude = 0.0;
		this.displayPos = displayPos;
	}
	
	// updater on pixel amplitude given an arraylist of waveforms
	public void updateAmp ( ArrayList<Waveform> waveField, double time) {
		this.amplitude = 0.0;
		for (Waveform w : waveField) {
			this.amplitude = this.amplitude + w.waveHeight(this.pos, time);
		}
	}
	
	// update all pixels amplitude
	public static ArrayList<Pixel> updateAllPix(ArrayList<Pixel> allPixels, ArrayList<Waveform> waveField, double time) {
		//initiate threads
		ExecutorService threadPool = Executors.newFixedThreadPool(8);
		List<Future<ArrayList<Pixel>>> futures = new ArrayList<Future<ArrayList<Pixel>>>();
		ArrayList<Pixel> updatedPixels = new ArrayList<Pixel>();
		int numPixels = allPixels.size();
		int interval = numPixels / 8;
		
		for (int iThread = 0; iThread<=7; iThread++) {
			ArrayList<Pixel> pixelThread = new ArrayList<Pixel>();
			for (int count = iThread*interval; count < (iThread+1)*interval; count++) {
				pixelThread.add(allPixels.get(count));
			}
			AllPixelUpdate updateThread = new AllPixelUpdate(waveField, pixelThread, time);
			Future<ArrayList<Pixel>> future = threadPool.submit(updateThread);
			futures.add(future);
		}
		
		// performs calculations and adds all dVel together
		for (int iThread = 0; iThread <= 7; iThread++) {
			try {
				 updatedPixels.addAll(futures.get(iThread).get());
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		//ensures threadPool is shutdown;
		threadPool.shutdown();
		
		return updatedPixels;
	}

}
