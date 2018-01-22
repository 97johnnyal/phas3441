package DemoSolarSystem;

import java.awt.Color;
import java.util.ArrayList;

public class SolarSystem {

	public static double G = 6.674e-11;
	public static long mSunG = (long) (1.989e30 * 6.674e-11);
	public static long dt = (long) 10000;
	public static long maxTime = (long) 10e7;
	public long time = 0;

	Planet mercury = new Planet(57910000000l, (long) 3.285e23, 2440000l, Color.GRAY);
	Planet venus = new Planet((long) 108.2e9, (long) 4.867e24, 6052000l, new Color(227,158,28));
	Planet earth = new Planet((long) 149.6e9, (long) 5.972e24, 6371000l, Color.BLUE);
	Planet mars = new Planet((long) 227.9e9, (long) 6.39e23, 3390000l, Color.RED);
	Planet jupiter = new Planet((long) 778.5e9, (long) 1.898e27, 69911000l, new Color(245,222,179));
	Planet saturn = new Planet((long) 1429e9, (long) 5.683e26, 58232000l, new Color(210,180,140));
	Planet uranus = new Planet((long) 2871e9, (long) 8.681e25, 25362000l, Color.CYAN);
	Planet neptune = new Planet((long) 4498e9, (long) 1.024e26, 24622000l, new Color(51, 102, 153));

	ArrayList<Planet> planets = new ArrayList<Planet>() {
		private static final long serialVersionUID = 1L;

		{
			add(mercury); add(venus); add(earth); add(mars); 
			add(jupiter); add(saturn); add(uranus); add(neptune);
		}
	};

	ArrayList<Planet> updatePos(SolarSystem ss) {

		ArrayList<Planet> others = new ArrayList<Planet>() {
			private static final long serialVersionUID = 1L;
			{
				add(ss.mercury); add(ss.venus); add(ss.earth); add(ss.mars); 
				add(ss.jupiter); add(ss.saturn); add(ss.uranus); add(ss.neptune);

			}
		};

		long[] d = new long[2];

		for (Planet p:ss.planets) {

			others.remove(p);

			//p.acc[0] = (long) ((mSunG*p.pos[0])/Math.pow(p.distance, 3));
			//p.acc[1] = (long) ((mSunG*p.pos[1])/Math.pow(p.distance, 3));

			/*for (Planet o:others) {
				d[0] = p.pos[0] - o.pos[0];
				d[1] = p.pos[1] - o.pos[1];
				p.acc[0] = (p.acc[0] - G*o.mass)/d[0]/d[0];
				p.acc[1] = (p.acc[1] - G*o.mass)/d[1]/d[1];
			}*/

			//p.velocity[0] = p.velocity[0] + p.acc[0]*dt;
			//p.velocity[1] = p.velocity[1] + p.acc[1]*dt;
			p.pos[0] = p.pos[0] + p.velocity[0]*dt;
			p.pos[1] = p.pos[1] + p.velocity[1]*dt;

			others.add(p);

		}

		ss.time = ss.time + ss.dt;
		return ss.planets;

	}

}