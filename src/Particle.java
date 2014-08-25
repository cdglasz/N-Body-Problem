import java.awt.Color;
import java.awt.Graphics;

/**
 * The Particle class defines a particle, with a mass, a location, a speed, a
 * net force, and a color.
 * 
 * @author Christopher Glasz
 */
public class Particle {

	/**
	 * The location of the particle
	 */
	protected double x, y;

	/**
	 * The current speed of the particle
	 */
	protected double xSpeed, ySpeed;

	/**
	 * The x component of the current net force acting on the particle
	 */
	protected double xNetForce;

	/**
	 * The y component of the current net force acting on the particle
	 */
	protected double yNetForce;

	/**
	 * The mass of the particle
	 */
	protected static double mass;

	/**
	 * The radius of the particle
	 */
	protected static int radius;

	/**
	 * The color of the particle
	 */
	protected Color color;

	/**
	 * The constructor creates a particle at the given location with a speed of
	 * zero and the standard mass
	 * 
	 * @param x
	 * @param y
	 */
	public Particle(double x, double y) {
		this.x = x;
		this.y = y;
		this.xSpeed = 0;
		this.ySpeed = 0;
		this.color = Color.BLACK;
	}

	/**
	 * Computes the net force the particle experiences as a result of other
	 * particles populating the passed quadtree
	 * 
	 * @param tree
	 */
	public void computeForce(Quadtree tree) {
		xNetForce = computeXForce(tree.getRoot());
		yNetForce = computeYForce(tree.getRoot());
	}

	/**
	 * Computes the magnitude of the net force acting on the particle
	 * 
	 * @param node
	 * @return the magnitude of the net force on the particle
	 */
	private double computeForceMagnitude(TreeNode node) {
		
		// Start at zero net force
		double netForce = 0;

		// Get the distance between the two nodes
		double dist = computeDistance(node);

		// If the distance is zero, we are looking at ourself: we exert no force
		// on ourselves, so we are skipped.
		if (dist > 0) {

			// If the node has no subquadrants, treat it like a particle and
			// calculate the force
			if (!node.isInternal()) {
				double m1 = mass;
				double m2 = node.getQuad().getTotalMass();
				double r = dist;
				netForce += (Universe.G * m1 * m2) / (r * r + Universe.epsilon);
			} else {
				
				// The width of the quadrant
				double s = node.getQuad().getWidth();
				
				// The distance to the node
				double d = dist;

				// If the node is sufficiently far away, just treat the whole
				// quadrant like a particle. Otherwise, keep delving deeper into
				// the tree
				if ((s / d) < Universe.theta) {
					double m1 = mass;
					double m2 = node.getQuad().getTotalMass();
					double r = dist;
					netForce += (Universe.G * m1 * m2)
							/ (r * r + Universe.epsilon);
				} else {
					netForce += computeForceMagnitude(node.getI());
					netForce += computeForceMagnitude(node.getII());
					netForce += computeForceMagnitude(node.getIII());
					netForce += computeForceMagnitude(node.getIV());
				}
			}
		}
		return netForce;
	}

	/**
	 * Recursively computes the net force acting on the particle in the x
	 * direction
	 * 
	 * @param node
	 * @return the x component of the net force acting on the particle
	 */
	protected double computeXForce(TreeNode node) {
		
		// Start at zero net force
		double xForce = 0;
		
		// Compute the distance between the two nodes
		double dist = computeDistance(node);
		
		// If the distance is zero, we are looking at ourself: we exert no force
		// on ourselves, so we are skipped.
		if (dist > 0) {
			
			// If the node has no subquadrants, treat it like a particle and
			// calculate the force
			if (!node.isInternal()) {
				double m1 = mass;
				double m2 = node.getQuad().getTotalMass();
				double r = dist;
				xForce += computeNormalizedX(node)
						* ((Universe.G * m1 * m2) / (r * r + Universe.epsilon));
			} else {

				// The width of the quadrant
				double s = node.getQuad().getWidth();

				// The distance to the node
				double d = dist;

				// If the node is sufficiently far away, just treat the whole
				// quadrant like a particle. Otherwise, keep delving deeper into
				// the tree
				if ((s / d) < Universe.theta) {
					double m1 = mass;
					double m2 = node.getQuad().getTotalMass();
					double r = dist;
					xForce += computeNormalizedX(node) * (Universe.G * m1 * m2)
							/ (r * r + Universe.epsilon);
				} else {
					xForce += computeXForce(node.getI());
					xForce += computeXForce(node.getII());
					xForce += computeXForce(node.getIII());
					xForce += computeXForce(node.getIV());
				}
			}
		}
		return xForce;
	}

	/**
	 * Recursively computes the net force acting on the particle in the y
	 * direction
	 * 
	 * @param node
	 * @return the y component of the net force acting on the particle
	 */
	protected double computeYForce(TreeNode node) {
		// For explanation, refer to the computeXForce method
		double yForce = 0;
		double dist = computeDistance(node);
		if (dist > 0) {
			if (!node.isInternal()) {
				double m1 = mass;
				double m2 = node.getQuad().getTotalMass();
				double r = dist;
				yForce += computeNormalizedY(node) * (Universe.G * m1 * m2)
						/ (r * r + Universe.epsilon);
			} else {
				double s = node.getQuad().getWidth();
				double d = dist;
				if ((s / d) < Universe.theta) {
					double m1 = mass;
					double m2 = node.getQuad().getTotalMass();
					double r = dist;
					yForce += computeNormalizedY(node) * (Universe.G * m1 * m2)
							/ (r * r + Universe.epsilon);
				} else {
					yForce += computeYForce(node.getI());
					yForce += computeYForce(node.getII());
					yForce += computeYForce(node.getIII());
					yForce += computeYForce(node.getIV());
				}
			}
		}
		return yForce;
	}

