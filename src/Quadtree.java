import java.awt.Graphics;

/**
 * The Quadtree class defines a two dimensional binary tree. Each node
 * references a quadrant. The root node references a quadrant that encompasses
 * all other sub-quadrants.
 * 
 * @author Christopher Glasz
 * 
 */
public class Quadtree {

	/**
	 * The root of the tree
	 */
	protected TreeNode root;

	/**
	 * Default constructor should never be used
	 */
	public Quadtree() {
	}

	/**
	 * Constructor defines a quadrant, of which all other quadrants will be a
	 * part of
	 * 
	 * @param quad
	 */
	public Quadtree(Quadrant quad) {
		root = new TreeNode(quad);
	}

	/**
	 * Returns true if Quadtree is empty
	 * 
	 * @return true if Quadtree is empty
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * Returns the size (number of quadrants) of the tree
	 * 
	 * @return size
	 */
	public int size() {
		return size(root);
	}

	/**
	 * Returns the size (number of quadrants) of the tree
	 * 
	 * @param root
	 * @return size
	 */
	private int size(TreeNode root) {
		if (root.isInternal())
			return size(root.getI()) + size(root.getII()) + size(root.getIII())
					+ size(root.getIV());
		else
			return 1;
	}

	/**
	 * Returns the population (number of particles) of the tree
	 * 
	 * @return population
	 */
	public int population() {
		return population(root);
	}

	/**
	 * Returns the population (number of particles) of the tree
	 * 
	 * @param root
	 * @return population
	 */
	private int population(TreeNode root) {
		if (root.isInternal())
			return population(root.getI()) + population(root.getII())
					+ population(root.getIII()) + population(root.getIV());
		else {
			if (root.getQuad().isFull())
				return 1;
			else
				return 0;
		}
	}

	/**
	 * Subdivides the nodes (adds four quadrants to the node)
	 * 
	 * @param root
	 */
	public void subdivide(TreeNode root) {
		if (!root.isInternal()) {
			root.subdivide();
		}
	}

	/**
	 * Inserts the passed particle into the correct quadrant
	 * 
	 * @param particle
	 */
	public void insertParticle(Particle particle) {
		insertParticle(root, particle);
	}

	/**
	 * Inserts the passed particle into the correct quadrant
	 * 
	 * @param root
	 * @param particle
	 */
	private void insertParticle(TreeNode root, Particle particle) {
		if (!root.isInternal()) {
			if (root.getQuad().isFull()) {

				// If the quadrant is full, we need to split it up
				Particle preExisting = root.getQuad().getParticle();
				if (!(preExisting.getX() == particle.getX() && preExisting
						.getY() == particle.getY())) {

					// Empty the quadrant
					root.getQuad().empty();
					
					// Split it up
					subdivide(root);
					
					// Distribute the two particles into the new sub-quadrants
					insertParticle(root, preExisting);
					insertParticle(root, particle);
				} else {

					// Overlaps are practically impossible
					System.out.println("Somehow overlapped");
					System.out.println("\t" + population()
							+ " particles distributed through " + size()
							+ " quadrants.");

				}
			} else {
				try {
					root.getQuad().insertParticle(particle);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		} else {
			// Update the center of mass
			root.getQuad().updateCom(particle);

			// Insert into the appropriate quadrant
			if (root.getI().getQuad().contains(particle))
				insertParticle(root.getI(), particle);
			else if (root.getII().getQuad().contains(particle))
				insertParticle(root.getII(), particle);
			else if (root.getIII().getQuad().contains(particle))
				insertParticle(root.getIII(), particle);
			else if (root.getIV().getQuad().contains(particle))
				insertParticle(root.getIV(), particle);
			else {
				// We should never have reached this octant...
				System.out.print("\nSomething went wrong: ");
				System.out.println("\t" + population()
						+ " particles distributed through " + size()
						+ " quadrants.");
			}
		}
	}

	/**
	 * Paints the tree
	 * 
	 * @param pane
	 */
	public void paint(Graphics pane) {
		paintTree(root, pane);
	}

	/**
	 * Paints the tree
	 * 
	 * @param root
	 * @param pane
	 */
	private void paintTree(TreeNode root, Graphics pane) {
		if (root != null) {
			root.getQuad().paintBounds(pane);
			if (root.isInternal()) {
				paintTree(root.getI(), pane);
				paintTree(root.getII(), pane);
				paintTree(root.getIII(), pane);
				paintTree(root.getIV(), pane);
			}
		}
	}

	/**
	 * Returns the root
	 * 
	 * @return the root
	 */
	public TreeNode getRoot() {
		return root;
	}
}
