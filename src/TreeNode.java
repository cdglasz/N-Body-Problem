
/**	
 * TreeNode class defines a Node in a quadtree
 * 
 * @author Christopher Glasz
 */
public class TreeNode {
	
	/**
	 * The nodes that point to this quadrants sub-quadrants
	 */
	private TreeNode i, ii, iii, iv;

	/**
	 * The node's quadrant
	 */
	private Quadrant quad;

	/**
	 * Boolean that describes whether the node is an internal node. Internal
	 * nodes have sub-quadrants; external quadrants do not
	 */
	protected boolean isInternal;

	/**
	 * The default constructor doesn't do much
	 */
	public TreeNode() {
		this(null);
	}

	/**
	 * The constructor sets the quadrant to the one passed in, and sets all the
	 * sub-quadrants to null
	 * 
	 * @param quad
	 */
	public TreeNode(Quadrant quad) {
		i = ii = iii = iv = null;
		this.quad = quad;
		isInternal = false;
	}

	/**
	 * Returns true if the node is internal
	 * @return true if the node is internal
	 */
	public boolean isInternal() {
		return isInternal;
	}

	/**
	 * Subdivides the quadrant, each new sub-node becoming a branch off of this
	 * node.
	 */
	public void subdivide() {
		if (!isInternal()) {
			// Our subnodes
			i = new TreeNode();
			ii = new TreeNode();
			iii = new TreeNode();
			iv = new TreeNode();

			// And our subquadrants-to-be
			Quadrant iQuad, iiQuad, iiiQuad, ivQuad;
			
			// North East
			iQuad = new Quadrant(
					quad.getCenterX(),
					quad.getY(), 
					quad.getWidth() / 2.0, 
					quad.getHeight() / 2.0);
			
			// North West
			iiQuad = new Quadrant(
					quad.getX(), 
					quad.getY(),
					quad.getWidth() / 2.0, 
					quad.getHeight() / 2.0);
			
			// South West
			iiiQuad = new Quadrant(
					quad.getX(), 
					quad.getCenterY(), 
					quad.getWidth() / 2.0,
					quad.getHeight() / 2.0);
			
			// South East
			ivQuad = new Quadrant(
					quad.getCenterX(),
					quad.getCenterY(),
					quad.getWidth() / 2.0, 
					quad.getHeight() / 2.0);
			
			// Attach all the nodes to the appropriate quadrants
			i.setQuad(iQuad);
			ii.setQuad(iiQuad);
			iii.setQuad(iiiQuad);
			iv.setQuad(ivQuad);
			
			// This is no longer an external node
			isInternal = true;
		}
	}

	/**
	 * @return the i quadrant
	 */
	public TreeNode getI() {
		return i;
	}

	/**
	 * @return the ii quadrant
	 */
	public TreeNode getII() {
		return ii;
	}

	/**
	 * @return the iii quadrant
	 */
	public TreeNode getIII() {
		return iii;
	}

	/**
	 * @return the iv quadrant
	 */
	public TreeNode getIV() {
		return iv;
	}

	/**
	 * @return the quadrant
	 */
	public Quadrant getQuad() {
		return quad;
	}

	/**
	 * @param quad
	 *            the quadrant to set
	 */
	public void setQuad(Quadrant quad) {
		this.quad = quad;
	}
}
