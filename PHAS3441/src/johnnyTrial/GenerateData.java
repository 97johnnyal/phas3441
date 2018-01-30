package johnnyTrial;

import java.util.ArrayList;

public class GenerateData {
	
	public ArrayList<DataFrame> frames;
	public DataFrame currentFrame;
	public int frameNo = 0;
	public ColorScale cs = new ColorScale(1, -1, 0, 1000);

	public void data() {
		double interval = currentFrame.resolution;
		WaveEquation wave = new WaveEquation(1,1);
		ArrayList<DataFrame> frames = new ArrayList<DataFrame>();
		for (double t = 0; t <= 10; t+= interval/10) {
			ArrayList<DataPoint> thesePoints = new ArrayList<DataPoint>();
			for (int x = -50; x<=50; x+=interval) {
				for (int y = -50; y<=50; y+=interval) {
					
					DataPoint thisPoint = new DataPoint(x,y);
					thisPoint.setZ(t, wave);
					thisPoint.setColour(cs.getColour(thisPoint.getZ()));
					thesePoints.add(thisPoint);
				}
			}
			DataFrame thisFrame = new DataFrame(thesePoints, t, (int) (101/interval), 1200); 
			frames.add(thisFrame);
		}
		
		this.frames = frames;
		this.currentFrame = this.frames.get(0);

	}
	
	public void nextFrame() {
		
		if (frameNo < frames.size()) {
			this.currentFrame = this.frames.get(this.frameNo);
			this.frameNo++;
		}
	}
}