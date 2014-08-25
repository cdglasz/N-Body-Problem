import java.awt.Graphics;

/**
 * The Octree class defines a three dimensional binary tree. Each node
 * references a octant. The root node references a octant that encompasses all
 * other sub-octants.
 * 
 * @author Christopher Glasz
 * 
 */
public class Octree extends Quadtree {

	/**
	 * Default constructor should never be used
	 */
	public Octree() {
	}

	/**
	 * Constructor defines a octant, of which all other octants will be a part
	 * of
	 * 
	 * @param oct
	 */
	public Octree(Octant oct) {
		root = new TreeNode3D(oct);
	}

	/**
	 * Returns the size (number of octants) of the tree
	 * 
	 * @return the size (number of octants) of the tree
	 */
	public int size() {
		return size((TreeNode3D) root);
	}

	/**
	 * Returns the size (number of octants) of the tree
	 * 
	 * @param root
	 * @return the size (number of octants) of the tree
	 */
	private int size(TreeNode3D root) {
		if (root.isInternal())
			return size(root.getI()) + size(root.getII()) + size(root.getIII())
					+ size(root.getIV()) + size(root.getV())
					+ size(root.getVI()) + size(root.getVII())
					+ size(root.getVIII());
		else
			return 1;
	}

	/**
	 * Returns the population (number of particles) of the tree
	 * 
	 * @return the population (number of particles) of the tree
	 */
	public int population() {
		return population((TreeNode3D) root);
	}

	/**
	 * Returns the population (number of particles) of the tree
	 * 
	 * @param root
	 * @return the population (number of particles) of the tree
	 */
	private int population(TreeNode3D root) {
		if (root.isInternal())
			return population(root.getI()) + population(root.getII())
					+ population(root.getIII()) + population(root.getIV())
					+ population(root.getV()) + population(root.getVI())
					+ population(root.getVII()) + population(root.getVII());
		else {
			if (root.getOct().isFull())
				return 1;
			else
				return 0;
		}
	}

	/**
	 * Inserts the passed particle into the correct octant
	 * 
	 * @param particle
	 */
	public void insertParticle(Particle3D particle) {
		insertParticle((TreeNode3D) root, particle);
	}

	/**
	 * Inserts the passed particle into the correct octant
	 * 
	 * @param root
	 * @param particle
	 */
	private void insertParticle(TreeNode3D root, Particle3D particle) {
		if (!root.isInternal()) {	
			if (root.getOct().isFull()) { 
				
				// If the octant is full, we need to split it up
				Particle3D preExisting = root.getOct().getParticle();
				
				if (!(preExisting.getX() == particle.getX()
						&& preExisting.getY() == particle.getY() && preExisting
							.getZ() == particle.getZ())) {
					
					// Empty the octant
					root.getOct().empty();
					
					// Divide it
					subdivide(root);
					
					// Distribute the two particles into the new sub-octants
					insertParticle(root, preExisting);
					insertParticle(root, particle);
					
				} else {
					
					// Overlaps are practically impossible
					System.out.println("Somehow overlapped");
					System.out.println("\t" + population()
							+ " particles distributed through " + size()
							+ " octants.");

				}
			} else {
				try {
					root.getOct().insertParticle(particle);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} else {
			// Update the center of mass
			root.getOct().updateCom(particle);
			
			// Insert into the appropriate octant
			if (root.getI().getOct().contains(particle))
				insertParticle(root.getI(), particle);
			else if (root.getII().getOct().contains(particle))
				insertParticle(root.getII(), particle);
			else if (root.getIII().getOct().contains(particle))
				insertParticle(root.getIII(), particle);
			else if (root.getIV().getOct().contains(particle))
				insertParticle(root.getIV(), particle);
			else if (root.getV().getOct().contains(particle))
				insertParticle(root.getV(), particle);
			else if (root.getVI().getOct().contains(particle))
				insertParticle(root.getVI(), particle);
			else if (root.getVII().getOct().contains(particle))
				insertParticle(root.getVII(), particle);
			else if (root.getVIII().getOct().contains(particle))
				insertParticle(root.getVIII(), particle);
			else {
				// We should never have reached this octant...
				System.out.print("\nSomething went wrong: ");
				System.out.println("\t" + population()
						+ " particles distributed through " + size()
						+ " octants.");
			}
		}
	}

	/**
	 * Paints the tree
	 * 
	 * @param pane
	 */
	public void paint(Graphics pane) {
		paintTree((TreeNode3D) root, pane);
	}

	/**
	 * Paints the tree
	 * 
	 * @param root
	 * @param pane
	 */
	private void paintTree(TreeNode3D root, Graphics pane) {
		if (root != null) {
			root.getOct().paintBounds(pane);
			if (root.isInternal()) {
				paintTree(root.getI(), pane);
				paintTree(root.getII(), pane);
				paintTree(root.getIII(), pane);
				paintTree(root.getIV(), pane);
				paintTree(root.getV(), pane);
				paintTree(root.getVI(), pane);
				paintTree(root.getVII(), pane);
				paintTree(root.getVIII(), pane);
			}
		}
	}

}
