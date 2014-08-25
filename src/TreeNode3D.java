/**	
 * TreeNode3D class defines a Node in an octree
 * 
 * @author Christopher Glasz
 */
public class TreeNode3D extends TreeNode {

	/**
	 * The nodes that point to this octants sub-octants
	 */
	private TreeNode3D i, ii, iii, iv, v, vi, vii, viii;

	/**
	 * The node's octant
	 */
	private Octant oct;

	/**
	 * The default constructor doesn't do much
	 */
	public TreeNode3D() {
		this(null);
	}

	/**
	 * The constructor sets the octant to the one passed in, and sets all the
	 * sub-octants to null
	 * 
	 * @param oct
	 */
	public TreeNode3D(Octant oct) {
		this.oct = oct;
	}

	/**
	 * Subdivides the octant, each new sub-node becoming a branch off of this
	 * node.
	 */
	public void subdivide() {
		// For explanation, see comments in the TreeNode class
		if (!isInternal()) {
			i = new TreeNode3D();
			ii = new TreeNode3D();
			iii = new TreeNode3D();
			iv = new TreeNode3D();
			v = new TreeNode3D();
			vi = new TreeNode3D();
			vii = new TreeNode3D();
			viii = new TreeNode3D();

			Octant iOct, iiOct, iiiOct, ivOct, vOct, viOct, viiOct, viiiOct;
			iOct = new Octant(
					oct.getCenterX(),
					oct.getY(),
					oct.getZ(),
					oct.getWidth() / 2.0, 
					oct.getHeight() / 2.0,
					oct.getDepth() / 2.0);
			iiOct = new Octant(
					oct.getX(), 
					oct.getY(),
					oct.getZ(),
					oct.getWidth() / 2.0, 
					oct.getHeight() / 2.0,
					oct.getDepth() / 2.0);
			iiiOct = new Octant(
					oct.getX(), 
					oct.getCenterY(), 
					oct.getZ(),
					oct.getWidth() / 2.0,
					oct.getHeight() / 2.0,
					oct.getDepth() / 2.0);
			ivOct = new Octant(
					oct.getCenterX(),
					oct.getCenterY(),
					oct.getZ(),
					oct.getWidth() / 2.0, 
					oct.getHeight() / 2.0,
					oct.getDepth() / 2.0);
			vOct = new Octant(
					oct.getCenterX(),
					oct.getY(),
					oct.getCenterZ(),
					oct.getWidth() / 2.0, 
					oct.getHeight() / 2.0,
					oct.getDepth() / 2.0);
			viOct = new Octant(
					oct.getX(), 
					oct.getY(),
					oct.getCenterZ(),
					oct.getWidth() / 2.0, 
					oct.getHeight() / 2.0,
					oct.getDepth() / 2.0);
			viiOct = new Octant(
					oct.getX(), 
					oct.getCenterY(), 
					oct.getCenterZ(),
					oct.getWidth() / 2.0,
					oct.getHeight() / 2.0,
					oct.getDepth() / 2.0);
			viiiOct = new Octant(
					oct.getCenterX(),
					oct.getCenterY(),
					oct.getCenterZ(),
					oct.getWidth() / 2.0, 
					oct.getHeight() / 2.0,
					oct.getDepth() / 2.0);
			i.setOct(iOct);
			ii.setOct(iiOct);
			iii.setOct(iiiOct);
			iv.setOct(ivOct);
			v.setOct(vOct);
			vi.setOct(viOct);
			vii.setOct(viiOct);
			viii.setOct(viiiOct);
			isInternal = true;
		}
	}

	/**
	 * @return the i octant
	 */
	public TreeNode3D getI() {
		return i;
	}

	/**
	 * @return the ii octant
	 */
	public TreeNode3D getII() {
		return ii;
	}

	/**
	 * @return the iii octant
	 */
	public TreeNode3D getIII() {
		return iii;
	}

	/**
	 * @return the iv octant
	 */
	public TreeNode3D getIV() {
		return iv;
	}

	/**
	 * @return the v octant
	 */
	public TreeNode3D getV() {
		return v;
	}

	/**
	 * @return the vi octant
	 */
	public TreeNode3D getVI() {
		return vi;
	}

	/**
	 * @return the vii octant
	 */
	public TreeNode3D getVII() {
		return vii;
	}

	/**
	 * @return the viii octant
	 */
	public TreeNode3D getVIII() {
		return viii;
	}

	/**
	 * @return the octant
	 */
	public Octant getOct() {
		return oct;
	}

	/**
	 * @param oct
	 *            the octant to set
	 */
	public void setOct(Octant oct) {
		this.oct = oct;
	}
	
}
