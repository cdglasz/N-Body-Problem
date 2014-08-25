import java.awt.Color;
import java.awt.Graphics;

/**
 * The Particle System class defines a system of particles. It has a number of
 * particles, and a radius which describes the maximum lateral distance a
 * particle can reach from the center of the system before being removed.
 * 
 * @author Christopher Glasz
 */
public class ParticleSystem {
	/**
	 * A constant to define the size of the system
	 */
	public static final double SYSTEM_RADIUS = Universe.WINDOW_WIDTH * 5;

	/**
	 * A quadtree which is populated with particles and effectively summarizes
	 * the system of particles for more efficient force calculations
	 */
	protected Quadtree myTree;

	/**
	 * The head of a list of particles
	 */
	protected Node head;

	/**
	 * The number of particles in the system
	 */
	protected int particleCount;

	/**
	 * The constructor instantiates an empty list of particles
	 */
	public ParticleSystem() {
		head = null;
		particleCount = 0;
	}

	/**
	 * Simulates the system at the next time step
	 */
	public void simulate() {
		// First, we make a quadtree and fill it with particles
		populateTree();
		
		// Then we calculate the forces acting on all the particles
		simulatePhysics();
		
		// Then we color the particles
		colorParticles();
		
		// Finally, we move everything
		moveParticles();
	}

	/**
	 * Populates the quadtree with the particles of the system.
	 */
	protected void populateTree() {
		// A quadtree to start with. It is one hundred times the size of the
		// viewing window
		myTree = new Quadtree(new Quadrant((Universe.WINDOW_WIDTH / 2)
				- SYSTEM_RADIUS, (Universe.WINDOW_HEIGHT / 2) - SYSTEM_RADIUS,
				SYSTEM_RADIUS * 2, SYSTEM_RADIUS * 2));
		
		// Go through each particle in the system
		for (Node current = head; current != null; current = current.getNext()) {
			
			// If it's in the bounds, throw it in
			if (myTree.getRoot().getQuad().contains(current.getParticle()))
				myTree.insertParticle(current.getParticle());
			
			// Otherwise, get rid of it
			else {
				Node placeHolder = current;
				try {
					remove(current);
				} catch (ParticleSystemException e) {
					e.printStackTrace();
				}
				current = placeHolder;
			}
		}
	}

	/**
	 * Simulates physics. Specifically, it applies the force acting on each
	 * particle.
	 */
	protected void simulatePhysics() {
		for (Node current = head; current != null; current = current.getNext()) {
			current.getParticle().computeForce(myTree);
		}
	}

	/**
	 * Colors each particle according to the net force acting on it
	 */
	protected void colorParticles() {
		if (Universe.colorByForce) {
			for (Node current = head; current != null; current = current
					.getNext()) {
				current.getParticle().colorByForce();
			}
		} else {
			for (Node current = head; current != null; current = current
					.getNext()) {
				current.getParticle().setColor(Color.WHITE);
			}
		}
	}

	/**
	 * Moves each particle according to its current speed and the net force
	 * acting on it
	 */
	protected void moveParticles() {
		
		// In case we want to center our view on the center of mass
		double xMassCenter = myTree.getRoot().getQuad().getComX();
		double yMassCenter = myTree.getRoot().getQuad().getComY();

		// To accomodate for center of mass wandering
		double xMassCenterSpeed = myTree.getRoot().getQuad().getComXSpeed();
		double yMassCenterSpeed = myTree.getRoot().getQuad().getComYSpeed();

		// The center of our window
		double xWindowCenter = Universe.WINDOW_WIDTH / 2.0;
		double yWindowCenter = Universe.WINDOW_HEIGHT / 2.0;

		// What to add to x
		double addToX = Math.round((xWindowCenter - xMassCenter));
		double addToY = Math.round((yWindowCenter - yMassCenter));

		// Go through each particle in the system and move it
		for (Node current = head; current != null; current = current.getNext()) {
			current.getParticle().advance();
			
			// If the user wants us to, recenter the view on the center of mass
			if (Universe.followCenter) {
				current.getParticle().setX(
						current.getParticle().getX() + addToX);
				current.getParticle().setY(
						current.getParticle().getY() + addToY);

				current.getParticle().setXSpeed(
						current.getParticle().getXSpeed() - xMassCenterSpeed);
				current.getParticle().setYSpeed(
						current.getParticle().getYSpeed() - yMassCenterSpeed);
			}
		}
	}

