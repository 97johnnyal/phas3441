package johnnyTrial;

import java.util.ArrayList;

public class DataFrame {
	
	@Override
	public String toString() {
		return String.format("[{%s};%s]\r", this.frame, this.t);
	}

	ArrayList<DataPoint> frame;
	double t;
	int resolution;
	int screenSize;

	public DataFrame(ArrayList<DataPoint> frame, double t, int resolution, int screenSize) {
		this.t= t;
		this.frame = frame;
		this.resolution = resolution;
		this.screenSize = screenSize;
	}

	public ArrayList<DataPoint> getFrame() {
		return frame;
	}

	public double getT() {
		return t;
	}
	
	public int getResolution() {
		return this.resolution;
	}
	
	public int getScreenSize() {
		return this.screenSize;
	}

	public void setFrame(WaveEquation wave) {
		for (DataPoint point : this.getFrame()) {
			point.setZ(this.getT(), wave);
		}
	}
	
}