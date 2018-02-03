package g_testing_v2;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import java.util.concurrent.*;

//v2
public class Simulator {
	//initiating relevant objects
	JFrame frame;
    ArrayList<DrawPanel> allPanels = new ArrayList<DrawPanel>();
    //ArrayList<Pixel> allPixels = new ArrayList<Pixel>();
    int framesizeX;
    int framesizeY;
    int nThreads = 8;
    int pixelSize =4;
    
    //constructor
	public Simulator(int framesizeX, int framesizeY, int nThreads, int pixelSize) {
		this.framesizeX = framesizeX;
		this.framesizeY = framesizeY;
		this.nThreads = nThreads;
		this.pixelSize = pixelSize;
		int divider = framesizeX/(pixelSize*nThreads);
		//for each thread
		for(int threadNum=0; threadNum<nThreads; threadNum++) {
			ArrayList<Pixel> threadPixels = new ArrayList<Pixel>();
			//for all the i rows belonging to this thread
			for (int i = threadNum*divider; i < (threadNum+1)*divider; i++) {
				//for all j columns
				for (int j = 0; j<framesizeY/pixelSize; j++) {
					Pixel genPix = new Pixel(new TwoVector(i*pixelSize,j*pixelSize),i*j);
					threadPixels.add(genPix);
				}
			}
			//create DrawPanel with arrayList
			DrawPanel threadPanel = new DrawPanel(threadPixels, pixelSize);
			allPanels.add(threadPanel);
		}
	}
    
	// Performs simulation
    private void Simulate() {
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(this.framesizeX,this.framesizeY);
        frame.setBackground(Color.BLACK);
        GridLayout grid = new GridLayout(8,1);
        frame.getContentPane().add(GridLayout(8,0), allPanels.get(0));
    	frame.add(allPanels.get(0));
    	frame.getContentPane().add(BorderLayout.NORTH, allPanels.get(1));
    	frame.add(allPanels.get(1));
    	frame.getContentPane().add(BorderLayout.CENTER, allPanels.get(2));
    	frame.add(allPanels.get(2));
    	frame.getContentPane().add(BorderLayout.CENTER, allPanels.get(3));
    	frame.add(allPanels.get(3));
    }
    
    public static void main(String[] args) {
		Simulator test = new Simulator(1000,1000,4,4);
		test.Simulate();
	}
}
