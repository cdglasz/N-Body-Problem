import java.awt.Color;
import java.awt.Graphics;

/**
 * The Particle3D class defines a particle in 3 dimensional space with a mass, a
 * location, a speed, a net force, and a color.
 * 
 * @author Christopher Glasz
 */
public class Particle3D extends Particle {
	/**
	 * The z coordinate of the particle
	 */
	private double z;

	/**
	 * The z component of the current speed of the particle
	 */
	private double zSpeed;

	/**
	 * The z component of the current net force on the particle
	 */
	private double zNetForce;

	/**
	 * The constructor creates a particle at the specified location
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Particle3D(double x, double y, double z) {
		super(x, y);
		this.z = z;
		zSpeed = 0;
	}

	/**
	 * Computes the net force the particle experiences as a result of other
	 * particles populating the passed octree
	 * 
	 * @param tree
	 */
	public void computeForce(Octree tree) {
		xNetForce = computeXForce((TreeNode3D) tree.getRoot());
		yNetForce = computeYForce((TreeNode3D) tree.getRoot());
		zNetForce = computeZForce((TreeNode3D) tree.getRoot());
	}

	/**
	 * Computes the magnitude of the net force acting on the particle
	 * 
	 * @param node
	 * @return the magnitude of the net force on the particle
	 */
	private double computeForceMagnitude(TreeNode3D node) {
		// For explanation, see comments in the Particle class
		double netForce = 0;
		double dist = computeDistance(node);
		if (dist > 0) {
			if (!node.isInternal()) {
				double m1 = mass;
				double m2 = node.getOct().getTotalMass();
				double r = dist;
				netForce += (Universe.G * m1 * m2) / (r * r + Universe.epsilon);
			} else {
				double s = node.getOct().getWidth();
				double d = dist;
				if ((s / d) < Universe.theta) {
					double m1 = mass;
					double m2 = node.getOct().getTotalMass();
					double r = dist;
					netForce += (Universe.G * m1 * m2)
							/ (r * r + Universe.epsilon);
				} else {
					netForce += computeForceMagnitude(node.getI());
					netForce += computeForceMagnitude(node.getII());
					netForce += computeForceMagnitude(node.getIII());
					netForce += computeForceMagnitude(node.getIV());
					netForce += computeForceMagnitude(node.getV());
					netForce += computeForceMagnitude(node.getVI());
					netForce += computeForceMagnitude(node.getVII());
					netForce += computeForceMagnitude(node.getVIII());
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
	protected double computeXForce(TreeNode3D node) {
		// For explanation, see comments in the Particle class
		double xForce = 0;
		double dist = computeDistance(node);
		if (dist > 0) {
			if (!node.isInternal()) {
				double m1 = mass;
				double m2 = node.getOct().getTotalMass();
				double r = dist;
				xForce += computeNormalizedX(node)
						* ((Universe.G * m1 * m2) / (r * r + Universe.epsilon));
			} else {
				double s = node.getOct().getDepth();
				double d = dist;
				if ((s / d) < Universe.theta) {
					double m1 = mass;
					double m2 = node.getOct().getTotalMass();
					double r = dist;
					xForce += computeNormalizedX(node) * (Universe.G * m1 * m2)
							/ (r * r + Universe.epsilon);
				} else {
					xForce += computeXForce(node.getI());
					xForce += computeXForce(node.getII());
					xForce += computeXForce(node.getIII());
					xForce += computeXForce(node.getIV());
					xForce += computeXForce(node.getV());
					xForce += computeXForce(node.getVI());
					xForce += computeXForce(node.getVII());
					xForce += computeXForce(node.getVIII());
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
	protected double computeYForce(TreeNode3D node) {
		// For explanation, see comments in the Particle class
		double yForce = 0;
		double dist = computeDistance(node);
		if (dist > 0) {
			if (!node.isInternal()) {
				double m1 = mass;
				double m2 = node.getOct().getTotalMass();
				double r = dist;
				yForce += computeNormalizedY(node) * (Universe.G * m1 * m2)
						/ (r * r + Universe.epsilon);
			} else {
				double s = node.getOct().getDepth();
				double d = dist;
				if ((s / d) < Universe.theta) {
					double m1 = mass;
					double m2 = node.getOct().getTotalMass();
					double r = dist;
					yForce += computeNormalizedY(node) * (Universe.G * m1 * m2)
							/ (r * r + Universe.epsilon);
				} else {
					yForce += computeYForce(node.getI());
					yForce += computeYForce(node.getII());
					yForce += computeYForce(node.getIII());
					yForce += computeYForce(node.getIV());
					yForce += computeYForce(node.getV());
					yForce += computeYForce(node.getVI());
					yForce += computeYForce(node.getVII());
					yForce += computeYForce(node.getVIII());
				}
			}
		}
		return yForce;
	}

	/**
	 * Recursively computes the net force acting on the particle in the z
	 * direction
	 * 
	 * @param node
	 * @return the z component of the net force acting on the particle
	 */
	private double computeZForce(TreeNode3D node) {
		// For explanation, see comments in the Particle class
		double zForce = 0;
		double dist = computeDistance(node);
		if (dist > 0) {
			if (!node.isInternal()) {
				double m1 = mass;
				double m2 = node.getOct().getTotalMass();
				double r = dist;
				zForce += computeNormalizedZ(node) * (Universe.G * m1 * m2)
						/ (r * r + Universe.epsilon);
			} else {
				double s = node.getOct().getDepth();
				double d = dist;
				if ((s / d) < Universe.theta) {
					double m1 = mass;
					double m2 = node.getOct().getTotalMass();
					double r = dist;
					zForce += computeNormalizedZ(node) * (Universe.G * m1 * m2)
							/ (r * r + Universe.epsilon);
				} else {
					zForce += computeZForce(node.getI());
					zForce += computeZForce(node.getII());
					zForce += computeZForce(node.getIII());
					zForce += computeZForce(node.getIV());
					zForce += computeZForce(node.getV());
					zForce += computeZForce(node.getVI());
					zForce += computeZForce(node.getVII());
					zForce += computeZForce(node.getVIII());
				}
			}
		}
		return zForce;
	}

	/**
	 * Computes the x component of the unit vector pointing from the particle to
	 * the particle pointed to by the passed node
	 * 
	 * @param node
	 * @return the x component of the unit vector pointing from the particle to
	 *         the particle pointed to by the passed node
	 */
	protected double computeNormalizedX(TreeNode3D node) {
		double dist = computeDistance((TreeNode3D) node);
		double dx = node.getOct().getComX() - x;
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
	protected double computeNormalizedY(TreeNode3D node) {
		double dist = computeDistance((TreeNode3D) node);
		double dy = node.getOct().getComY() - y;
		return dy / dist;
	}

	/**
	 * Computes the z component of the unit vector pointing from the particle to
	 * the particle pointed to by the passed node
	 * 
	 * @param node
	 * @return the z component of the unit vector pointing from the particle to
	 *         the particle pointed to by the passed node
	 */
	protected double computeNormalizedZ(TreeNode3D node) {
		double dist = computeDistance(node);
		double dz = node.getOct().getComZ() - z;
		return dz / dist;
	}

	/**
	 * Computes the x component of the unit vector pointing from the particle to
	 * the passed in coordinates
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the x component of the unit vector pointing from the particle to
	 *         the passed in coordinates
	 */
	private double computeNormalizedX(double x, double y, double z) {
		double dist = computeDistance(x, y, z);
		double dx = x - this.x;
		return dx / dist;
	}

	/**
	 * Computes the y component of the unit vector pointing from the particle to
	 * the passed in coordinates
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the y component of the unit vector pointing from the particle to
	 *         the passed in coordinates
	 */
	private double computeNormalizedY(double x, double y, double z) {
		double dist = computeDistance(x, y, z);
		double dy = y - this.y;
		return dy / dist;
	}

	/**
	 * Computes the z component of the unit vector pointing from the particle to
	 * the passed in coordinates
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the z component of the unit vector pointing from the particle to
	 *         the passed in coordinates
	 */
	private double computeNormalizedZ(double x, double y, double z) {
		double dist = computeDistance(x, y, z);
		double dz = z - this.z;
		return dz / dist;
	}

	/**
	 * Computes the distance between the particle and the particle the passed in
	 * node points to
	 * 
	 * @param node
	 * @return the distance between the particle and the particle the passed in
	 *         node points to
	 */
	protected double computeDistance(TreeNode3D node) {
		return computeDistance(node.getOct().getComX(),
				node.getOct().getComY(), node.getOct().getComZ());
	}

	/**
	 * Computes the distance between the particle and the passed in coordinates
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the distance between the particle and the coordinates passed in
	 */
	private double computeDistance(double x, double y, double z) {
		double dx = this.x - x;
		double dy = this.y - y;
		double dz = this.z - z;
		double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
		return dist;
	}

	/**
	 * Moves the particle according to its current position, speed, and the net
	 * force acting on it
	 */
	public void advance() {
		super.advance();
		double zAcceleration = zNetForce / mass;
		zSpeed += zAcceleration;
		z += zSpeed;

	}

	/**
	 * Sets the particles color according to the net force acting on it. The
	 * larger the net force, the warmer the color. particles with low net force
	 * are a subdued blue.
	 */
	public void colorByForce() {
		// For explanation, see comments in the Particle class
		double netForce = Math.sqrt(xNetForce * xNetForce + yNetForce
				* yNetForce + zNetForce * zNetForce);
		double S = 3e10;
		double L = netForce / S;

		double R, G, B;

		R = Math.min(60 + L * 115, 255);
		G = Math.min(90 + L * 35, 255);
		B = Math.max(125 - L * 45, 80);

		color = new Color((int) R, (int) G, (int) B, 150);
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
	 * @return the z
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z
	 *            the z to set
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * @return the zSpeed
	 */
	public double getZSpeed() {
		return zSpeed;
	}

	/**
	 * @param zSpeed
	 *            the zSpeed to set
	 */
	public void setZSpeed(double zSpeed) {
		this.zSpeed = zSpeed;
	}

}
