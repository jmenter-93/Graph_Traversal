import java.awt.Color;

/**
 * DisplayableEdge interface
 * @author JMenter
 * @version 10 December 2015
 */
public interface DisplayableEdge {

	/** Get color of edge */
	public Color getColor();
	
	/** Set color of edge */
	public void setColor(Color c);
	
	/** Get weight on edge */
	public int getWeight();
	
}
