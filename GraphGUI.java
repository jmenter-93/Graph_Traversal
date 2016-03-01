import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
* Controller for GUI - allow user interaction for creating
* graph and traversal methods (BFT and DFT)
* @author Jordan Menter
* @version 10 December 2015
*/
public class GraphGUI {
	
	/** The graph to be displayed */
	private JGraph canvas;
	
	/** Label for the input mode instructions */
	private JLabel instr;
	
	/** The input mode */
	InputMode mode = InputMode.ADD_NODES;
	
	/** Remembers point where last mousedown event occurred */
	Point pointUnderMouse;
	
	/** Remembers node where last mousedown event occurred */
	Graph<MyDisplayNode, MyDisplayEdge>.Node nodeUnderMouse;
	
	/**
	 * Schedules a job for the event-dispatching thread
	 * creating and showing this application's GUI.
	 */
    public static void main(String[] args) {
        final GraphGUI GUI = new GraphGUI();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    GUI.createAndShowGUI();
                }
            });
    }
    
    /** Sets up the GUI window */
    public void createAndShowGUI() {
        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        JFrame frame = new JFrame("Graph GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add components
        createComponents(frame);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    /** Puts content in the GUI window */
    public void createComponents(JFrame frame) {
        // graph display
        Container pane = frame.getContentPane();
        pane.setLayout(new FlowLayout());
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        canvas = new JGraph();
        PointMouseListener pml = new PointMouseListener();
        canvas.addMouseListener(pml);
        canvas.addMouseMotionListener(pml);
        panel1.add(canvas);
        instr = new JLabel("Click to add new points; drag to move.");
        panel1.add(instr,BorderLayout.NORTH);
        pane.add(panel1);

        // Controls
        JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayout(3,2));
        
        // Buttons
        JButton addPointButton = new JButton("Add/Move Points");
        panel2.add(addPointButton);
        addPointButton.addActionListener(new AddPointListener());
        
        JButton rmvPointButton = new JButton("Remove Points");
        panel2.add(rmvPointButton);
        rmvPointButton.addActionListener(new RmvPointListener());
        
        JButton addEdgeButton = new JButton("Add Edges");
        panel2.add(addEdgeButton);
        addEdgeButton.addActionListener(new AddEdgeListener());
        
        JButton rmvEdgeButton = new JButton("Remove Edges");
        panel2.add(rmvEdgeButton);
        rmvEdgeButton.addActionListener(new RmvEdgeListener());
        
        JButton BFTButton = new JButton("Breadth-First Traversal");
        panel2.add(BFTButton);
        BFTButton.addActionListener(new BFTListener());
        
        JButton DFTButton = new JButton("Depth-First Traversal");
        panel2.add(DFTButton);
        DFTButton.addActionListener(new DFTListener());
        
        pane.add(panel2);
    }
    
    /** 
     * Returns a point found within the drawing radius of the given location, 
     * or null if none
     *
     *  @param x  the x coordinate of the location
     *  @param y  the y coordinate of the location
     *  @return  a point from the canvas if there is one covering this location, 
     *  or a null reference if not
     */
    public Point findNearbyPoint(int x, int y) {
		// Loop over all points in the canvas and see if any of them
		// overlap with the location specified.
    	Point location = new Point(x, y);
    	for(int i = 0; i < canvas.display.NumNodes(); i++) {
    		// Get node
    		Graph<MyDisplayNode, MyDisplayEdge>.Node node = canvas.display.getNode(i);
    		// Check distance between node's position and the location point
    		if(node.getData().getPosition().distance(location) <= 5) {
    			return node.getData().getPosition();
    		}
    	}
    	return null;
    }
    
    /** Constants for recording the input mode */
    enum InputMode {
        ADD_NODES, RMV_NODES, ADD_EDGES, RMV_EDGES, BFT, DFT
    }
    
    /** Listener for AddPoint button */
    private class AddPointListener implements ActionListener {
        /** Event handler for AddPoint button */
        public void actionPerformed(ActionEvent e) {
            mode = InputMode.ADD_NODES;
            instr.setText("Click to add new nodes or change their location.");
        }
    }

    /** Listener for RmvPoint button */
    private class RmvPointListener implements ActionListener {
        /** Event handler for RmvPoint button */
        public void actionPerformed(ActionEvent e) {
        	mode = InputMode.RMV_NODES;
        	instr.setText("Click to remove nodes.");
        }
    }
    
    /** Listener for AddEdges button */
    private class AddEdgeListener implements ActionListener {
    	/** Event handler for AddEdges button */
    	public void actionPerformed(ActionEvent e) {
    		mode = InputMode.ADD_EDGES;
    		instr.setText("Click to add new edges.");
    	}
    }
    
    /** Listener for RmvEdges button */
    private class RmvEdgeListener implements ActionListener {
    	/** Event handler for RmvEdges button */
    	public void actionPerformed(ActionEvent e) {
    		mode = InputMode.RMV_EDGES;
    		instr.setText("Click to remove edges.");
    	}
    }
    
    /** Listener for BFT button */
    private class BFTListener implements ActionListener {
    	/** Event handler for BFT button */
    	public void actionPerformed(ActionEvent e) {
    		mode = InputMode.BFT;
    		instr.setText("Click to do Breadth-First Traversal.");
    	}
    }
    
    /** Listener for DFT button */
    private class DFTListener implements ActionListener {
    	/** Event handler for DFT button */
    	public void actionPerformed(ActionEvent e) {
    		mode = InputMode.DFT;
    		instr.setText("Click to do Depth-First Traversal.");
    	}
    }
    
    /** Mouse listener for JGraph element */
    private class PointMouseListener extends MouseAdapter
        implements MouseMotionListener {

        /** Responds to click event depending on mode */
        public void mouseClicked(MouseEvent e) {
            switch (mode) { // begin switch block
            case ADD_NODES:
				// If the click is not on top of an existing point, create a new one and add it to the canvas.
				// Otherwise, emit a beep.
            	if(findNearbyPoint(e.getX(), e.getY()) == null) {
            		
            		// New node object
            		MyDisplayNode node = new MyDisplayNode();
            		// Set location
            		node.setPosition(e.getX(), e.getY());
            		
            		// Prompt user for location data
            		JFrame frame = new JFrame("InputDialog #1"); 
            		String location = JOptionPane.showInputDialog(frame, "Enter a city.");
            		// Set as node data
            		node.setData(location);
            		
            		// Add to canvas using Graph methods
            		canvas.display.addNode(node);
            		
            	} else { Toolkit.getDefaultToolkit().beep();
            	}
                break;
                
            case RMV_NODES:
				// If the click is on top of an existing point, remove it from the canvas's list of points.
				// Otherwise, emit a beep.
            	// The point underneath the click.
            	Point removePt = findNearbyPoint(e.getX(), e.getY());
            	if(removePt != null) {
            		for(int i = 0; i < canvas.display.NumNodes(); i++) {
            			// Find the corresponding node
            			Graph<MyDisplayNode, MyDisplayEdge>.Node node = canvas.display.getNode(i);
            			if ( node.getData().getPosition().equals(removePt) ) {
            				// Remove using Graph methods
            				canvas.display.removeNode(node);
            			}
            		}
            	} else {
            		Toolkit.getDefaultToolkit().beep();
            	}
                break; 
                
            case BFT:
            	// Do Breadth-First Traversal on the node indicated by the user. Print out edges traversed,
            	// nodes visited, and nodes that cannot be reached from the starting point.
            	
            	HashSet<Graph<MyDisplayNode, MyDisplayEdge>.Edge> traversedBFT = new HashSet<Graph<MyDisplayNode, MyDisplayEdge>.Edge>();
            	// The starting point
            	Point startBFT = findNearbyPoint(e.getX(), e.getY()); 
            	
            	if(startBFT != null) { 
            		// Node under mouse is starting node
            		nodeUnderMouse.getData().setPosition(e.getX(), e.getY());
            		
            		// Do BFT on this node as the start
            		traversedBFT = canvas.display.BFT(nodeUnderMouse);
            		
            		// Compile visited nodes into a HashSet
            		HashSet<Graph<MyDisplayNode, MyDisplayEdge>.Node> visitedNodes = new HashSet<Graph<MyDisplayNode, MyDisplayEdge>.Node>();
            		
            		// Display traversed edges
            		System.out.println("Results of Breadth-First Traversal:");
            		for(Graph<MyDisplayNode, MyDisplayEdge>.Edge edge : traversedBFT) {
            			visitedNodes.add(edge.getHead());
            			visitedNodes.add(edge.getTail());
            			System.out.println("Traversed edge between " + edge.getHead().getData().getData() + " and " + edge.getTail().getData().getData());
            		}
            		
            		// Display visited nodes
            		for(Graph<MyDisplayNode, MyDisplayEdge>.Node node : visitedNodes) {
            			System.out.println("Visited " + node.getData().getData());
            		}
            		
            		// Display nodes that cannot be reached from the start point
            		HashSet<Graph<MyDisplayNode, MyDisplayEdge>.Node> isolated = canvas.display.otherNodes(visitedNodes);
            		if(!isolated.isEmpty()) {
            			for(Graph<MyDisplayNode, MyDisplayEdge>.Node node : isolated) {
            				System.out.println("Could not reach " + node.getData().getData());
            			}
            		} else {
            			System.out.println("All nodes were reached from the start point.");
            		}
            		System.out.println();
            		
            	} else {
            		Toolkit.getDefaultToolkit().beep();
            	}
            	break;
            	
            case DFT:
            	// Do Depth-First Traversal on the node indicated by the user. Print out edges traversed,
            	// nodes visited, and nodes that cannot be reached from the starting point.
            	HashSet<Graph<MyDisplayNode, MyDisplayEdge>.Edge> traversedDFT = new HashSet<Graph<MyDisplayNode, MyDisplayEdge>.Edge>();
            	// The starting point
            	Point startDFT = findNearbyPoint(e.getX(), e.getY());
            	
            	if(startDFT != null) {
            		
            		// Node under mouse is starting node
            		nodeUnderMouse.getData().setPosition(e.getX(), e.getY());
            	
            		// Do Depth-First Traversal
            		traversedDFT = canvas.display.DFT(nodeUnderMouse);
            		
            		// Compile visited nodes into a HashSet
            		HashSet<Graph<MyDisplayNode, MyDisplayEdge>.Node> visitedNodes = new HashSet<Graph<MyDisplayNode, MyDisplayEdge>.Node>();
            		
            		// Display traversed edges
            		System.out.println("Results of Depth-First Traversal:");
            		for(Graph<MyDisplayNode, MyDisplayEdge>.Edge edge : traversedDFT) {
            			visitedNodes.add(edge.getHead());
            			visitedNodes.add(edge.getTail());
            			System.out.println("Traversed edge between " + edge.getHead().getData().getData() + " and " + edge.getTail().getData().getData());
            		}
            		
            		// Display visited nodes
            		for(Graph<MyDisplayNode, MyDisplayEdge>.Node node : visitedNodes) {
            			System.out.println("Visited " + node.getData().getData());
            		}
            		
            		// Display nodes that cannot be reached from the start point
            		HashSet<Graph<MyDisplayNode, MyDisplayEdge>.Node> isolated = canvas.display.otherNodes(visitedNodes);
            		if(!isolated.isEmpty()) {
            			for(Graph<MyDisplayNode, MyDisplayEdge>.Node node : isolated) {
            				System.out.println("Could not reach " + node.getData().getData());
            			}
            		} else {
            			System.out.println("All nodes were reached from the start point.");
            		}
            		System.out.println();
            			
            	} else {
            		Toolkit.getDefaultToolkit().beep();
            	}
            } // end of switch block
            canvas.repaint();
        }

        /** Records point under mousedown event in anticipation of possible drag */
        public void mousePressed(MouseEvent e) {
            // Record point under mousePressed event
        	pointUnderMouse = findNearbyPoint(e.getX(), e.getY());
        	
        	// Record node under mousePressed event
        	// Necessary for defining head node when adding edges
        	DisplayGraph<MyDisplayNode, MyDisplayEdge>.Node node;
        	for(int i = 0; i < canvas.display.NumNodes(); i++) {
    			node = canvas.display.getNode(i);
    			if ( node.getData().getPosition().equals(pointUnderMouse) ) {
    				nodeUnderMouse = node;
    			}
        	}
        }

        /** Responds to mouse drag event */
        public void mouseDragged(MouseEvent e) {
			// If mode allows point motion, and there is a point under the mouse, 
			// then change its coordinates to the current mouse coordinates & update display
        	if((mode == InputMode.ADD_NODES) && (nodeUnderMouse != null)) {
        		nodeUnderMouse.getData().setPosition(e.getX(), e.getY());
        		canvas.repaint();
        	}
        }
        
        /** Responds to mouseup event */
        public void mouseReleased(MouseEvent e) {
        	
        	// Head and tail nodes for adding edges
        	Graph<MyDisplayNode, MyDisplayEdge>.Node head = null;
        	Graph<MyDisplayNode, MyDisplayEdge>.Node tail = null;
        	
        	// Head node is nodeUnderMouse from mousePressed event
        	head = nodeUnderMouse;
        	
        	// Get tail point from mouseReleased event and store as node
        	Point pointUnderMouseRelease = findNearbyPoint(e.getX(), e.getY());
        	DisplayGraph<MyDisplayNode, MyDisplayEdge>.Node node;
        	
            for(int i = 0; i < canvas.display.NumNodes(); i++) {
        		node = canvas.display.getNode(i);
        		if ( node.getData().getPosition().equals(pointUnderMouseRelease) ) {
        			tail = node;
        		}
            }
        		
        	if(mode == InputMode.ADD_EDGES) {
        		// Add edges with weights specified by the user
        		JFrame frame2 = new JFrame("InputDialog #2"); 
        		String weight = JOptionPane.showInputDialog(frame2, "Enter an edge weight.");
        		int weightInt = Integer.parseInt(weight);
        		// Set as edge data with MyDisplayEdge constructor
        		MyDisplayEdge data = new MyDisplayEdge(weightInt);
        		
        		// Add to canvas using Graph methods
        		canvas.display.addEdge(data, head, tail); 
        		// Reset head and tail
        		head = null;
        		tail = null;
        	}
        	
        	if(mode == InputMode.RMV_EDGES) {
        		// Remove edges from canvas based on user's clicks and drags
        		Graph<MyDisplayNode, MyDisplayEdge>.Edge edge;
        		
        		// Get edge reference of head and tail that were clicked on
        		Graph<MyDisplayNode, MyDisplayEdge>.Edge testEdge = canvas.display.getEdgeRef(head, tail);
        		
        		// Find edge with same endpoints as the testEdge
        		for( int i = 0; i <= canvas.display.NumEdges(); i++) {
        			edge = canvas.display.getEdge(i);
        			// If they have the same endpoints...
        			if(edge.equals(testEdge)) {
        				// Remove the edge from the graph
        				canvas.display.removeEdge(edge);
        			}
        		}
        	}
        	// Repaint the canvas
        	canvas.repaint();
        }

		// Empty but necessary to comply with MouseMotionListener interface.
        public void mouseMoved(MouseEvent e) {}
    }
}