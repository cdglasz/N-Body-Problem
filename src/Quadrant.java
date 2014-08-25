import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

/**
 * Quadrant class defines a square field which has dimensions and a list of
 * particles contained within it
 * 
 * @author Christopher Glasz
 */
public class Quadrant extends Rectangle2D.Double {

	/**
	 * The location and magnitude of the center of mass of the quadrant
	 */
	protected double totalMass;

	/**
	 * The x coordinate of the center of mass of the quadrant
	 */
	protected double comX;

	/**
	 * The y coordinate of the center of mass of the quadrant
	 */
	protected double comY;

	/**
	 * The x component of the speed of the center of mass of the quadrant
	 */
	protected double comXSpeed;

	/**
	 * The y component of the speed of the center of mass of the quadrant
	 */
	protected double comYSpeed;

	/**
	 * Boolean hold whether the quadrant already holds a particle
	 */
	protected boolean isFull;

	/**
	 * The particle that the quadrant holds
	 */
	protected Particle particle;

	/**
	 * The constructor creates a quadrant with the given dimensions
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Quadrant(double x, double y, double width, double height) {
		super(x, y, width, height);
		particle = null;
		isFull = false;
		totalMass = 0;
		comX = 0;
		comY = 0;
		comXSpeed = 0;
		comYSpeed = 0;
	}

	/**
	 * Inserts a particle into the quadrant and alters its center of mass
	 * accordingly
	 * 
	 * @param particle
	 * @throws QuadrantException
	 */
	public void insertParticle(Particle particle) throws QuadrantException {
		if (this.particle != null)
			throw new QuadrantException("Tried to add to a full quadrant.");
		this.particle = particle;
		isFull = true;

		// This is guaranteed to be the first particle in the quadrant, so the
		// center of mass is essentially the particle itself
		totalMass = Particle.mass;
		comX = particle.getX();
		comY = particle.getY();
		comXSpeed = particle.getXSpeed();
		comYSpeed = particle.getYSpeed();
	}

	/**
	 * Returns true if the quadrant is full
	 * 
	 * @return true if the quadrant is full
	 */
	public boolean isFull() {
		return isFull;
	}

	/**
	 * Empties the quadrant of particles
	 */
	public void empty() {
		particle = null;
		isFull = false;
		totalMass = 0;
		comX = 0;
		comY = 0;
		comXSpeed = 0;
		comYSpeed = 0;
	}

	/**
	 * Resets the quadrant
	 */
	public void reset() {
		empty();
		comX = 0;
		comY = 0;
		totalMass = 0;

	}

	/**
	 * Returns true if the quadrant contains the given particle
	 * 
	 * @param particle
	 * @return true if the quadrant contains the given particle
	 */
	public boolean contains(Particle particle) {
		return super.contains(particle.getX(), particle.getY());
	}

	/**
	 * Returns true if the quadrant contains the given coordinates
	 * 
	 * @param particle
	 * @return true if the quadrant contains the given coordinates
	 */
	public boolean contains(double x, double y) {
		return super.contains(x, y);
	}

	/**
	 * Updates the quadrants center of mass to include the given particle
	 * 
	 * @param particle
	 */
	public void updateCom(Particle particle) {
		comX = (comX * totalMass + Particle.mass * particle.getX())
				/ (totalMass + Particle.mass);
		comY = (comY * totalMass + Particle.mass * particle.getY())
				/ (totalMass + Particle.mass);
		
		comXSpeed = (comXSpeed * totalMass + Particle.mass
				* particle.getXSpeed())
				/ (totalMass + Particle.mass);
		comYSpeed = (comYSpeed * totalMass + Particle.mass
				* particle.getYSpeed())
				/ (totalMass + Particle.mass);
		
		totalMass = totalMass + Particle.mass;
	}

	/**
	 * Displays the quadrant's borders
	 * 
	 * @param pane
	 */
	public void paintBounds(Graphics pane) {
		// Draw the bounds of the quadrant
		pane.drawRect((int) x, (int) y, (int) width, (int) height);
		
		// If theres particles inside, draw them too
		if (totalMass != 0) {
			if (isFull) {
				// If theres a particle in the quadrant, fill the circle
				pane.fillOval((int) (comX - Particle.radius / 2),
						(int) (comY - Particle.radius / 2), Particle.radius,
						Particle.radius);
			} else {
				// If not, just indicate the center of mass
				pane.setColor(Color.darkGray);
				pane.drawOval((int) (comX - Particle.radius / 2),
						(int) (comY - Particle.radius / 2), Particle.radius,
						Particle.radius);
				pane.setColor(Color.white);
			}
		}
	}

	/**
	 * Returns the quadrant's particle
	 * 
	 * @return the quadrant's particle
	 */
	public Particle getParticle() {
		return particle;
	}

	/**
	 * Returns the x value of the quadrant's center of mass
	 * 
	 * @return the x value of the quadrant's center of mass
	 */
	public double getComX() {
		return comX;
	}

	/**
	 * Returns the y value of the quadrant's center of mass
	 * 
	 * @return the y value of the quadrant's center of mass
	 */
	public double getComY() {
		return comY;
	}

	/**
	 * @return the comXSpeed
	 */
	public double getComXSpeed() {
		return comXSpeed;
	}

	/**
	 * @param comXSpeed
	 *            the comXSpeed to set
	 */
	public void setComXSpeed(double comXSpeed) {
		this.comXSpeed = comXSpeed;
	}

	/**
	 * @return the comYSpeed
	 */
	public double getComYSpeed() {
		return comYSpeed;
	}

	/**
	 * @param comYSpeed
	 *            the comYSpeed to set
	 */
	public void setComYSpeed(double comYSpeed) {
		this.comYSpeed = comYSpeed;
	}

	/**
	 * Returns the quadrant's total mass
	 * 
	 * @return the quadrant's total mass
	 */
	public double getTotalMass() {
		return totalMass;
	}

	/**
	 * Exception thrown when dealing with quadrants
	 * 
	 * @author Christopher Glasz
	 */
	public class QuadrantException extends Exception {
		/**
		 * The default constructor creates an exception with a default message
		 */
		public QuadrantException() {
			super("Particle System Exception");
		}

		/**
		 * Creates an exception with the passed message
		 * 
		 * @param message
		 */
		public QuadrantException(String message) {
			super(message);
		}
	}

}
