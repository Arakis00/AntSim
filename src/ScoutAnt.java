import java.util.LinkedList;


public class ScoutAnt extends Ant
{
	public ScoutAnt()
	{
		//doesn't need anything other than the standard super constructor
	}
	
	
	public void move(Node[][] nodeArray)
	{
		int randomX = this.getX(); //to hold a randomly generated value for x axis location
		int randomY = this.getY(); //to hold a randomly generated value for y axis location
		int currentX = this.getX(); //hold current x location
		int currentY = this.getY(); //hold current y location
		
		//ensuring X and Y doesn't stay the same so a move actually occurs
		while (randomX == this.getX() && randomY == this.getY())
		{
			//generating random numbers to determine movement
			//0 = subtract one, 1 = stay the same, 2 = plus one
			
			//generating x value
			randomNum = randomGen.nextInt(3);
			if (randomNum == 0 && this.getX() > 0)
				randomX = (this.getX()-1); //subtract one from x
			else if (randomNum == 2 && this.getX() < 26)
				randomX = (this.getX()+1); //add one to x
			else
			{} //value is 1 or a boundary issue occurred so the value doesn't change
				
			//generating y value	
			randomNum = randomGen.nextInt(3);
			if (randomNum == 0 && this.getY() > 0)
				randomY = (this.getY()-1); //subtract one from y
			else if (randomNum == 2 && this.getY() < 26)
				randomY = (this.getY()+1); //add one to y
			else
			{} //value is 1 or a boundary issue occurred so the value doesn't change
		}
		
		//decrement scout count in current node before moving
		nodeArray[currentX][currentY].setScoutCount(nodeArray[currentX][currentY].getScoutCount()-1);
		//increment scout count in node being moved to
		nodeArray[randomX][randomY].setScoutCount(nodeArray[randomX][randomY].getScoutCount()+1);
		//check whether new node is shown and make it visible if it is not
		if (!(nodeArray[randomX][randomY].getShown()))
		{
			nodeArray[randomX][randomY].setShown(true);
		}
		//setting new location values
		this.setX(randomX);
		this.setY(randomY);
	}
	
	
	/**
	 * @override sets a scout ant to be removed from the list and decrements its count in the node it resides in
	 */
	public void die(Node[][] nodeArray)
	{
		this.setAlive(false);
		//decrementing scout count within its current node
		nodeArray[this.getX()][this.getY()].setScoutCount(nodeArray[this.getX()][this.getY()].getScoutCount()-1);
	}
	
	
	/**
	 * coordinates all actions needed to be taken during a turn
	 */
	public void takeTurn(Node[][] nodeArray, LinkedList<Ant> antList, int turn, LinkedList<BalaAnt> balaList)
	{
		//randomly move to a location on the grid
		move(nodeArray);
		//checking age and killing if after its turn if lifespan is about to expire
		this.checkLifeSpan(turn, nodeArray);
	}
}
