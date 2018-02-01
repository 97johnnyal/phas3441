package prototype;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;
import javax.swing.JPanel;

public class OilDropAnimationPanel extends JPanel implements ActionListener {

	// creating 
	private static Polygon pixel;
	private static final long serialVersionUID = 1L;
	GenerateData gd = new GenerateData();
	private final int delay = 50;
	private Timer animationTimer;
	public int resolution;
	
	// method to create an animation with the sun at the center
	OilDropAnimationPanel(int width, int height) {
		setPreferredSize(new Dimension(width,height));
		this.gd.data();
		animationTimer = new Timer(delay,this);
		animationTimer.start();
	}
	
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// create a Graphics2D object to make ellipses
		int height = getHeight();
		int width = getWidth();
		// Fill in background
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, width, height);
		// Now move origin to centre of panel
		g.translate(width/2, height/2);
		
		int m = 7;
		
		// go through all planets and redraw them at their updated position 
		int delta = 26000/gd.currentFrame.getResolution()/gd.currentFrame.getScreenSize();
		for (DataPoint dp:gd.currentFrame.frame) {
			g.setColor(dp.getColour());
			int[] xpts = {dp.x*m + delta, dp.x*m-delta, dp.x*m-delta, dp.x*m+delta};
			int[] ypts = {dp.y*m + delta, dp.y*m+delta, dp.y*m-delta, dp.y*m-delta};
			pixel = new Polygon(xpts, ypts, 4);
			g.fillPolygon(pixel);
				
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		// update planets position and then repaint
		gd.nextFrame();
		repaint();
		
	}
	
	 /** Start the animation */
	 public void start() {animationTimer.start();}
	 /** Stop the animation */
	 public void stop() {animationTimer.stop();}


}