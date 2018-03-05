package g_testing_v3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimulationDataCollector implements ActionListener{
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
    
    // boolean to control start stop
    boolean playBoolean = false;
    // Initiates Buttons
    JButton play;
    
    //constructor --> initiates droplet at centre of frame with zero velocity
    public SimulationDataCollector(int framesizeX, int framesizeY) {
    	this.framesizeX = framesizeX;
    	this.framesizeY = framesizeY;
    	TwoVector startPos = new TwoVector(0.0005, 0);
    	TwoVector velocity = new TwoVector(0,0.0005);
    	droplets.add(new OilDrop(startPos,velocity));
    	droplets.add(new OilDrop(startPos.multiply(-1),velocity.multiply(-1)));
    	waveField.add(new Waveform(droplets.get(0),n));
    	waveField.add(new Waveform(droplets.get(1),n));
    	
    	//create pixel list
    	double interval = 1.0/500000;
    	double xmin = -framesizeX * interval / 2;
    	double xmax = framesizeX * interval / 2;
    	double ymin = -framesizeY * interval / 2;
    	double ymax = framesizeY * interval / 2;
    	//initiate counters
    	for (int xCount = 0; xCount<framesizeX ; xCount ++) {
    		for(int yCount = 0; yCount<framesizeY ; yCount ++) {
    			TwoVector pos = new TwoVector(xmin + (interval*(double) xCount),ymin + (interval*(double)yCount));
    			allPixels.add(new Pixel(pos));
    		}
    	}
    }
    
    ///// GUI
    // Performs simulation
    private void Simulate() {
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        drawPanel = new DrawPanel();
        frame.getContentPane().add(BorderLayout.CENTER, drawPanel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(this.framesizeX,this.framesizeY);
        frame.setBackground(Color.BLACK);
        
        // insert button
        play = new JButton("Play");
        play.setLayout(null);
        play.setBounds(50,20,80,25);
        play.addActionListener(this);
        
        drawPanel.add(play);
        frame.add(drawPanel);
        
        run();
    }
    
    //performs button action
    public void actionPerformed(ActionEvent e) {
		// Sets what happens when timestep buttons are clicked
		if (e.getSource() == play) {
			if (this.playBoolean == false) {
				this.playBoolean = true;
			}
			else {
				this.playBoolean = false;
			}
		}
    }
    
    //runs the simulation
    private void run() {
    	//print particle location to file //initiate file output data
    	File outputfileParticle = new File("D:"+ File.separator +"Frame Data" + File.separator +"Particle Data" +".csv");
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
    	
		pwParticle.println("#n,#posx,#posy,#velx,#vely");
		
    	while (n<2 ) {
    		
    		if(playBoolean == true) {
				double time = n*droplets.get(0).T_F;
				
				allPixels = Pixel.updateAllPix(allPixels, waveField, time);
				
				// prints pixeldata to file
				//initiate file output data
				String filename = Double.toString(n);
		    	File outputfile = new File("D:"+ File.separator +"Frame Data" + File.separator +filename +".csv");
		    	FileWriter fw ;
				BufferedWriter b = null ;
				PrintWriter pw = null ;
				
		    	// initiates writers
				try {
					fw = new FileWriter(outputfile);
					b = new BufferedWriter(fw);
					pw = new PrintWriter(b);
				} catch (Exception e) {
					System.out.println(e);
				}
				
				pw.println("#n,#posx,#posy,#amp");
				
				for(Pixel p : allPixels) {
					pw.println(n+","+p.pos+","+p.amplitude);
				}
				try {
					b.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//updates all droplets and assumes the interact with the field at the given time
				for (OilDrop o : droplets) {
					//calculates gradient and using that the change in velocity due to interaction with the gradient of the particle
		    		TwoVector gradient = Waveform.fieldGradient(waveField, o.currentPos, time);
		    		TwoVector dVel = gradient.multiply(o.mass_droplet*o.g);
		    		o.update(dVel);
				}
				
				//Creates new field
				for (int i = 0; i < droplets.size(); i++) {
					OilDrop o = droplets.get(i);
					waveField.add(new Waveform(o,n));
					pwParticle.println(n+ "," +o.currentPos + "," +o.velocity);
				}
				
				// removes any waveform from 200T_F s ago.
				if(waveField.size()>200) {
					waveField.remove(0);
				}
				
				try {
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				frame.repaint();
				//increase time step
				n= n+1;
    		}
    		
    		else {
    			frame.repaint();
    		}
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
   			/*for (Pixel p : allPixels) {
   				TwoVector displayPos = (p.position.multiply(500000)).add(guiCenter);
   				double colorInt = (displayPos.magnitude() / 10000)*255;
   				System.out.println(colorInt);
   				g.setColor(new Color((int) colorInt,(int) colorInt,(int) colorInt));
   				g.fillRect((int)displayPos.getX(), (int)displayPos.getY(), (int)pixelSize, (int)pixelSize);
   			}*/
   			
   			g.setColor(Color.WHITE);
   			g.drawString("Time = " + (n*droplets.get(0).T_F) + "s", 20,20);
   			
   			//Creating graphics for each droplet position
   			for (OilDrop o : droplets) {
   				g.setColor(Color.WHITE);
   				TwoVector displayPos = (o.currentPos.multiply(500000)).add(guiCenter);
   				g.fillOval((int)displayPos.getX(), (int)displayPos.getY(), 5, 5);
   				//display information on the particle
   	   			g.drawString("Particle Position: ("+o.currentPos+")" , (int)displayPos.getX()+12, (int)displayPos.getY()+22);
   	   			g.drawString("Particle Velocity: ("+o.velocity+")" , (int)displayPos.getX()+12, (int)displayPos.getY()+37);
   			}

   		}
   		
   	}
    
	public static void main(String[] args) {
		SimulationDataCollector simulator = new SimulationDataCollector(1000,1000);
		simulator.Simulate();
	
	}
}
