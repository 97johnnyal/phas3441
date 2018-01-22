package test;

import java.awt.Color;

import org.apache.commons.math3.special.BesselJ;

public class DataPoint {

	double x;
	double y;
	double z;
	Color colour;

	public DataPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double t, WaveEquation wave) {
		BesselJ bessel = new BesselJ(0);
		double r = Math.sqrt(this.getX()*this.getX()+this.getY()*this.getY());
		//System.out.println(r);
		double h = wave.getInitialHeight()*Math.cos(wave.getSpeed()*Math.PI*t)*bessel.value(Math.PI*r);
		this.z = h;
	}

	public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

	@Override
	public String toString() {
		return String.format("%s,%s,%s", this.x, this.y, this.z);
	}




}
