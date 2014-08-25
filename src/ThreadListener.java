/**
 * ThreadListener defines a class that can have a threader run it
 * 
 * @author Christopher Glasz
 */
public interface ThreadListener {
	/**
	 * If the Applet is not paused and the particle system has been initialized,
	 * the method tells the system to simulate physics at each time step, and
	 * then displays the results in the Applet window.
	 */
	public void timeStep();
}
