import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TimeSpeed extends JPanel {
	JFrame frame;
	private static final int INDEX = 1;
	private FeedForward feedForward;
	private double totalTime, timePerRound, currentTime, prevTime = 0;

	public TimeSpeed(double setPoint, double timePerRound) {
		this.currentTime = timePerRound;
		this.feedForward = new FeedForward(0, 3.5, 0.7, -0.3, setPoint);
		this.timePerRound = timePerRound;
		this.totalTime = feedForward.getTotalTime();
		initComponents();
	}

	public void initComponents() {
		this.setPreferredSize(new Dimension(1000, 800));
		frame = new JFrame("Time-Speed function");
		frame.setSize(new Dimension(1000, 800));
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		for (int i = 0; i < totalTime; i += timePerRound) {
			repaint();
			try {
				Thread.sleep((long) (timePerRound * 1000.0));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawLine((int) this.prevTime, (int) feedForward.getExpected(this.prevTime)[INDEX], (int) this.currentTime,
				(int) feedForward.getExpected(this.currentTime)[INDEX]);
		this.prevTime = this.currentTime;
		this.currentTime += timePerRound;
	}

	public static void main(String[] args) {
		new TimeSpeed(10, 1);
	}
}
