package prototype;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Rotating square animation applet with
 * start, stop and exit buttons.
 */
public class OilDropAnimationGuiPanel extends JPanel
implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OilDropAnimationPanel animPanel; // panel containing animation
	private JButton startButton;
	private JButton stopButton;
	private JButton exitButton;
	/** Create JPanel containing animation panel and buttons. */
	public OilDropAnimationGuiPanel() {
		super();
		setPreferredSize(new Dimension(200,200));
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		animPanel = new OilDropAnimationPanel(400,400);
		startButton = new JButton("Start");
		stopButton = new JButton("Stop");
		exitButton = new JButton("Exit");
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		exitButton.addActionListener(this);
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(
				buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.add(startButton);
		buttonPanel.add(stopButton);
		buttonPanel.add(exitButton);
		add(animPanel);
		add(buttonPanel);
	}
	/** Respond to button clicks */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==startButton) start();
		else if (e.getSource()==stopButton) stop();
		else if (e.getSource()==exitButton) System.exit(0);
	}
	/** Start animation when applet is started */
	public void start() {animPanel.start();}
	/** Stop animation when applet is stopped */
	public void stop() {animPanel.stop();}

}