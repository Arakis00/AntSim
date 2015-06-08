import java.util.LinkedList;

import javax.swing.JOptionPane;


public class QueenAnt extends Ant
{
	final int QUEEN_SPOT = 13; //location of queen on grid
	final int IDNUM = 0; //queens ID number
	final int MOVABLE_RANGE = 0; //queen can't move
	final int LIFE_SPAN = 73000; //in turns, queen lives 20 years (365 * 10 * 20)
	public int assignedID; //for assigning ID's to hatched ants
	/**
	 * ****Constructor****
	 */
	public QueenAnt()
	{
		//id number to assign(starts at 65)
		assignedID = 65;
		
		this.setIDNum(IDNUM); //queen's ID is always zero
		this.setMovableRange(MOVABLE_RANGE); //queen can't move from center square
		this.setLifeSpan(LIFE_SPAN); //queen lives 20 years
	}
	
	/**
	 * hatchAnt takes a random number and decides what type of ant to create/return
	 * @return an Ant object to be hatched/created
	 */
	public Ant hatchAnt(Node[][] nodeArray, int turn)
	{
		//obtaining random number to determine what to hatch
		//50% chance for forager, 25% for both scout and soldier
		randomNum = randomGen.nextInt(100);
		
		if (randomNum < 50) //0 thru 49
		{
			ForagerAnt forager = new ForagerAnt(); //creating new ant
			//setting forager count in the node
			nodeArray[QUEEN_SPOT][QUEEN_SPOT].setForagerCount(nodeArray[QUEEN_SPOT][QUEEN_SPOT].getForagerCount()+1);
			forager.setSpawnTurn(turn); //setting forager's spawn turn
			forager.setIDNum(assignedID); //assigning ID number
			incrementAssignedID();  //incrementing ID number
			return forager;
		}
		else if (randomNum < 75) //50 thru 74
		{
			ScoutAnt scout = new ScoutAnt();
			//setting scout count in the node
			nodeArray[QUEEN_SPOT][QUEEN_SPOT].setScoutCount(nodeArray[QUEEN_SPOT][QUEEN_SPOT].getScoutCount()+1);
			scout.setSpawnTurn(turn); //setting scout's spawn turn
			scout.setIDNum(assignedID);
			incrementAssignedID();
			return scout;
		}
		else //75 or above (99)
		{
			SoldierAnt soldier = new SoldierAnt();
			//setting soldier count in the node
			nodeArray[QUEEN_SPOT][QUEEN_SPOT].setSoldierCount(nodeArray[QUEEN_SPOT][QUEEN_SPOT].getSoldierCount()+1);
			soldier.setSpawnTurn(turn); //setting soldier's spawn turn
			soldier.setIDNum(assignedID);
			incrementAssignedID();
			return soldier;
		}
		
	}
	
	
	public int consumeFood(int food, Node[][] nodeArray)
	{
		//check that food value is greater than 0
		if (food > 0) 
		{
			food--; //consume a unit of food
			return food;
		}
		else
			this.die(nodeArray); //queen dies if no food is present
			
		return food;
	}
	
	/**
	 * gets the current value that is to be assigned to a hatched ant
	 * @return assignedID
	 */
	public int getAssignedID()
	{
		return assignedID;
	}
	
	
	/**
	 * increments the ID to be assigned to a hatched ant
	 */
	public void incrementAssignedID()
	{
		assignedID++;
	}
	
	
	/**
	 * @override if queen dies, sim is over
	 */
	public void die(Node[][] nodeArray)
	{
		this.setAlive(false);
		//show message stating sim has ended
		JOptionPane.showMessageDialog(null, "The simulation has ended due to the queen dying.  Press enter to continue...");
		//exit program
		System.exit(0);
	}
	
	
	/**
	 * coordinates all actions needed to be taken during a turn
	 */
	public void takeTurn(Node[][] nodeArray, LinkedList<Ant> antList, int turn, LinkedList<BalaAnt> balaList)
	{
		//hatching a new ant if it's the first turn of a day
		if (turn%10 == 0)
		{
			antList.add(hatchAnt(nodeArray, turn));
		}
		
		//consuming food
		int food = nodeArray[QUEEN_SPOT][QUEEN_SPOT].getFoodCount();
		nodeArray[QUEEN_SPOT][QUEEN_SPOT].setFoodCount(consumeFood(food, nodeArray));
		
		//checking age of queen and killing if after its turn if lifespan is about to expire
		this.checkLifeSpan(turn, nodeArray);
		
		//if queen is dead, show a message and end the sim
		if (!(this.getAlive()))
		{
			//show message stating sim has ended
			JOptionPane.showMessageDialog(null, "The simulation has ended due to the queen dying.  Press enter to continue...");
			//exit program
			System.exit(0);
		}
	}
}
