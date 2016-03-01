import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
* Graph class for an undirected graph.
* @author Jordan Menter
* @version 02 December 2015
*/
public class Graph<V, E> {
	
	/** Master list of edges */
	private ArrayList<Edge> edges;
	
	/** Master list of nodes */
	private ArrayList<Node> nodes;
	
	/** Graph constructor */
	public Graph() {
		edges  = new ArrayList<Edge>();
		nodes = new ArrayList<Node>();
	}
	
	/**
	 * Adds an edge to the graph.
	 * @param data
	 * @param head
	 * @param tail
	 * @return The Edge object added to the graph.
	 */
	public Edge addEdge(E data, Node head, Node tail) {
		Edge edge  = new Edge(data, head, tail);
		Edge result = null;
		if ( !edges.contains(edge) ) {
			edges.add(edge);
			head.addEdgeRef(edge);
			tail.addEdgeRef(edge);
			result = edge;
		} else {
			result = null;
		}
		return result;
	}
	
	/**
	 * Adds a node to the graph.
	 * @param data
	 * @return The Node object added to the graph.
	 */
	public Node addNode(V data) {
		Node node = new Node(data);
		nodes.add(node);
		return node;
	}

	/**
	 * Return nodes that are endpoints of a set of edges.
	 * @param edges
	 * @return A set of the endpoints.
	 */
	public ArrayList<Node> endpoints(ArrayList<Edge> edges) {
		ArrayList<Node> endpoints = new ArrayList<Node>();
		Edge first = edges.get(0);
		Edge last = edges.get(edges.size() - 1);
		endpoints.add(first.getHead());
		endpoints.add(last.getTail());
		return endpoints;
	}
	
	/**
	 * Accessor for edges.
	 * Input will be index into ArrayList, which preserves ordering.
	 * @param i, the index into the ArrayList
	 * @return the desired edge.
	 */
	public Edge getEdge(int i) {
		Edge edge = edges.get(i);
		return edge;
	}
	
	/**
	 * Accessor for a specific edge.
	 * @param head
	 * @param tail
	 * @return the desired edge.
	 */
	public Edge getEdgeRef(Node head, Node tail) throws NoSuchElementException {
		Edge result;
		Edge edge = head.edgeTo(tail);
		if (edge != null) {
				result = edge;
			} else {
				NoSuchElementException problem = new NoSuchElementException("This head/tail combination"
						+ "does not exist!");
				throw problem;
			}
		return result;
	}
	
	/**
	 * Accessor for nodes.
	 * @param i
	 * @return The desired node.
	 */
	public Node getNode(int i) {
		Node node = nodes.get(i);
		return node;
	}
	
	/**
	 * Accessor for number of edges.
	 * @return Number of edges.
	 */
	public int NumEdges() {
		int num = edges.size();
		return num;
	}
	
	/**
	 * Accessor for number of nodes.
	 * @return Number of nodes. 
	 */
	public int NumNodes() {
		int num = nodes.size();
		return num;
	}
	
	/**
	 * Returns nodes not on a given list.
	 * @param group
	 * @return HashSet of nodes not on a given list.
	 */
	public HashSet<Node> otherNodes(HashSet<Node> group) {
		HashSet<Node> nodeSet = new HashSet<Node>(nodes);
		nodeSet.removeAll(group);
		return nodeSet;
	}
	
	/**
	 * Removes an edge.
	 * @param edge, the edge to remove.
	 */
	public void removeEdge(Edge edge) {
		// Remove reference from head node
		edge.head.removeEdgeRef(edge);
		// Remove reference from tail node
		edge.tail.removeEdgeRef(edge);
		// Remove reference from edge master list
		edges.remove(edge);
	}
	
	/**
	 * Removes an edge.
	 * @param head of the edge to remove.
	 * @param tail of the edge to remove.
	 */
	public void removeEdge(Node head, Node tail) {
		Edge edge = this.getEdgeRef(head, tail);
		this.removeEdge(edge);
	}
	
	/**
	 * Removes a node.
	 * @param The node to remove.
	 */
	public void removeNode(Node node) {
		// Remove all of node's edges and references to each edge
		while(!(node.edges.isEmpty())) {
			// Get last edge and remove
			Edge edge = edges.get(edges.size() - 1);
			this.removeEdge(edge);
		}
		//for (Edge edge : node.edges) {
			//edges.remove(edge);
			//Node opposite = edge.oppositeTo(node);
			//opposite.removeEdgeRef(edge);
		//}
		// Remove reference from node master list
		nodes.remove(node);
	}
	
