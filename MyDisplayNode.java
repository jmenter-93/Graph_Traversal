import java.awt.Color;
import java.awt.Point;

/**
* Implements DisplayableNode interface
* @author Jordan Menter
* @version 10 December 2015
*/
public class MyDisplayNode implements DisplayableNode {
	
	/** Color of this node */
	private Color color;
	
	/** Data at this node */
	private String data;
	
	/** x-coordinate of this node */
	private int x;
	
	/** y-coordinate of this node */
	private int y;
	
	/** Position at this node */
	private Point position;
	
	/**
	 * Set x coordinate of node.
	 * @param int x
	 */
	@Override
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Return x coordinate of node.
	 */
	@Override
	public int getX() {
		return x;
	}
	
	/**
	 * Set y coordinate of node.
	 * @param int y
	 */
	@Override
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Return y coordinate of node.
	 */
	@Override
	public int getY() {
		return y;
	}

	/**
	 * Get color of node.
	 */
	@Override
	public Color getColor() {
		return color;
	}

	/**
	 * Set color of node.
	 * @param color to set
	 */
	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Set position of node.
	 * @param x coordinate of node
	 * @param y coordinate of node
	 */
	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Get position of node.
	 */
	@Override
	public Point getPosition() {
		position = new Point(x, y);
		return position;
	}
	
	/**
	 * Get data at node.
	 */
	@Override
	public String getData() {
		return data;
	}

	/**
	 * Set data at this node.
	 * @param data to set.
	 */
	@Override
	public void setData(String data) {
		this.data = data;
	}
}