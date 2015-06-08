import java.util.LinkedList;

//Didn't do Scout/Attack mode since this seems to meet the requirements without adding a flag that seems unnecessary
public class SoldierAnt extends Ant
{
	public SoldierAnt()
	{
		
	}
	
	
	/**
	 * handles random movement within the grid, also handles decrementing and incrementing bala count
	 * in nodes that are moved from and to.
	 * @param nodeArray grid used to move on
	 */
	public void move(Node[][] nodeArray, LinkedList<BalaAnt> balaList)
	{	
		int randomX = this.getX(); //to hold a randomly generated value for x axis location
		int randomY = this.getY(); //to hold a randomly generated value for y axis location
		int currentX = this.getX(); //hold current x location
		int currentY = this.getY(); //hold current y location
		
		//checking for bala ants in any adjacent, shown squares and moving to there if true
		if (checkAdjSquares(nodeArray, balaList))
		{
			//already moved if the above was true, so skip random movement
		}
		else
		{
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
				
				//if new node isn't shown, change random x/y back to original values so loop reiterates
				if (!nodeArray[randomX][randomY].getShown())
				{
					randomX = currentX;
					randomY = currentY;
				}
			}
		
			//decrement soldier count in current node before moving
			nodeArray[currentX][currentY].setSoldierCount(nodeArray[currentX][currentY].getSoldierCount()-1);
			//increment soldier count in node being moved to
			nodeArray[randomX][randomY].setSoldierCount(nodeArray[randomX][randomY].getSoldierCount()+1);
			
			//setting new location values
			this.setX(randomX);
			this.setY(randomY);
		}
	}
	
	
	/**
	 * determines whether an enemy is present in an adjacent node that is shown
	 * will also move the soldier if the conditions are found to be true
	 * @param nodeArray the grid of nodes
	 * @return whether an enemy was found or not
	 */
	public boolean checkAdjSquares(Node[][] nodeArray, LinkedList<BalaAnt> balaList)
	{
		boolean enemyFound = false;
		
		for (int i = 0; i < balaList.size(); i++)
		{
			if (balaList.get(i).getX() >= this.getX()-1 &&
					balaList.get(i).getX() <= this.getX()+1)
			{
				if (balaList.get(i).getY() >= this.getY()-1 &&
						balaList.get(i).getY() <= this.getY()+1)
				{
					if (nodeArray[balaList.get(i).getX()][balaList.get(i).getY()].getShown())
					{
						enemyFound = true;
						//decrement soldier count in current node before moving
						nodeArray[this.getX()][this.getY()].setSoldierCount(nodeArray[this.getX()][this.getY()].getSoldierCount()-1);
						//increment soldier count in node being moved to
						nodeArray[balaList.get(i).getX()][balaList.get(i).getY()].setSoldierCount(nodeArray[balaList.get(i).getX()][balaList.get(i).getY()].getSoldierCount()+1);
						
						//setting new location values
						this.setX(balaList.get(i).getX());
						this.setY(balaList.get(i).getY());
					}
				}
			}
		}
		
		return enemyFound;
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
		if (nodeArray[this.getX()][this.getY()].getBalaCount() > 0)
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
	public void attack(Node[][] nodeArray, LinkedList<BalaAnt> balaList)
	{
		boolean antFound = false;  //flag for determining if ant to kill was found
		
		if (randomGen.nextInt(100) < 50)
		{
			//attack successful, killing enemy ant
			//****NOTE - should probably find a more efficient way to do this****
			for (int i = 0; i < balaList.size(); i++)
			{
				//ensuring that only 1 ant is attacked/removed
				if (!antFound)
				{
					//checking for oldest spawned ant with matching coordinates to remove
					if (balaList.get(i).getX() == this.getX() &&
							balaList.get(i).getY() == this.getY())
					{
						balaList.get(i).die(nodeArray);
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
	 * @override sets a soldier ant to be removed from the list and decrements its count in the node it resides in
	 */
	public void die(Node[][] nodeArray)
	{
		this.setAlive(false);
		//decrementing soldier count within its current node
		nodeArray[this.getX()][this.getY()].setSoldierCount(nodeArray[this.getX()][this.getY()].getSoldierCount()-1);
	}
	
	
	/**
	 * coordinates all actions needed to be taken during a turn
	 */
	public void takeTurn(Node[][] nodeArray, LinkedList<Ant> antList, int turn, LinkedList<BalaAnt> balaList)
	{
		//if enemies are already present in current node, then attack and end turn
				if (checkForEnemies(nodeArray))
				{
					attack(nodeArray, balaList);
				}
				else //only move if no attack was already made prior
				{
					//move to a location on the grid
					move(nodeArray, balaList);
					//check for ants and attack if any are present
					//decided to allow moving and then attacking, but not the opposite as it made logical sense
					if (checkForEnemies(nodeArray))
					{
						attack(nodeArray, balaList);
					}
				}
				
				//checking age and killing if after its turn if lifespan is about to expire
				this.checkLifeSpan(turn, nodeArray);
	}
}
