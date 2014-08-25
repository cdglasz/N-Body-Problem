import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

/**
 * The Simulation class is the Applet.
 * 
 * @author Christopher Glasz
 */
public class Simulation extends Applet {

	/**
	 * The Universe
	 */
	private Universe myUniverse;

	public static final Font myFont = new Font("Small Font", Font.PLAIN, 10);

	/**
	 * Constant to hold the width of the window
	 */
	public static final int WINDOW_WIDTH = (int) Toolkit.getDefaultToolkit()
			.getScreenSize().getWidth();

	/**
	 * Constant to hold the height of the window
	 */
	public static final int WINDOW_HEIGHT = (int) (GraphicsEnvironment
			.getLocalGraphicsEnvironment().getMaximumWindowBounds().height * 0.95);

	/**
	 * The main class the starts the ball rolling
	 */
	public void init() {
		// Our universe
		myUniverse = new Universe(this);

		// size of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screenSize);
		
		// title the applet
		((Frame) this.getParent().getParent()).setTitle("GRAVITY n");
	}

	/**
	 * The top paint method, all other paint methods are called from here
	 */
	public void paint(Graphics pane) {
		// The empty blackness of space
		setBackground(Color.BLACK);
		
		// We want to see as much of the space as possible
		pane.setFont(myFont);
		
		if (myUniverse != null) {
			myUniverse.paint(pane);
		}
	}
}
