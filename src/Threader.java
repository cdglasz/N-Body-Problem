/**
 * The Threader class defines the thread that runs the physics of the simulation
 * 
 * @author Christopher Glasz
 */
public class Threader extends Thread {

	/**
	 * The class that's running the thread
	 */
	private ThreadListener runningClass;

	/**
	 * The default constructor just creates a thread and makes the running class
	 * null
	 */
	public Threader() {
		super("Threader");
		runningClass = null;
	}

	/**
	 * Sets the running class and creates the thread
	 * 
	 * @param runningClass
	 */
	public Threader(ThreadListener runningClass) {
		super("Threader");
		this.runningClass = runningClass;
	}

	/**
	 * Sets the running class, and creates the thread with a name
	 * 
	 * @param name
	 * @param runningClass
	 */
	public Threader(String name, ThreadListener runningClass) {
		super(name);
		this.runningClass = runningClass;
	}

	/**
	 * The run class just continuously calls the running class' time step method
	 */
	public void run() {
		while (true) {
			if (runningClass != null)
				runningClass.timeStep();
		}
	}
}
