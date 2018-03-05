package g_testing_v3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class SimulationFrameMaker {
	// initiate simulator properties
	ArrayList<OilDrop> droplets = new ArrayList<OilDrop>();
	ArrayList<Waveform> waveField = new ArrayList<Waveform>();
	ArrayList<Pixel> allPixels = new ArrayList<Pixel>();
	
	//initiate timestep counter n
	double n =0;
	//initiating GUI objects
	JFrame frame;
    DrawPanel drawPanel; 
    int framesizeX;
    int framesizeY;
    int pixelSize;
    double zoom;
    TwoVector startPos;
    TwoVector velocity;
    
    
  //constructor --> initiates droplet at centre of frame with zero velocity
    public SimulationFrameMaker(int framesizeX, int framesizeY , int pixelSize, double zoom, ArrayList<OilDrop> droplets) {
    	this.framesizeX = framesizeX;
    	this.framesizeY = framesizeY;
    	this.droplets = droplets;
    	
    	//Creates new field (for 300 static fields)
    	while (n<300) {
    		for (int i = 0; i < droplets.size(); i++) {
    			OilDrop o = droplets.get(i);
    			waveField.add(new Waveform(o,n));
    		}
    		n = n+1;
    	}
		
    	
    	this.pixelSize = pixelSize;
    	this.zoom = zoom;
    	
    	//create pixel list
    	double interval = 1.0/zoom;
    	double xmin = -framesizeX * interval / 2;
    	double xmax = framesizeX * interval / 2;
    	double ymin = -framesizeY * interval / 2;
    	double ymax = framesizeY * interval / 2;
    	//initiate counters
    	for (int xCount = 0; xCount<(framesizeX/pixelSize) ; xCount ++) {
    		for(int yCount = 0; yCount<(framesizeY/pixelSize) ; yCount ++) {
    			TwoVector pos = new TwoVector(xmin + (interval*(double) (xCount*pixelSize)),ymin + (interval*(double)(yCount*pixelSize)));
    			TwoVector displayPos = new TwoVector(pixelSize*xCount, pixelSize*yCount);
    			allPixels.add(new Pixel(pos, displayPos));
    		}
    	}
    }
    
    // GUI
    // Performs simulation
    public void Simulate() {
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        drawPanel = new DrawPanel();
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(this.framesizeX,this.framesizeY);
        frame.setBackground(Color.BLACK);
        frame.add(drawPanel);
        
        run();
    }
    
    //initiate perturbation velocity
    double perturbVel=0.0005;
    
  //runs the simulation
    private void run() {
		
    	//Collect position data as a text file
    	//print particle location to file //initiate file output data
    	File outputfileParticle = new File("D:"+ File.separator +"Frames" + File.separator +"Particle Data" +Double.toString(perturbVel)+".csv");
    	FileWriter fwParticle ;
		BufferedWriter bParticle = null ;
		PrintWriter pwParticle = null ;
		
    	// initiates writers
		try {
			fwParticle = new FileWriter(outputfileParticle);
			bParticle = new BufferedWriter(fwParticle);
			pwParticle = new PrintWriter(bParticle);
		} catch (Exception e) {
			System.out.println(e);
		}
    	
		pwParticle.println("#n,#posx,#posy,#velx,#vely,#time");
    	
    	
    	while (n<4000 ) {
    		//determines time
			double time = n*droplets.get(0).T_F;
			
			// adds pertubation after 200th waveform formed
			if (n == 300) {
				for(OilDrop o : droplets) {
					o.velocity = new TwoVector (this.perturbVel, 0);
				}
			}
			
			//update all pixels
			//allPixels = Pixel.updateAllPix(allPixels, waveField, time);
			
			//updates all droplets and assumes the interact with the field at the given time
			for (OilDrop o : droplets) {
				//calculates gradient and using that the change in velocity due to interaction with the gradient of the particle
	    		TwoVector gradient = Waveform.fieldGradient(waveField, o.currentPos, time);
	    		TwoVector dVel = gradient.multiply( - o.T_F*0.01*o.F/o.mass_droplet);
	    		o.update(dVel);
			}
			
			//Creates new field
			for (int i = 0; i < droplets.size(); i++) {
				OilDrop o = droplets.get(i);
				waveField.add(new Waveform(o,n));
				pwParticle.println(n+ "," +o.currentPos + "," +o.velocity + ","+ time);
			}
			
			// removes any waveform from 200T_F s ago.
			if(waveField.size()>300) {
				waveField.remove(0);
			}
			
			/*frame.repaint();
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			
			/*//captures frame as screenshot
			Container content = frame.getContentPane();
            BufferedImage img = new BufferedImage(content.getWidth(), content.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = img.createGraphics();
            content.printAll(g2d);
            g2d.dispose();

            try {
                ImageIO.write(img, "png", new File("D:/Frames/frame"+String.valueOf((int)n)+".png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
			*/
			//increase time step
			n= n+1;
		}
    	
    	try {
			bParticle.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
	 // class to display objects in gui
   	class DrawPanel extends JPanel {
   		//Central point
   		TwoVector guiCenter = new TwoVector(framesizeX/2, framesizeY/2);
   		
   		public void paintComponent(Graphics g) {
   			//paints pixels
   			for (Pixel p : allPixels) {
   				// sets coloured appropriate to amplitude
   				if (p.amplitude >0) {
   					g.setColor(new Color( (int)((p.amplitude/0.06)*255.0) , 0 , 0 ) );
   					//(int)((1-(p.amplitude/0.05))*255.0)
   				}
   				else {
   					g.setColor(new Color(0, 0 , (int)((-p.amplitude/0.06)*255.0) ) );
   				}
   				g.fillRect((int)p.displayPos.getX(), (int)p.displayPos.getY(), pixelSize, pixelSize);
   			}
   			
   			//outputs the time
   			g.setColor(Color.WHITE);
   			BigDecimal time = new BigDecimal(n*droplets.get(0).T_F);
   			time = time.round(new MathContext(3));
   			g.drawString("Time = " + time + "s", 20,20);
   			/*g.drawString("Starting Position = ("+startPos.toString()+")", 20, 35);
   			g.drawString("Starting Velocity = ("+velocity.toString()+")", 20, 50);*/
   			
   			//Creating graphics for each droplet position
   			for (OilDrop o : droplets) {
   				g.setColor(Color.WHITE);
   				TwoVector displayPos = (o.currentPos.multiply(zoom)).add(guiCenter);
   				g.fillOval((int)displayPos.getX(), (int)displayPos.getY(), 5, 5);
   				//display information on the particle
   				g.drawString("Particle Position: ("+o.currentPos+")" , (int)20, (int)35);
   	   			g.drawString("Particle Velocity: ("+o.velocity+")" , (int)20, (int)50);
   	   			/*g.drawString("Particle Position: ("+o.currentPos+")" , (int)displayPos.getX()+12, (int)displayPos.getY()+22);
   	   			g.drawString("Particle Velocity: ("+o.velocity+")" , (int)displayPos.getX()+12, (int)displayPos.getY()+37);*/
   			}

   		}
   		
   	}

	public static void main(String[] args) {
		ArrayList<OilDrop> droplets = new ArrayList<OilDrop>();
		
		//Create droplets equidistant apart along a circle
		double numOfDroplets = 1;
		double radialDisplacement = 2*Math.PI/numOfDroplets;
		double radius = 0.002;

		/*for (double count =0 ; count < numOfDroplets; count ++) {
			TwoVector startPos = new TwoVector(Math.cos(radialDisplacement *count), Math.sin(radialDisplacement* count));
			TwoVector velocity = new TwoVector(Math.cos((radialDisplacement *count)+(Math.PI/2)), Math.sin((radialDisplacement *count)+(Math.PI/2)));
			System.out.println(startPos + "  ,  " + velocity);
			droplets.add(new OilDrop(startPos.multiply(0),velocity.multiply(radius*0.01)));
		}*/
		
		TwoVector startPos = new TwoVector(0,0);
		TwoVector velocity = new TwoVector(0,0);
		droplets.add(new OilDrop(startPos,velocity));
		
		SimulationFrameMaker simulator = new SimulationFrameMaker(1000,1000,4, 10000,droplets);
		// new SimulationFrameMaker(1000,1000,4, 25000,droplets); working parameters
		simulator.Simulate();
	}

}
