//TODO - fairly sure I want to delete this class since I don't believe I'll end up using it.

/**
 * Colony - container to hold a collection of Node objects
 *
 */
public class Colony 
{
	//2 dimensional array to store colony nodes within
	public Node[][] Colony;
	
	/**
	 * ****Constructor****
	 * @param height
	 * @param width
	 */
	public Colony(int height, int width)
	{
		Colony = new Node[height][width];
	}
	
	/**
	 * Adds a node at the specified location
	 * @param node - node to add
	 * @param x - x coordinate
	 * @param y - y coordinate
	 */
	public void addNode(Node node, int x, int y)
	{
		Colony[x][y] = node;
	}
	
}
