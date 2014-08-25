import java.awt.Color;
import java.awt.Graphics;

/**
 * The Particle System class defines a system of particles. It has a number of
 * particles, and a radius which describes the maximum lateral distance a
 * particle can reach from the center of the system before being removed.
 * 
 * @author Christopher Glasz
 */
public class ParticleSystem3D extends ParticleSystem {

	/**
	 * The constructor instantiates an empty list of particles
	 */
	public ParticleSystem3D() {
		super();
	}

	/**
	 * Populates the octree with the particles of the system.
	 */
	protected void populateTree() {
		// For explanation, see comments in the ParticleSystem class
		myTree = new Octree(new Octant((Universe.WINDOW_WIDTH / 2)
				- SYSTEM_RADIUS, (Universe.WINDOW_HEIGHT / 2) - SYSTEM_RADIUS,
				(Universe.WINDOW_HEIGHT / 2) - SYSTEM_RADIUS,
				SYSTEM_RADIUS * 2, SYSTEM_RADIUS * 2, SYSTEM_RADIUS * 2));

		for (Node current = head; current != null; current = current.getNext()) {
			if ((((TreeNode3D) (myTree.getRoot())).getOct()
					.contains(((Particle3D) current.getParticle()))))
				((Octree) myTree).insertParticle(((Particle3D) current
						.getParticle()));
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
			((Particle3D) current.getParticle()).computeForce((Octree) myTree);
		}
	}

	/**
	 * Colors each particle according to the net force acting on it
	 */
	protected void colorParticles() {
		if (Universe.colorByForce) {
			for (Node current = head; current != null; current = current
					.getNext()) {
				((Particle3D) current.getParticle()).colorByForce();
			}
		} else {
			for (Node current = head; current != null; current = current
					.getNext()) {
				((Particle3D) current.getParticle()).setColor(Color.WHITE);
			}
		}
	}

	/**
	 * Moves each particle according to its current speed and the net force
	 * acting on it
	 */
	protected void moveParticles() {
		// For explanation, see comments in the ParticleSystem class
		double xMassCenter = ((TreeNode3D) myTree.getRoot()).getOct().getComX();
		double yMassCenter = ((TreeNode3D) myTree.getRoot()).getOct().getComY();
		double zMassCenter = ((TreeNode3D) myTree.getRoot()).getOct().getComZ();

		double xMassCenterSpeed = ((TreeNode3D) myTree.getRoot()).getOct()
				.getComXSpeed();
		double yMassCenterSpeed = ((TreeNode3D) myTree.getRoot()).getOct()
				.getComYSpeed();
		double zMassCenterSpeed = ((TreeNode3D) myTree.getRoot()).getOct()
				.getComZSpeed();

		double xWindowCenter = Universe.WINDOW_WIDTH / 2.0;
		double yWindowCenter = Universe.WINDOW_HEIGHT / 2.0;
		double zWindowCenter = Universe.WINDOW_HEIGHT / 2.0;

		double addToX, addToY, addToZ;

		addToX = Math.round((xWindowCenter - xMassCenter));
		addToY = Math.round((yWindowCenter - yMassCenter));
		addToZ = Math.round((zWindowCenter - zMassCenter));

		for (Node current = head; current != null; current = current.getNext()) {
			((Particle3D) current.getParticle()).advance();
			if (Universe.followCenter) {
				((Particle3D) current.getParticle()).setX(((Particle3D) current
						.getParticle()).getX() + addToX);
				((Particle3D) current.getParticle()).setY(((Particle3D) current
						.getParticle()).getY() + addToY);
				((Particle3D) current.getParticle()).setZ(((Particle3D) current
						.getParticle()).getZ() + addToZ);

				((Particle3D) current.getParticle())
						.setXSpeed(((Particle3D) current.getParticle())
								.getXSpeed() - xMassCenterSpeed);
				((Particle3D) current.getParticle())
						.setYSpeed(((Particle3D) current.getParticle())
								.getYSpeed() - yMassCenterSpeed);
				((Particle3D) current.getParticle())
						.setZSpeed(((Particle3D) current.getParticle())
								.getZSpeed() - zMassCenterSpeed);
			}
		}
	}

	/**
	 * Displays the system of particles (or, if indicated, the quadtree)
	 * 
	 * @param pane
	 */
	public void paint(Graphics pane) {
		// For explanation, see comments in the ParticleSystem class
		if (Universe.showTree && myTree != null) {
			pane.setColor(Color.WHITE);
			myTree.paint(pane);
		} else {
			for (Node current = head; current != null; current = current
					.getNext())
				((Particle3D) current.getParticle()).paint(pane);
		}
		pane.setColor(Color.white);
		int y = Simulation.WINDOW_HEIGHT;
		y -= pane.getFontMetrics().getHeight();

		pane.drawString(String.format("%.3f Physics Calculations per Second",
				Universe.calcsPS), 10, y);

		y -= pane.getFontMetrics().getHeight();

		pane.drawString(Universe.timesteps + " Time Steps", 10, y);

		y -= pane.getFontMetrics().getHeight();

		pane.drawString(particleCount + " Particles", 10, y);
	}

}
