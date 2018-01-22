package test;

import java.util.ArrayList;

public class DataFrame {
	
	@Override
	public String toString() {
		return String.format("[{%s};%s]\r", this.frame, this.t);
	}

	ArrayList<DataPoint> frame;
	double t;

	public DataFrame(ArrayList<DataPoint> frame, double t) {
		this.t= t;
		this.frame = frame;
	}

	public ArrayList<DataPoint> getFrame() {
		return frame;
	}

	public double getT() {
		return t;
	}

	public void setFrame(WaveEquation wave) {
		for (DataPoint point : this.getFrame()) {
			point.setZ(this.getT(), wave);
		}
	}
	
}
