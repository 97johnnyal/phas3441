package g_testing_v2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;
import javax.swing.JPanel;


public class DrawPanel extends JPanel implements Runnable {
	//initiate parameters
	ArrayList<Pixel> pixelList;
	Thread newThread; 
	int size;
	
	// Constructor
	public DrawPanel(ArrayList<Pixel> threadPixels, int size) {
		this.pixelList = threadPixels;
		this.size = size;
		newThread = new Thread(this);
	}
	
	// painter
	public void paintComponent(Graphics g) {
		//Creating graphics for each pixel
		for (Pixel p : this.pixelList) {
			g.setColor(new Color(p.colorIntensity,p.colorIntensity,p.colorIntensity));
			g.fillRect((int)p.position.getX(), (int)p.position.getY(), this.size, this.size);
		}
	}

	public void run() {
		 while(newThread != null){
			 for (Pixel p : this.pixelList) {
				 p.WaveAnimate();
			 }
			 repaint();
		 }
	}
	
}
