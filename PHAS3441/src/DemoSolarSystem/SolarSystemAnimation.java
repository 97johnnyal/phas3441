package DemoSolarSystem;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SolarSystemAnimation {
	
	/** Create and display JFrame containing animation GUI panel */
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				JFrame frame = new JFrame("Animation demo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(500, 500);
				//JPanel panel = new SolarSystemAnimationGuiPanel();
				//frame.add(panel);
				frame.setVisible(true);
			}
		});
	}
}