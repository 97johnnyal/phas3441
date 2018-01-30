package johnnyTrial;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class OilDropAnimaion {
	
	/** Create and display JFrame containing animation GUI panel */
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			public void run() {
				JFrame frame = new JFrame("Animation demo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(1500, 1500);
				JPanel panel = new OilDropAnimationGuiPanel();
				frame.add(panel);
				frame.setVisible(true);
			}
		});
	}
}