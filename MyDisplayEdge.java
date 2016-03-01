import java.awt.Color;

/**
* Implements DisplayableEdge interface
* @author Jordan Menter
* @version 10 December 2015
*/
public class MyDisplayEdge implements DisplayableEdge {
	
	/** Edge color */
	private Color color;
	
	/** Edge weight */
	private int weight;
	
	/** Constructor */
	public MyDisplayEdge(int weight) {
		this.weight = weight;
	}
	
	/** 
	 * Get color of edge 
	 */
	@Override
	public Color getColor() {
		return color;
	}

	/**
	 * Set color of edge
	 * @param color to set
	 */
	@Override
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Get weight of edge
	 */
	@Override
	public int getWeight() {
		return weight;
	}
}