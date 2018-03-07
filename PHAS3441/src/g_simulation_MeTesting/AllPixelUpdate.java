package g_simulation_MeTesting;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class AllPixelUpdate implements Callable<ArrayList<Pixel>> {
	//initiate parameters
	ArrayList<Waveform> waveField;
	ArrayList<Pixel> pixelThread;
	double time;
	
	//constructor
	public AllPixelUpdate(ArrayList<Waveform> waveField, ArrayList<Pixel> pixelThread, double time) {
		this.pixelThread = pixelThread;
		this.waveField = waveField;
		this.time = time;
	}
	
	//each Thread
	public ArrayList<Pixel> call() throws Exception {
		for (Pixel p : pixelThread) {
			p.updateAmp(waveField, time);
		}
		return pixelThread;
	}
}
