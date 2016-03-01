import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JComponent;

/**
* JGraph - extends JComponent and shows how graph components will be displayed
* @author Jordan Menter
* @version 10 December 2015
*/
public class JGraph extends JComponent {
	
	/** field of class DisplayGraph */
	DisplayGraph<MyDisplayNode, MyDisplayEdge> display;
	
	/** Constructor */
	public JGraph() {
		// Display contents of DisplayGraph
		super();
		display = new DisplayGraph<MyDisplayNode, MyDisplayEdge>();
	}
	
	/** 
	 * Paints the components of the graph.
	 * 
	 * @param g The graphics object to draw with.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/** Color of components */
		g.setColor(Color.BLUE);
		
		/** Loop through graph nodes and paint points */
		for (int i = 0; i < display.NumNodes(); i++) {
			Graph<MyDisplayNode, MyDisplayEdge>.Node node = display.getNode(i);
			Point point = node.getData().getPosition();
			g.fillOval(point.x - 10, point.y - 10, 20, 20);
			g.drawString(node.getData().getData(), point.x + 10, point.y + 10);
		}
		
		/** Loop through graph edges and paint lines */
		for (int i = 0; i < display.NumEdges(); i++) {
			// Get edge
			Graph<MyDisplayNode, MyDisplayEdge>.Edge edge = display.getEdge(i);
			
			// Color of edges
			g.setColor(edge.getData().getColor());

			int x1 = (int) edge.getHead().getData().getPosition().getX();
			int y1 = (int) edge.getHead().getData().getPosition().getY();
			int x2 = (int) edge.getTail().getData().getPosition().getX();
			int y2 = (int) edge.getTail().getData().getPosition().getY();
			
			// Draw edge
			g.drawLine(x1, y1, x2, y2);		
			
			// Add the edge weight
			String weight = String.valueOf(edge.getData().getWeight());
			g.drawString(weight, (x1 + x2)/2, (y1 + y2)/2);
		}
	}
	
	/**
	 * Dimensions
	 * 
	 * @returns The minimum dimension
	 */
	public Dimension getMinimumSize() {
		return new Dimension(500, 3000);
	}
	
	/**
	 * The component will look best at this size
	 * 
	 * @returns The preferred dimension
	 */
	public Dimension getPreferredSize() {
		return new Dimension(500, 300);
	}
}