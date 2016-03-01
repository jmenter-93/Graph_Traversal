import java.awt.Color;
import java.awt.Point;

/**
 * DisplayableNode interface
 * @author Jordan Menter
 * @version 10 December 2015
 */
public interface DisplayableNode {
	
	/** Set x coordinate of node */
	public void setX(int x);

	/** Get x coordinate of node */
	public int getX();
	
	/** Set y coordinate of node */
	public void setY(int y);
	
	/** Get y coordinate of node */
	public int getY();
	
	/** Set position of node */
	public void setPosition(int x, int y);
	
	/** Get position of node */
	public Point getPosition();
	
	/** Get color of node */
	public Color getColor();
	
	/** Set color of node */
	public void setColor(Color color);
	
	/** Get data of node */
	public String getData();
	
	/** Set dadta of node */
	public void setData(String data);
}
