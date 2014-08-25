/**
 * Octant class defines a cube which has dimensions and a list of particles
 * contained within it
 * 
 * @author Christopher Glasz
 */
public class Octant extends Quadrant {

	/**
	 * The location and magnitude of the center of mass of the quadrant
	 */
	private double totalMass;

	/**
	 * The z coordinate of the top upper left corner of the octant
	 */
	private double z;

	/**
	 * The depth of the octant
	 */
	private double depth;

	/**
	 * The z coordinate of the center of mass of the octant
	 */
	private double comZ;

	/**
	 * The z component of the speed of the center of mass of the octant
	 */
	private double comZSpeed;

	/**
	 * The constructor creates an octant with the given dimensions
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 * @param depth
	 */
	public Octant(double x, double y, double z, double width, double height,
			double depth) {
		super(x, y, width, height);
		this.z = z;
		this.depth = depth;
		this.comZSpeed = 0;
	}

	/**
	 * Inserts a particle into the quadrant and alters its center of mass
	 * accordingly
	 * 
	 * @param particle
	 * @throws QuadrantException
	 */
	public void insertParticle(Particle3D particle) throws QuadrantException {
		super.insertParticle(particle);
		comZ = particle.getZ();
		comZSpeed = particle.getZSpeed();
	}

	/**
	 * Empties the octant of particles
	 */
	public void empty() {
		super.empty();
		comZ = 0;
		comZSpeed = 0;
	}

	/**
	 * Resets the quadrant
	 */
	public void reset() {
		super.reset();
		comZSpeed = 0;
		comZ = 0;

	}

	/**
	 * Returns true if the quadrant contains the given particle
	 * 
	 * @param particle
	 * @return true if the quadrant contains the given particle
	 */
	public boolean contains(Particle3D particle) {
		boolean contains2D = super.contains(particle);
		boolean contains3rd = (particle.getZ() > z)
				&& (particle.getZ() < (z + depth));
		return contains2D && contains3rd;
	}

	/**
	 * Updates the quadrants center of mass to include the given particle
	 * 
	 * @param particle
	 */
	public void updateCom(Particle3D particle) {
		super.updateCom(particle);
		
		comZ = (comZ * totalMass + Particle.mass * particle.getZ())
				/ (totalMass + Particle.mass);
		
		comZSpeed = (comZSpeed * totalMass + Particle.mass
				* particle.getZSpeed())
				/ (totalMass + Particle.mass);
	}

	/**
	 * Returns the octant's particle
	 * 
	 * @return the octant's particle
	 */
	public Particle3D getParticle() {
		return (Particle3D) particle;
	}

	/**
	 * Returns the z value of the quadrant's center of mass
	 * 
	 * @return the z value of the quadrant's center of mass
	 */
	public double getComZ() {
		return comZ;
	}

	/**
	 * Returns the z value of the quadrant's center of mass
	 * 
	 * @return the z value of the quadrant's center of mass
	 */
	public double getComZSpeed() {
		return comZSpeed;
	}

	/**
	 * Returns the z-coordinate of the octant's geometrical center
	 * 
	 * @return the z-coordinate of the octant's geometrical center
	 */
	public double getCenterZ() {
		return (z + (depth / 2));
	}

	/**
	 * Returns the z-coordinate of the octant's upper-top-left corner
	 * 
	 * @return the z-coordinate of the octant's upper-top-left corner
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Returns the depth of the octant
	 * 
	 * @return the depth of the octant
	 */
	public double getDepth() {
		return depth;
	}

}
