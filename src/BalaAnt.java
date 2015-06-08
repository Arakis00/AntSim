import java.util.LinkedList;


public class BalaAnt extends Ant 
{
	/**
	 * Constructor makes sure all bala ants spawns at the top left corner of the grid
	 */
	public BalaAnt()
	{
		//always spawn at top left corner of grid (0, 0)
		this.setX(0);
		this.setY(0);
	}
	
	
	/**
	 * handles random movement within the grid, also handles decrementing and incrementing bala count
	 * in nodes that are moved from and to.
	 * @param nodeArray grid used to move on
	 */
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
		
		//decrement bala count in current node before moving
		nodeArray[currentX][currentY].setBalaCount(nodeArray[currentX][currentY].getBalaCount()-1);
		//increment bala count in node being moved to
		nodeArray[randomX][randomY].setBalaCount(nodeArray[randomX][randomY].getBalaCount()+1);
		
		//**************TESTING ONLY************************************
		/*
				//check whether new node is shown and make it visible if it is not
				if (!(nodeArray[randomX][randomY].getShown()))
				{
					nodeArray[randomX][randomY].setShown(true);
				}
		*/
		//**************************************************************
				
		//setting new location values
		this.setX(randomX);
		this.setY(randomY);
	}
	
	/**
	 * checks for enemy ants and returns true if any are present
	 * @param nodeArray the array of colony nodes
	 * @return whether any enemy ants are present
	 */
	public boolean checkForEnemies(Node[][] nodeArray)
	{
		//to hold whether any enemies are present or not
		boolean enemyPresent = false;
		
		//true if any other ants are present
		if (nodeArray[this.getX()][this.getY()].getQueenPresent() ||
			nodeArray[this.getX()][this.getY()].getForagerCount() > 0 ||
			nodeArray[this.getX()][this.getY()].getScoutCount() > 0 ||
			nodeArray[this.getX()][this.getY()].getSoldierCount() > 0)
		{
			enemyPresent = true;
		}
		
		return enemyPresent;
	}
	
	
	/**
	 * attacks an enemy ant if one is present
	 * @param nodeArray grid holding colony node information
	 * @param antList list of friendly ants
	 */
	public void attack(Node[][] nodeArray, LinkedList<Ant> antList)
	{
		boolean antFound = false;  //flag for determining if ant to kill was found
		
		if (randomGen.nextInt(100) < 50)
		{
			//attack successful, killing enemy ant
			//****NOTE - should probably find a more efficient way to do this****
			for (int i = 0; i < antList.size(); i++)
			{
				//ensuring that only 1 ant is attacked/removed
				if (!antFound)
				{
					//checking for oldest spawned ant with matching coordinates to remove
					if (antList.get(i).getX() == this.getX() &&
							antList.get(i).getY() == this.getY())
					{
						antList.get(i).die(nodeArray);
					}
				}
			}
			
		}
		else
		{
			//attack failed so do nothing
		}
	}
	

	/**
	 * @override sets a bala ant to be removed from the list and decrements its count in the node it resides in
	 */
	public void die(Node[][] nodeArray)
	{
		this.setAlive(false);
		//decrementing bala count within its current node
		nodeArray[this.getX()][this.getY()].setBalaCount(nodeArray[this.getX()][this.getY()].getBalaCount()-1);
	}
	
	
	/**
	 * coordinates all actions needed to be taken during a turn
	 */
	public void takeTurn(Node[][] nodeArray, LinkedList<Ant> antList, int turn, LinkedList<BalaAnt> balaList)
	{
		//if enemies are already present in current node, then attack and end turn
		if (checkForEnemies(nodeArray))
		{
			attack(nodeArray, antList);
		}
		else //only move if no attack was already made prior
		{
			//randomly move to a location on the grid
			move(nodeArray);
			//check for ants and attack if any are present
			//decided to allow moving and then attacking, but not the opposite as it made logical sense
			if (checkForEnemies(nodeArray))
			{
				attack(nodeArray, antList);
			}
		}
		
		//checking age and killing if after its turn if lifespan is about to expire
		this.checkLifeSpan(turn, nodeArray);
	}
}
