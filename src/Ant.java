import java.util.LinkedList;
import java.util.Random;

/**
 * Generic class to hold the characteristics common to all ant types.
 *
 */
public class Ant 
{
	//number of actions an ant can take in a single turn
	final int ACTIONS_PER_TURN = 1;
	//queen location
	final int QUEEN_LOC = 13;
	//number of squares an ant can move in a single turn
	private int movableRange;
	//life span of an ant
	private int lifeSpan;
	//set spawn turn of ant
	private int spawnTurn;
	//unique ID number assigned to each ant as it's spawned
	private int IDNum;
	//whether an ant can move into spots that have been revealed or not
	boolean moveUnrevealed;
	//whether an ant is still alive or not
	boolean isAlive;
	//two variables to hold the x and y location
	private int locationX;
	private int locationY;
	
	//for obtaining/holding random values
	public static Random randomGen = new Random();
	public int randomNum;
	
	/**
	 * ****Default Constructor****
	 */
	public Ant()
	{
		movableRange = 1;
		lifeSpan = 3650; //in turns, equal to 1 year
		moveUnrevealed = false;
		isAlive = true;
		locationX = QUEEN_LOC;
		locationY = QUEEN_LOC;
		spawnTurn = 0;
	}
	
	// ****Setters****
	public void setMovableRange(int range)
	{
		movableRange = range;
	}
	
	public void setLifeSpan(int span)
	{
		lifeSpan = span;
	}
	
	public void setMoveUnrevealed(boolean unrevealed)
	{
		moveUnrevealed = unrevealed;
	}
	
	public void setAlive(boolean alive)
	{
		isAlive = alive;
	}
	
	public void setX(int x)
	{
		locationX = x;
	}
	
	public void setY(int y)
	{
		locationY = y;
	}
	
	public void setIDNum(int ID)
	{
		IDNum = ID;
	}
	
	public void setSpawnTurn(int turn)
	{
		spawnTurn = turn;
	}
	
	
	//****Getters****
		public int getMovableRange()
		{
			return movableRange;
		}
		
		public int getLifeSpan()
		{
			return lifeSpan;
		}
		
		public boolean getMoveUnrevealed()
		{
			return moveUnrevealed;
		}
		
		public boolean getAlive()
		{
			return isAlive;
		}
		
		public int getX()
		{
			return locationX;
		}
		
		public int getY()
		{
			return locationY;
		}
		
		public int getIDNum()
		{
			return IDNum;
		}
		
		public int getSpawnTurn()
		{
			return spawnTurn;
		}
		
		// ****Methods****
		/**
		 * Compares the current turn to the ant's lifespan to determine whether the ant should die
		 * @param turn the current turn in the simulation
		 */
		public void checkLifeSpan(int turn, Node[][] nodeArray)
		{
			if (lifeSpan <= (turn-spawnTurn))
			{
				this.die(nodeArray);
			}
		}
		
		/**
		 * Sets whether an ant is alive to false.
		 * Decided to let this handle decrementing node counts too so will have to be overridden in all other derived classes
		 */
		public void die(Node[][] nodeArray)
		{
			this.setAlive(false);
		}
		
		/**
		 * coordinates all actions needed to be taken during a turn
		 */
		public void takeTurn(Node[][] nodeArray, LinkedList<Ant> antList, int turn, LinkedList<BalaAnt> balaList)
		{
			//intended to be overridden so left blank
		}
		
	
	
}