	/**
	 * Computes the distance between the particle and the particle the passed in
	 * node points to
	 * 
	 * @param node
	 * @return the distance between the particle and the particle the passed in
	 *         node points to
	 */
	protected double computeDistance(TreeNode node) {
		return computeDistance(node.getQuad().getComX(), node.getQuad()
				.getComY());
	}

	/**
	 * Computes the distance between the particle and the passed in coordinates
	 * 
	 * @param x
	 * @param y
	 * @return the distance between the particle and the coordinates passed in
	 */
	private double computeDistance(double x, double y) {
		double dx = this.x - x;
		double dy = this.y - y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Computes the x component of the unit vector pointing from the particle to
	 * the particle pointed to by the passed node
	 * 
	 * @param node
	 * @return the x component of the unit vector pointing from the particle to
	 *         the particle pointed to by the passed node
	 */
	protected double computeNormalizedX(TreeNode node) {
		double dist = computeDistance(node);
		double dx = node.getQuad().getComX() - x;
		return dx / dist;
	}

	/**
	 * Computes the y component of the unit vector pointing from the particle to
	 * the particle pointed to by the passed node
	 * 
	 * @param node
	 * @return the y component of the unit vector pointing from the particle to
	 *         the particle pointed to by the passed node
	 */
	protected double computeNormalizedY(TreeNode node) {
		double dist = computeDistance(node);
		double dy = node.getQuad().getComY() - y;
		return dy / dist;
	}

	/**
	 * Computes the x component of the unit vector pointing from the particle to
	 * the passed in coordinates
	 * 
	 * @param x
	 * @param y
	 * @return the x component of the unit vector pointing from the particle to
	 *         the passed in coordinates
	 */
	private double computeNormalizedX(double x, double y) {
		double dist = computeDistance(x, y);
		double dx = x - this.x;
		return dx / dist;
	}

	/**
	 * Computes the y component of the unit vector pointing from the particle to
	 * the passed in coordinates
	 * 
	 * @param x
	 * @param y
	 * @return the y component of the unit vector pointing from the particle to
	 *         the passed in coordinates
	 */
	private double computeNormalizedY(double x, double y) {
		double dist = computeDistance(x, y);
		double dy = y - this.y;
		return dy / dist;
	}

	/**
	 * Gives the particle the proper speed to put it in a completely ineccentric
	 * orbit about the particle passed in
	 * 
	 * @param x
	 * @param y
	 * @param radius
	 * @param density
	 */
	public void giveCircularOrbit(double x, double y, double radius,
			double density) {
		
		// Distance from the center
		double r = computeDistance(x, y);

		// Total mass of the system
		double m = Math.PI * radius * radius * density;

		// Standard Gravitational Parameter
		double stdGravParam = Universe.G * (m + mass);

		// The orbital velocity of a particle at the edge of the system
		double outerOrbitalVelocity = Math.sqrt(stdGravParam / (radius));

		// The orbital velocity of the particle
		double orbitalVelocity = outerOrbitalVelocity * (r / radius);

		// The x component of the unit vector pointing towards the center
		double dx = computeNormalizedX(x, y);

		// The y component of the unit vector pointing towards the center
		double dy = computeNormalizedY(x, y);

		// The x component of the the normal to the unit vector
		double dxNormal = -1.0 * dy;

		// The y component of the the normal to the unit vector
		double dyNormal = dx;

		// The x speed of the particle
		xSpeed = (dxNormal * orbitalVelocity);

		// The y speed of the particle
		ySpeed = (dyNormal * orbitalVelocity);
	}

	/**
	 * Moves the particle according to its current position, speed, and the net
	 * force acting on it
	 */
	public void advance() {
		double xAcceleration = xNetForce / mass;
		double yAcceleration = yNetForce / mass;

		xSpeed += xAcceleration;
		ySpeed += yAcceleration;

		x += xSpeed;
		y += ySpeed;
	}

	/**
	 * Paints the particle
	 * 
	 * @param pane
	 */
	public void paint(Graphics pane) {
		pane.setColor(color);
		pane.fillOval((int) (x - radius / 2), (int) (y - radius / 2), radius,
				radius);
	}

	/**
	 * @param quad
	 * @return True if particle is within the quadrant. False otherwise.
	 */
	public boolean isInside(Quadrant quad) {
		return quad.contains(this);
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the ySpeed
	 */
	public double getYSpeed() {
		return ySpeed;
	}

	/**
	 * @param ySpeed
	 *            the ySpeed to set
	 */
	public void setYSpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}

	/**
	 * @return the xSpeed
	 */
	public double getXSpeed() {
		return xSpeed;
	}

	/**
	 * @param xSpeed
	 *            the xSpeed to set
	 */
	public void setXSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Sets the particles color according to the net force acting on it. The
	 * larger the net force, the warmer the color. particles with low net force
	 * are a subdued blue.
	 */
	public void colorByForce() {
		
		// Take the shortcut to finding net force
		double netForce = Math.sqrt(xNetForce * xNetForce + yNetForce
				* yNetForce);
		
		// An arbitrary number that results in a reasonable force range
		double S = 3e10;
		double L = netForce / S;

		// Red, green, blue
		double R, G, B;

		// More arbitrary numbers that color particles between dull blue to
		// bright yellow
		R = Math.min(60 + L * 115, 255);
		G = Math.min(90 + L * 35, 255);
		B = Math.max(125 - L * 45, 80);

		// Set to that color
		color = new Color((int) R, (int) G, (int) B, 255);
	}
}
