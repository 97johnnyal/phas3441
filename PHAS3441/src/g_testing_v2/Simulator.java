package g_testing_v2;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.util.concurrent.*;


public class Simulator implements ActionListener {
	//initiating relevant objects
	JFrame frame;
    DrawPanel drawPanel; 
    ArrayList<Pixel> allPixels = new ArrayList<Pixel>();
    int framesizeX;
    int framesizeY;
    int nThreads = 8;
    int size =2;
    ArrayList<OilDrop> residualDrops = new ArrayList<OilDrop>();
    double frequency;
    OilDrop ogDrop;
    Random rand = new Random();
    
    //NextPos Pixels
    ArrayList<Pixel> nexPosPixels = new ArrayList<Pixel>();
	
    /** Constructor for SolarSystem object
     * Assumes reflection boundary at pixel = 50 line
     * @param ArrayList containing OrbitingObj objects for simulation
     */
    public Simulator(int framesizeX, int framesizeY,double frequency) {
    	this.framesizeX = framesizeX;
    	this.framesizeY = framesizeY;
    	this.frequency = frequency;
    	ogDrop = new OilDrop(new TwoVector((double)(framesizeX*1) - 450,(double)framesizeY*0.5), 1350.0, frequency,0);
    	//generate all pixels starting from top left to bottom right
    	for (int i =0; i < framesizeX/size; i++) {
    		for (int j =0; j < framesizeY/size; j++) {
    			Pixel genPix = new Pixel(new TwoVector(i*size,j*size),0);
    			allPixels.add(genPix);
    		}
    	}
    	
    }
    
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
        frame.add(drawPanel);
        run();
    }
    
    private void run() {
    	//initiate time counter
    	double time =0;
    	System.out.println(ogDrop.currPos +""+ogDrop.prevPos);
    	ogDrop.updateParam(new TwoVector(-45,0), time);
    	residualDrops.add(ogDrop);
    	residualDrops.add(ogDrop.mirrorDrop(100));
    	
    	//initiate file output data
    	File outputfile = new File(System.getProperty("user.home") + File.separator +"test1" +".txt");
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
    	
    	// limit for calculation
    	double maxX = 20000;
    	int calculationRound = 0;
    	
    	// determine simulation start time
		long startSim = System.currentTimeMillis();
    	
    	while (ogDrop.currPos.getX()<maxX) {
    		//System.out.println(ogDrop.currPos +""+ogDrop.prevPos);
    		
    		// pause time to wait for repaint to finish
    		 try{
				 Thread.sleep(20);
			 } catch (Exception exc){}
    		
    		// determine calculation start time
    		long start = System.currentTimeMillis();
    		
//    		PixelAmplitude pixelUpdater = new PixelAmplitude(this.nThreads,this.residualDrops,time,this.framesizeX,0,this.framesizeY,0,this.size);
//    		this.allPixels = pixelUpdater.UpdatedPixelList();
    		
    		//increase Time Step
    		//time = time + 0.01;
    		
    		//repaint to reset screen
			 try{
				 Thread.sleep(0);
			 } catch (Exception exc){}
			frame.repaint();
			
			// Det change in velocity = g * gradient * 0.01 / (2piOmega)
			TwoVector dVel = OilDrop.gradient(this.residualDrops, ogDrop.currPos).multiply(100 * 0.1 / (2*Math.PI*this.frequency));
			
			//print to data file
			pw.println(calculationRound+"    "+ogDrop.vel+"    "+ogDrop.currPos+"    "+ this.residualDrops.size());
			calculationRound++;
			ogDrop.updateParam(dVel, time);
			
			for (OilDrop o : residualDrops){
				o.decayUpdate(1);
			}
			
			
			residualDrops.add(ogDrop);
	    	residualDrops.add(ogDrop.mirrorDrop(100));
			
			// determine end time and time taken
			long end = System.currentTimeMillis();
			long timeElapsed1 = end - start;
			
			//System.out.println(timeElapsed1+ "ms, no. of residualDrops" + residualDrops.size());
			if(residualDrops.size() > 30000) {
				residualDrops.remove(0);
				residualDrops.remove(0);
			}
			//System.out.println("Grad at" + (droplets.get(0).currPos) + " = " + OilDrop.gradient(droplets, droplets.get(0).currPos));
    	}
    	
    	try {
			b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	// determine end time and time taken
		long endSim = System.currentTimeMillis();
		long timeElapsed2 = endSim - startSim;
    	
    	System.out.println("DONE, simulation time = " + timeElapsed2 + "ms" );
    }
    
    /** DrawPanel class that extends JPanel
	 * creates graphics to be displayed in simulation
	 * @author geach
	 * @version 1.1 (27/12/2017)
	 */
	class DrawPanel extends JPanel {
		public void paintComponent(Graphics g) {
			//guess peak in amplitude
			/*double ampAtCurrPos =0;
			for (OilDrop drop : residualDrops) {
				ampAtCurrPos = ampAtCurrPos + drop.strengthAtPt(ogDrop.currPos);
			}
			double resSum = Math.abs(ampAtCurrPos)*3;*/
			
			/*double resSum =2;
			//Creating graphics for each pixel
			for (Pixel p : allPixels) {
				if (p.position.getX()>50) {
					g.setColor(new Color((int)(p.colorIntensityR/resSum),0,(int)(p.colorIntensityB/resSum)));
					g.fillRect((int)p.position.getX(), (int)p.position.getY(), size+10, size);
				}
				
			}*/
			
			g.setColor(new Color(0,255,0));
			g.fillRect(46, 0, 4, 1000);
			g.fillRect((int)ogDrop.currPos.getX(), (int)ogDrop.currPos.getY(), 4, 4);
			
			//creating point for next pixel
			for (Pixel p : nexPosPixels) {
				g.setColor(new Color(255,0,0));
				g.fillRect((int)p.position.getX(), (int)p.position.getY(), 1, 1);
			}
		}
	}
    
	@Override //for buttons
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void main(String[] args) {
		Simulator test = new Simulator(1000,1000,80);
		test.Simulate();
	}

	
}
