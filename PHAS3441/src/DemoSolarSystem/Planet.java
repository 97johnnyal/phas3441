package DemoSolarSystem;

import java.lang.Math;
import java.awt.Color;

public class Planet {
	
	
	long distance; // m
	long mass; // kg
	long radius; // m
	long[] pos = new long[2]; //m
	long speed; // m/s
	long[] velocity = new long[2]; // m/s
	Color shade; // colour of planet
	
	// constructor class, setting all of the values 
	public Planet(long d, long m, long r, Color planetColor) {
		
		this.distance = d;
		this.mass = m;
		this.radius = r;
		this.speed = (long) Math.sqrt(6.674e-11*1.989e30/this.distance);
		this.pos[0] = (long) d;
		this.pos[1] = (long) 0;
		this.velocity[0] = this.pos[1]*this.speed/this.distance;
		this.velocity[1] = this.pos[0]*this.speed/this.distance;
		this.shade = planetColor;
		
	}
	
	// to string class for planets
	public String toString() {
		
		String str = "Distance from sun: " + this.distance + " m \nMass: " + this.mass +
				" kg \nRadius of planet: " + this.radius + " m \nPosition: (" + this.pos[0] + ", " +
				this.pos[1] + ") m" + "\nSpeed: " + this.speed + " m/s\nVelocity: (" +
				this.velocity[0] + ", " + this.velocity[1] + ")n";
		
		
		return str;
		
	}

}