	/**
	 * Adds the given particle to the system
	 * 
	 * @param particle
	 */
	public void add(Particle particle) {
		Node newNode = new Node();
		newNode.setParticle(particle);
		if (head != null)
			head.setPrevious(newNode);
		newNode.setNext(head);
		head = newNode;
		particleCount++;
	}

	/**
	 * Removes the given particle from the system
	 * 
	 * @param particle
	 * @throws ParticleSystemException
	 */
	public void remove(Particle particle) throws ParticleSystemException {
		remove(findParticle(particle));
	}

	/**
	 * Removes the given node from the system
	 * 
	 * @param node
	 * @throws ParticleSystemException
	 */
	protected void remove(Node node) throws ParticleSystemException {
		if (isEmpty())
			throw new ParticleSystemException();
		if (node.getNext() != null)
			node.getNext().setPrevious(node.getPrevious());
		if (node.getPrevious() != null)
			node.getPrevious().setNext(node.getNext());
		if (node == head)
			head = head.getNext();
		particleCount--;
	}

	/**
	 * Finds the given particle in the system and returns the node pointing to
	 * it. If the particle is not found, an exception is thrown.
	 * 
	 * @param particle
	 * @return the node pointing to the given particle
	 * @throws ParticleSystemException
	 */
	private Node findParticle(Particle particle) throws ParticleSystemException {
		if (isEmpty())
			throw new ParticleSystemException("Empty list");
		Node node = null;
		Node current = head;
		boolean found = false;
		while (!found) {
			if (current.getParticle().equals(particle)) {
				node = current;
				found = true;
			} else {
				current = current.getNext();
			}
		}
		if (node == null)
			throw new ParticleSystemException("Particle not found");
		return node;
	}

	/**
	 * Gives whether or not the system is empty
	 * 
	 * @return true if the system has no particles
	 */
	public boolean isEmpty() {
		return head == null;
	}

	/**
	 * Displays the system of particles (or, if indicated, the quadtree)
	 * 
	 * @param pane
	 */
	public void paint(Graphics pane) {
		if (Universe.showTree && myTree != null) {
			pane.setColor(Color.WHITE);
			myTree.paint(pane);
		} else {
			for (Node current = head; current != null; current = current
					.getNext())
				current.getParticle().paint(pane);
		}
		
		// Now we give some useful information about the system
		pane.setColor(Color.white);
		int y = Simulation.WINDOW_HEIGHT;
		y -= pane.getFontMetrics().getHeight();

		// Calculations per second
		pane.drawString(String.format("%.3f Physics Calculations per Second",
				Universe.calcsPS), 10, y);
		y -= pane.getFontMetrics().getHeight();

		// Time elapsed
		pane.drawString(Universe.timesteps + " Time Steps", 10, y);
		y -= pane.getFontMetrics().getHeight();

		// The number of particles in the system
		pane.drawString(particleCount + " Particles", 10, y);
	}

	/**
	 * Exception thrown when dealing with particle systems
	 * 
	 * @author Christopher Glasz
	 */
	class ParticleSystemException extends Exception {
		/**
		 * The default constructor creates an exception with a default message
		 */
		public ParticleSystemException() {
			super("Particle System Exception");
		}

		/**
		 * Creates an exception with the passed message
		 * 
		 * @param message
		 */
		public ParticleSystemException(String message) {
			super(message);
		}
	}
}
