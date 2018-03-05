package DemoSolarSystem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;
import javax.swing.Timer;

public class SolarSystemAnimationPanel extends JPanel implements ActionListener {

	// creating 
	private Ellipse2D.Double planet;
	private Ellipse2D.Double sun;
	private static final long serialVersionUID = 1L;
	BasicSolarSystem ss = new BasicSolarSystem();
	private final int delay = 1;
	private Timer animationTimer;
	
	// numbers to divide distance and radius by to ensure good scaling 
	private static double distRatio = 4e8;
	private static double radRatio = 1e5;
	
	// radius of the sun
	private int sunR = 80;
	
	// method to create an animation with the sun at the center
	SolarSystemAnimationPanel(int width, int height) {
		setPreferredSize(new Dimension(width,height));

		sun = new Ellipse2D.Double(0, 0, sunR, sunR);
		animationTimer = new Timer(delay,this);
		animationTimer.start();
	}
	
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// create a Graphics2D object to make ellipses
		Graphics2D g2d = (Graphics2D)g;
		int height = getHeight();
		int width = getWidth();
		// Fill in background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		// Now move origin to centre of panel
		g.translate(width/2, height/2);
		
		// go through all planets and redraw them at their updated position 
		for (Planet p:ss.planets) {
			g.setColor(p.shade);
			g2d.fillOval((int) (p.pos[0]/distRatio), (int) (p.pos[1]/distRatio), 
					(int) (p.radius/radRatio), (int) (p.radius/radRatio));
		}
		
		// fill the sun in
		g.setColor(new Color(255,77,1));
		g2d.fill(sun);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		// update planets position and then repaint
		ss.updatedPlanets(ss);
		repaint();
		
	}
	
	 /** Start the animation */
	 public void start() {animationTimer.start();}
	 /** Stop the animation */
	 public void stop() {animationTimer.stop();}


}
