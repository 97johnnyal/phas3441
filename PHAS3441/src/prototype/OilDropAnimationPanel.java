package prototype;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class OilDropAnimationPanel extends JPanel implements ActionListener {

	// creating 
	private static Polygon pixel;
	private static final long serialVersionUID = 1L;
	GenerateData gd = new GenerateData();
	private final int delay = 50;
	private Timer animationTimer;
	public int resolution;
	
	OilDropAnimationPanel(int width, int height) {
		setPreferredSize(new Dimension(width,height));
		animationTimer = new Timer(delay,this);
		animationTimer.start();
		this.gd.data();
	}
	
	
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int height = getHeight();
		int width = getWidth();
		// Fill in background
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, width, height);
		// Now move origin to centre of panel
		g.translate(width/2, height/2);
		
		
		// draw all pixels - to be parallelised 
		int m = gd.currentFrame.getPixelSize();
		int offset = gd.currentFrame.getOffset();
		for (DataPoint dp:gd.currentFrame.frame) {
			g.setColor(dp.getColour());
			int[] xpts = {dp.x*m + offset, dp.x*m-offset, dp.x*m-offset, dp.x*m+offset};
			int[] ypts = {dp.y*m + offset, dp.y*m+offset, dp.y*m-offset, dp.y*m-offset};
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