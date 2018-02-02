package prototype;

import java.util.ArrayList;

public class DataFrame {

	@Override
	public String toString() {
		return String.format("[{%s};%s]\r", this.frame, this.t);
	}

	double t;
	int pixelSize;
	int offset;
	ArrayList<DataPoint> frame;

	/** Data frame object
	 * 
	 * @param frame array list of dataPoints making up the frame
	 * @param t current time
	 * @param pixelSize size of pixel???
	 * @param offset offset of pixel ???
	 */
	public DataFrame(ArrayList<DataPoint> frame, double t, int pixelSize, int offset) {
		this.t= t;
		this.frame = frame;
		this.pixelSize = pixelSize;
		this.offset = offset;
	}

	public ArrayList<DataPoint> getFrame() {
		return frame;
	}

	public double getT() {
		return t;
	}

	public int getPixelSize() {
		return pixelSize;
	}

	public void setFrame(WaveEquation wave) {
		for (DataPoint point : this.getFrame()) {
			point.setZ(this.getT(), wave);
		}
	}

	public int getOffset() {
		return offset;
	}

}