/**
 * The node class defines a Node to be used in a linked list, which has a
 * reference to a Particle, the previous Node, and the next Node
 * 
 * @author Christopher Glasz
 */
public class Node {

	/**
	 * Holds a Particle
	 */
	protected Particle particle;

	/**
	 * Points to the next node
	 */
	protected Node next;

	/**
	 * Points to the previous node
	 */
	protected Node previous;

	/**
	 * The constructor sets all the fields to null
	 */
	public Node() {
		particle = null;
		next = null;
		previous = null;
	}

	/**
	 * @return the particle
	 */
	public Particle getParticle() {
		return particle;
	}

	/**
	 * @param particle
	 *            the particle to set
	 */
	public void setParticle(Particle particle) {
		this.particle = particle;
	}

	/**
	 * @return the next
	 */
	public Node getNext() {
		return next;
	}

	/**
	 * @param next
	 *            the next to set
	 */
	public void setNext(Node next) {
		this.next = next;
	}

	/**
	 * @return the previous
	 */
	public Node getPrevious() {
		return previous;
	}

	/**
	 * @param previous
	 *            the previous to set
	 */
	public void setPrevious(Node previous) {
		this.previous = previous;
	}

}
