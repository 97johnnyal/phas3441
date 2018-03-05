package g_testing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Simulator implements ActionListener {
	//initiating relevant objects
	JFrame frame;
    DrawPanel drawPanel; 
    ArrayList<Pixel> allPixels = new ArrayList<Pixel>();
    int framesizeX;
    int framesizeY;
    int nThreads = 8;
    int size =4;
    OilDrop droplet1;
    
    //NextPos Pixels
    ArrayList<Pixel> nexPosPixels = new ArrayList<Pixel>();
	
    /** Constructor for SolarSystem object
     * @param ArrayList containing OrbitingObj objects for simulation
     */
    public Simulator(int framesizeX, int framesizeY,double frequency) {
    	this.framesizeX = framesizeX;
    	this.framesizeY = framesizeY;
    	droplet1 = new OilDrop(frequency,new TwoVector((double)framesizeX/2,(double)framesizeY/2));
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
    	while (true) {

    		// pause time to wait for repaint to finish
    		 try{
				 Thread.sleep(45);
			 } catch (Exception exc){}
    		
    		// determine calculation start time
    		long start = System.currentTimeMillis();
    		
    		ExecutorService threadPool = Executors.newFixedThreadPool(this.nThreads);
    		List<Future<ArrayList<Pixel>>> futures = new ArrayList<Future<ArrayList<Pixel>>>();
    		//initiate new arraylist to hold all animated pixels
    		ArrayList<Pixel> allAnimatedPixelList = new ArrayList<Pixel>();
    		
    		//creates task per thread
    		for (int iThread = 0; iThread < this.nThreads; ++iThread) {
    			PixelSetterTask task = new PixelSetterTask(this.allPixels, iThread, this.nThreads,this.droplet1,time);
    			Future<ArrayList<Pixel>> future = threadPool.submit(task);
    			futures.add(future);
    		}
    		
    		// performs animation
    		for (int iThread = 0; iThread < nThreads; ++iThread) {
    			ArrayList<Pixel> animatedPixelList = null;
    			try {
    				animatedPixelList = futures.get(iThread).get();
    			} catch (Exception e) {
    				System.out.println(e);
    			}
    			allAnimatedPixelList.addAll(animatedPixelList);
    			
    		}
    		
    		threadPool.shutdown();
    		threadPool.shutdownNow();
    		this.allPixels.clear();
    		this.allPixels = allAnimatedPixelList;
    		
    		/*//find mostlikely pixel
    		Pixel mostLikely = new Pixel(new TwoVector(0,0),0);
    		for (Pixel p: this.allPixels) {
    			if(p.positionGuess > mostLikely.amplitude)
    				mostLikely = p;
    		}
    		
    		mostLikely.colorIntensity=255;*/
    		
    		// determines next poisiton
    		NextPosGenerator nextPos = new NextPosGenerator(0.5,this.nThreads, this.droplet1,time);
    		//System.out.println(nextPos.generate());
    		TwoVector nextPosition = nextPos.generate();
    		
    		//Create pixel for next position
    		nexPosPixels.add(new Pixel(nextPosition, 0));
    		
    		
    		if (nextPosition.getX()<this.framesizeX && nextPosition.getX() > 0 &&
    				nextPosition.getY()< this.framesizeY && nextPosition.getY() >0) {
    			//this.droplet1.updateDet(nextPosition);
    		}
    		else {
    			//this.droplet1.reset();
    		}
    		
    		//increase Time Step
    		//time = time + 0.01;
    		//repaint to reset screen
			 try{
				 Thread.sleep(0);
			 } catch (Exception exc){}
			frame.repaint();
			
			// determine end time and time taken
			long end = System.currentTimeMillis();
			long timeElapsed1 = end - start;
			System.out.println(timeElapsed1+ "");
    	}
    }
    
    /** DrawPanel class that extends JPanel
	 * creates graphics to be displayed in simulation
	 * @author geach
	 * @version 1.1 (27/12/2017)
	 */
	class DrawPanel extends JPanel {
		public void paintComponent(Graphics g) {
			//Creating graphics for each pixel
			for (Pixel p : allPixels) {
				g.setColor(new Color(p.colorIntensity,p.colorIntensity,p.colorIntensity));
				g.fillRect((int)p.position.getX(), (int)p.position.getY(), size, size);
			}
			
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
		Simulator test = new Simulator(1000,1000,20);
		test.Simulate();
	}

	
}
