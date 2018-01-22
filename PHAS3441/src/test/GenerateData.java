package test;

import java.util.ArrayList;

public class GenerateData {

	public static void main(String[] args) {
		double interval = 1;
		WaveEquation wave = new WaveEquation(1,1);
		ArrayList<DataFrame> frames = new ArrayList<DataFrame>();
		for (double t = 0; t <= 1; t+= interval/10) {
			ArrayList<DataPoint> thesePoints = new ArrayList<DataPoint>();
			for (double x = -2; x<=2; x+=interval) {
				for (double y = -2; y<=2; y+=interval) {
					DataPoint thisPoint = new DataPoint(x,y);
					thisPoint.setZ(t, wave);
					thesePoints.add(thisPoint);
				}
			}
			DataFrame thisFrame = new DataFrame(thesePoints, t); 
			frames.add(thisFrame);
		}
		
		System.out.print(frames);

	}

}
