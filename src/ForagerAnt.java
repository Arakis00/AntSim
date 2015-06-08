import java.util.LinkedList;
import java.util.Stack;


public class ForagerAnt extends Ant
{
	//whether forager is carrying food or not
	private boolean carryingFood;
	//to flag when a forager has gotten stuck in a loop
	private boolean stuckInLoop;
	//to maintain movement history
	private Stack<Node> movementHistory = new Stack<Node>();
	
	public ForagerAnt()
	{
		//TODO
		//foragers start off not carrying food
		carryingFood = false;
		stuckInLoop = false;
	}
	
	
	/**Setter
	 * sets whether forager is carrying food or not
	 * @param carrying whether forager is carrying food or not
	 */
	public void setCarryingFood(boolean carrying)
	{
		carryingFood = carrying;
	}
	
	/**
	 * sets whether a forager has gotten stuck in a loop
	 * @param stuck
	 */
	public void setStuckInLoop(boolean stuck)
	{
		stuckInLoop = stuck;
	}
	
	
	/**Getter
	 * returns whether forager is carrying food or not
	 * @return whether forager is carrying food or not
	 */
	public boolean getCarryingFood()
	{
		return carryingFood;
	}
	
	/**
	 * returns whether the stuck in loop flag is active or not
	 * @return whether forager got stuck in loop or not while trying to find a food source
	 */
	public boolean getStuckInLoop()
	{
		return stuckInLoop;
	}
	
	
	/**
	 * @override sets a forager ant to be removed from the list and decrements its count in the node it resides in
	 */
	public void die(Node[][] nodeArray)
	{
		this.setAlive(false);
		//decrementing forager count within its current node
		nodeArray[this.getX()][this.getY()].setForagerCount(nodeArray[this.getX()][this.getY()].getForagerCount()-1);
		//if carrying food, dropping it in the current square
		if (this.getCarryingFood())
		{
			nodeArray[this.getX()][this.getY()].setFoodCount(nodeArray[this.getX()][this.getY()].getFoodCount()+1);
		}
	}
	
	
	/**
	 * deposits 10 pheromones in current square if current node's value is less than 1000
	 * @param nodeArray grid that is used
	 */
	public void depositPheromones(Node[][] nodeArray)
	{
		//deposit 10 pheromone before moving if count < 1000
		if (nodeArray[this.getX()][this.getY()].getPheromoneCount() < 1000)
		{
			nodeArray[this.getX()][this.getY()].setPheromoneCount(nodeArray[this.getX()][this.getY()].getPheromoneCount()+10);
		}
	}
	
	
	/**
	 * moves randomly until a new food source is found then turns off loop flag
	 * @param nodeArray grid that is moved on
	 */
	public void moveRandomly(Node[][] nodeArray)
	{
		boolean finished = false; //loop flag
		LinkedList<Node> tempNodes = new LinkedList<Node>(); //to hold a temporary node values that have highest pheromones
		
		while (!finished)
		{
		
			try
			{
				//checking surrounding nodes
				for (int i = this.getX()-1; i <= this.getX()+1; i++)
				{
					for (int j = this.getY()-1; j <= this.getY()+1; j++)
					{
						//if current spot in loop is not equal to the previous movement spot
						if (i != movementHistory.peek().getXLoc() ||
								j != movementHistory.peek().getYLoc())
						{
							//if current spot in loop is not the queen spot
							if (i != QUEEN_LOC ||
									j != QUEEN_LOC)
							{
								//if current spot in loop is not identical to ant's current location
								if (i != this.getX() ||
										j != this.getY())
								{
									//if current node is visible
									if (nodeArray[i][j].getShown())
									{
										if (tempNodes.size() < 1) //starting new linkedlist
										{
											tempNodes.add(nodeArray[i][j]);
										}
										else //list already populated
										{
											//****************************************************************
											//have it checking for food even though it wasn't part of the specs
											//figured it would make the foragers operate more efficiently
											//****************************************************************
											//add to list if food are the same
											if (nodeArray[i][j].getFoodCount() == tempNodes.get(0).getFoodCount())
											{
												tempNodes.add(nodeArray[i][j]);
											}
											//if pheromones are higher then clear list and add new value
											else if (nodeArray[i][j].getFoodCount() > tempNodes.get(0).getFoodCount())
											{
												tempNodes.clear();
												tempNodes.add(nodeArray[i][j]);
											}
										}
									}
								}
							}
						}
					}
				}
				//loop fully executed
				finished = true;
			}
			catch (IndexOutOfBoundsException e)
			{
				continue; //ignore out of bounds exception and keep searching
			}
		
		}
		//randomly determining which space to move to
		randomNum = randomGen.nextInt(tempNodes.size());
		//decrementing forager count in current node
		nodeArray[this.getX()][this.getY()].setForagerCount(nodeArray[this.getX()][this.getY()].getForagerCount()-1);
		//moving ant
		this.setX(tempNodes.get(randomNum).getXLoc());
		this.setY(tempNodes.get(randomNum).getYLoc());
		//incrementing forager count in new node
		nodeArray[this.getX()][this.getY()].setForagerCount(nodeArray[this.getX()][this.getY()].getForagerCount()+1);
		//adding to movement history
		movementHistory.add(tempNodes.get(randomNum));
	
		//if food is present, pick it up and return to nest
		if (nodeArray[this.getX()][this.getY()].getFoodCount() > 0)
		{
			//pick up food if it's present
			this.setCarryingFood(true);
			//decrement current square's food count
			nodeArray[this.getX()][this.getY()].setFoodCount(nodeArray[this.getX()][this.getY()].getFoodCount()-1);
			//removing loop flag
			this.setStuckInLoop(false);
		}
	}
	
	
	/**
	 * handles the movement of a forager ant
	 * @param nodeArray grid that is moved within
	 */
	public void move(Node[][] nodeArray)
	{
		
		
		if (!carryingFood) //in forage mode and looking for food
		{
			
			boolean finished = false; //loop flag
			LinkedList<Node> tempNodes = new LinkedList<Node>(); //to hold a temporary node values that have highest pheromones
			
			
			//testing for loop....not a great test, but seems to work for the most part
			if (movementHistory.size() > 9) //checking for loops if at least 9 moves have been made
			{
				int count = 0; //to count duplicate occurrences
				//creating a copied stack as to pop values and not affect the original stack
				Stack<Node> copiedStack = (Stack<Node>) movementHistory.clone();
				//to hold node on top of stack
				Node tempNode = copiedStack.pop();
				
				while (!copiedStack.empty())
				{
					//checking for equality
					if (tempNode.getXLoc() == copiedStack.peek().getXLoc() &&
						tempNode.getYLoc() == copiedStack.peek().getYLoc())
					{
						//increment count if the same
						count++;
					}
					copiedStack.pop();  //remove previous test from the stack
				}
				
				
				//if more than 4 occurrences of last node being visited (one occurrence is already stored in a temp variable)
				if (count > 3)
				{
					//set stuck in loop to true to move randomly on future turns until a new food source is found
					this.setStuckInLoop(true);
					count = 0; //reset counter
				}
			}
			
			
			
			if (stuckInLoop) //got stuck in loop while searching for food, now moving randomly until a new source is found
			{
				//move randomly until food source is found
				this.moveRandomly(nodeArray);
			}
			else //no looping has been detected while searching for food
			{
			
				while (!finished)
				{
				
					try
					{
						//checking surrounding nodes
						for (int i = this.getX()-1; i <= this.getX()+1; i++)
						{
							for (int j = this.getY()-1; j <= this.getY()+1; j++)
							{
								//if current spot in loop is not equal to the previous movement spot
								if (i != movementHistory.peek().getXLoc() ||
										j != movementHistory.peek().getYLoc())
								{
									//if current spot in loop is not the queen spot
									if (i != QUEEN_LOC ||
											j != QUEEN_LOC)
									{
										//if current spot in loop is not identical to ant's current location
										if (i != this.getX() ||
												j != this.getY())
										{
											//if current node is visible
											if (nodeArray[i][j].getShown())
											{
												if (tempNodes.size() < 1) //starting new linkedlist
												{
													tempNodes.add(nodeArray[i][j]);
												}
												else //list already populated
												{
													//add to list if pheromones are the same
													if (nodeArray[i][j].getPheromoneCount() == tempNodes.get(0).getPheromoneCount())
													{
														tempNodes.add(nodeArray[i][j]);
													}
													//if pheromones are higher then clear list and add new value
													else if (nodeArray[i][j].getPheromoneCount() > tempNodes.get(0).getPheromoneCount())
													{
														tempNodes.clear();
														tempNodes.add(nodeArray[i][j]);
													}
												}
											}
										}
									}
								}
							}
						}
						//loop fully executed
						finished = true;
					}
					catch (IndexOutOfBoundsException e)
					{
						continue; //ignore out of bounds exception and keep searching
					}
				
				}
			
				//randomly determining which space to move to
				randomNum = randomGen.nextInt(tempNodes.size());
				//decrementing forager count in current node
				nodeArray[this.getX()][this.getY()].setForagerCount(nodeArray[this.getX()][this.getY()].getForagerCount()-1);
				//moving ant
				this.setX(tempNodes.get(randomNum).getXLoc());
				this.setY(tempNodes.get(randomNum).getYLoc());
				//incrementing forager count in new node
				nodeArray[this.getX()][this.getY()].setForagerCount(nodeArray[this.getX()][this.getY()].getForagerCount()+1);
				//adding to movement history
				movementHistory.add(tempNodes.get(randomNum));
				//if food is present, pick it up and return to nest
				if (nodeArray[this.getX()][this.getY()].getFoodCount() > 0)
				{
					//pick up food if it's present
					this.setCarryingFood(true);
					//decrement current square's food count
					nodeArray[this.getX()][this.getY()].setFoodCount(nodeArray[this.getX()][this.getY()].getFoodCount()-1);
				}
			}
			}//???
			else  //carrying food and returning to nest
			{
				if (movementHistory.size() > 0)
				{
					//checking whether queen node is within movable range
					if (this.getX() >= QUEEN_LOC-1 &&
							this.getX() <= QUEEN_LOC+1 &&
							this.getY() >= QUEEN_LOC-1 &&
							this.getY() <= QUEEN_LOC+1)
					{
						//deposit 10 pheromone before moving if count < 1000
						this.depositPheromones(nodeArray);
						//decrement current nodes forager count
						nodeArray[this.getX()][this.getY()].setForagerCount(nodeArray[this.getX()][this.getY()].getForagerCount()-1);
						//setting coordinates to center square
						this.setX(QUEEN_LOC);
						this.setY(QUEEN_LOC);
						//increment forager count in new square
						nodeArray[this.getX()][this.getY()].setForagerCount(nodeArray[this.getX()][this.getY()].getForagerCount()+1);
						//drop off food in queen's square
						nodeArray[this.getX()][this.getY()].setFoodCount(nodeArray[this.getX()][this.getY()].getFoodCount()+1);
						//turn carry flag off
						this.setCarryingFood(false);
						//set stuck in loop back to false in case it was activated
						this.setStuckInLoop(false);
						//reset move history
						movementHistory.clear();
						//add central node to the movement history
						movementHistory.add(nodeArray[this.getX()][this.getY()]);
					}
					else //retrace steps back toward nest entrance since colony entrance isn't within movable range
					{
						//remove top stack entry and move there
						Node tempNode = movementHistory.pop();
						//deposite 10 pheromone if count < 1000 before moving
						this.depositPheromones(nodeArray);
						//decrement current nodes forager count
						nodeArray[this.getX()][this.getY()].setForagerCount(nodeArray[this.getX()][this.getY()].getForagerCount()-1);
						//setting coordinates to new square to be moved to
						this.setX(tempNode.getXLoc());
						this.setY(tempNode.getYLoc());
						//increment forager count in new square
						nodeArray[this.getX()][this.getY()].setForagerCount(nodeArray[this.getX()][this.getY()].getForagerCount()+1);
					}
				
				}
			}
	}
	
	
	/**
	 * coordinates all actions needing to be taken during a turn
	 */
	public void takeTurn(Node[][] nodeArray, LinkedList<Ant> antList, int turn, LinkedList<BalaAnt> balaList)
	{
		//if this is the ant's first turn, add the queen's node to the movement history
		if (this.getSpawnTurn() - turn == 0)
		{
			movementHistory.add(nodeArray[QUEEN_LOC][QUEEN_LOC]);
		}
		
		//moving the ant
		move(nodeArray);
		
		
		//checking age and killing if after its turn if lifespan is about to expire
		this.checkLifeSpan(turn, nodeArray);
	}
}