	/**
	 * Breadth-first traversal of graph.
	 * @param Node to start at.
	 * @return HashSet of edges of traversal.
	 */
	public HashSet<Edge> BFT(Node start) {
		
		Queue<Node> queue = new LinkedList<Node>();
		HashSet<Node> visited = new HashSet<Node>();
		HashSet<Edge> edges = new HashSet<Edge>();
		// Put starting node in queue and mark as "seen"
		queue.add(start);
		visited.add(start);
		
		// While queue not empty
		while(!queue.isEmpty()) {
			// Take node off queue
			Node node = queue.remove();
			// Visit it
			visited.add(node);
			
			for (Node neighbor : node.getNeighbors()) {
				// Put unseen neighbors on queue and mark as seen
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					queue.add(neighbor);
					Edge traversed = this.getEdgeRef(node, neighbor);
					edges.add(traversed);
				}
			}
		}
		return edges;
	}
	
	/**
	 * Depth-first traversal of graph -- public interface (helper method).
	 * Sets up data structure and calls recursive method on start node.
	 * @param start
	 * @return HashSet of edges of traversal.
	 */
	public HashSet<Edge> DFT(Node start) {
		HashSet<Node> visited = new HashSet<Node>();
		HashSet<Edge> edges = new HashSet<Edge>();
		
		recurse(start, edges, visited);
		
		return edges;
	}
	
	/**
	 * Private method to run the recursion.
	 * @param start, starting node
	 * @param edges for storage
	 * @param set for storing visited nodes
	 */
	private HashSet<Edge> recurse(Node start, HashSet<Edge> edges, HashSet<Node> visited) {
		HashSet<Edge> result = new HashSet<Edge>();
		//if (visited.contains(start)) {
			//result = edges;
		//} else {
		visited.add(start);
		for (Node neighbor : start.getNeighbors()) {
			if(!visited.contains(neighbor)) {
				Edge traversed = this.getEdgeRef(start, neighbor);
				// Add edge to traversed edges
				edges.add(traversed);
				// Add visited neighbor to set of visited nodes
				visited.add(neighbor);
				// Recursive call on all neighbors
				recurse(neighbor, edges, visited);
			}
		}
		return result;
	}
	
	/**
	 * Checks the consistency of the graph.
	 * @throws Exception
	 */
	public void check() throws Exception {

		for (Edge edge : edges) {
			if ((edge.getHead() == null) || (edge.getTail() == null)) {
				Exception problem = new Exception("This edge is inconsistent!");
				throw problem;
			}
			// Test that head and tail node list the edge on their edge lists
			if (!(edge.getHead().edges.contains(edge)) || !(edge.getTail().edges.contains(edge))) {
				Exception problem = new Exception("Head or tail edge lists are inconsistent!");
				throw problem;
			}
			// Test that head and tail nodes appear in the master lists
			if (!(nodes.contains(edge.getHead())) || !nodes.contains(edge.getTail())) {
				Exception problem = new Exception("Head or tail are not contained in master lists!");
				throw problem;
			}
		}
		
		for (Node node : nodes) {
			for (Edge edge : node.edges) {
				// For each edge on the node's edge list, that edge links back to head or tail
				if ( (edge.head == node) || (edge.tail == node) ) {
					Exception problem = new Exception("Edge does not link back to head or tail!");
					throw problem;
				}
				// For each edge on the node's edge list, that edge appears on the master list of edges
				if (!edges.contains(edge)) {
					Exception problem = new Exception("Edge does not appear on edge master list!");
					throw problem;
				}
				// For each edge on each node's edge list, make sure that it's not a self edge, i.e. head != tail
				if (edge.getHead().equals(edge.getTail())) {
					Exception problem = new Exception("Graph contains self-edges!");
					throw problem;
				}
			}
		}
	}
	
	/**
	 * Prints each node of the graph and its edge weights.
	 */
	public void print() {
		for (Node node : nodes) {
			System.out.print(node.getData() + " : ");
			System.out.print("{ ");
			for(Edge edge : node.edges) {
				System.out.print(edge.getData() + " ");
			}
			System.out.print("}");
			System.out.println("");
		}
	}
	
	/**
	 * Edge class, a sub-class of the Graph class. 
	 * @author JMenter
	 * @version 02 December 2015
	 */
	public class Edge { 
		
		/** The head node of the edge */
		private Node head;
		
		/** The tail node of the edge */
		private Node tail;
		
		/** The data on the edge */
		private E data;
		
		/** Edge constructor */
		public Edge(E data, Node head, Node tail) {
			this.head = head;
			this.tail = tail;
			this.data = data;
		}
		
		/** Accessor for Edge data */
		public E getData() {
			return data;
		}
		
		/** Accessor for Head node */
		public Node getHead() {
			return head;
		}
		
		/** Accessor for Tail node */
		public Node getTail() {
			return tail;
		}

		/** Manipulator for edge data */
		public void setData(E data) {
			this.data = data;
		}
		
		/**
		 * Accessor for opposite node.
		 * @param node
		 * @return The opposite node.
		 */
		public Node oppositeTo(Node node) {
			Node result = null; 
			if(this.tail.equals(node)) {
				result = this.head;
			} else if(this.head.equals(node)) {
				result = this.tail;
			}
			return result;
		}
		
		/**
		 * Two edges are equal if they connect the same endpoints.
		 * @param Object o
		 * @return
		 */
		@ Override
		public boolean equals(Object o) {
			boolean result = false;
			if(getClass() == o.getClass()) {
				@SuppressWarnings("unchecked")
				Edge edge = (Edge)o;
				if (( this.head == edge.head && this.tail == edge.tail ) || 
					( this.head == edge.tail && this.tail == edge.head)) {
					result = true;
				}
			}
			return result;
		}
		
		/**
		 * Redefined hashCode to match redefined equals, for an undirected graph.
		 * @return the hashCode
		 */
		@ Override
		public int hashCode() {
			int hash = this.head.hashCode() + this.tail.hashCode();
			return hash;
		}	
	}
	
	/**
	 * The Node class, a sub-class of the Graph class.
	 * @author Jordan Menter
	 * @version 02 December 2015. 
	 */
	public class Node { 
		
		/** The data at each node */
		private V data;
		
		/** Each node's master list of edges */
		private HashSet<Edge> edges = new HashSet<Edge>();
		
		/** Node constructor */
		public Node(V data) {
			this.data = data;
		}
		
		/**
		 * Accessor for Node data
		 * @return the Node object's data.
		 */
		public V getData() {
			return data;
		}
		
		/**
		 * Manipulator for Node data
		 * @param data
		 */
		public void setData(V data) {
			this.data = data;
		}
		
		/**
		 * Adds an edge to the Node's edge list
		 * @param edge
		 */
		protected void addEdgeRef(Edge edge) {
			edges.add(edge);
		}
		
		/**
		 * Removes an edge from the Node's edge list.
		 * @param edge to remove
		 */
		protected void removeEdgeRef(Edge edge) {
			edges.remove(edge);
		}
		
		/**
		 * Return a list of neighboring nodes.
		 * @return A HashSet of neighboring nodes.
		 */
		public HashSet<Node> getNeighbors() {
			HashSet<Node> neighbors = new HashSet<Node>();
			for (Edge edge : edges) {
				Node n1 = edge.getHead();
				Node n2 = edge.getTail();
				// If head or tail node are not the current node, add to list of neighbors
				if (!this.equals(n1)) {
					neighbors.add(n1);
				}
				if(!this.equals(n2)) {
					neighbors.add(n2);
				}
			}
			return neighbors;
		}
		
		/**
		 * Returns true if there is an edge from a node to the node in question.
		 * @param node in question.
		 * @return true or false
		 */
		public boolean isNeighbor(Node node) {
			boolean result = false;
			for (Edge e1 : this.edges) {
				if( (node.equals(e1.getHead())) || (node.equals(e1.getTail()))) {
					result = true;
					}
			}
		return result;
		}
		
		/**
		 * Returns the edge to a specified node, or null if there is none.
		 * @param neighbor Node.
		 * @return Edge to the neighbor, or null.
		 */
		public Edge edgeTo(Node neighbor) {
			Edge result = null;
			for (Edge e1 : this.edges) {
				if( (neighbor.equals(e1.getHead())) || (neighbor.equals(e1.getTail()))) {
					result = e1;
				}
			}
			return result;
		}
		
	}
}