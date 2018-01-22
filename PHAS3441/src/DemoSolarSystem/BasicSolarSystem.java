package DemoSolarSystem;

import java.awt.Color;
import java.util.ArrayList;

public class BasicSolarSystem {

	// setting the time interval, 
	public static long dt = (long) 10000;
	
	// creating the first four planets
	Planet mercury = new Planet(57910000000l, (long) 3.285e23, 2440000l, Color.GRAY);
	Planet venus = new Planet((long) 108.2e9, (long) 4.867e24, 6052000l, new Color(227,158,28));
	Planet earth = new Planet((long) 149.6e9, (long) 5.972e24, 6371000l, Color.BLUE);
	Planet mars = new Planet((long) 227.9e9, (long) 6.39e23, 3390000l, Color.RED);
	
	// appending the planets to a list
	ArrayList<Planet> planets = new ArrayList<Planet>() {
		{
			add(mercury); add(venus); add(earth); add(mars);
		}
	};

	// method to update the position and velocity of the planets in a solarsystem class
	public ArrayList<Planet> updatedPlanets(BasicSolarSystem ss) {
		
		// loops through the planets
		for (Planet p:ss.planets) {
			
			//setting the velocity based on the current position
			p.velocity[0] = p.pos[1]*p.speed/p.distance;
			p.velocity[1] = p.pos[0]*p.speed/p.distance;
			
			// setting the new position based on current position and velocity
			p.pos[0] = p.pos[0] - p.velocity[0]*dt;
			p.pos[1] = p.pos[1] + p.velocity[1]*dt;
		}
		
		return ss.planets;
		
	}

}