/**
 * Node - class to store the information of a particular 
 *         block within the ant colony grid.
 */
public class Node 
{
	// ****Attributes****
	
	//whether block should be shown or not
	private boolean shown;
	//whether queen is present or not
	private boolean queenPresent;
	//number of foragers present in the block
	private int foragerCount;
	//number of scouts present in the block
	private int scoutCount;
	//number of soldiers present in the block
	private int soldierCount;
	//number of balas present in the block
	private int balaCount;
	//amount of food present in the block
	private int foodCount;
	//level of pheromones present in the block
	private int pheromoneCount;
	
	//to hold coordinate information for storage in an array
	private int XLoc;
	private int YLoc;

	/**
	 * Constructor
	 */
	public Node(int x, int y)
	{
		shown = false;
		queenPresent = false;
		foragerCount = 0;
		scoutCount = 0;
		soldierCount = 0;
		balaCount = 0;
		foodCount = 0;
		pheromoneCount = 0;
		//storing location information
		XLoc = x;
		YLoc = y;
	}
	
	// ****Setters****
	
	/**
	 * set whether block should be shown
	 * @param show - whether to show or not
	 */
	public void setShown(boolean show)
	{
		shown = show;
	}
	
	/**
	 * set whether queen is present in the block
	 * @param present - whether queen is present or not
	 *       **(Only present in center block)**
	 */
	public void setQueenPresent (boolean present)
	{
		queenPresent = present;
	}
	
	/**
	 * set the amount of foragers present in the block
	 * @param count - amount of foragers
	 */
	public void setForagerCount(int count)
	{
		foragerCount = count;
	}
	
	/**
	 * set the amount of scouts present in the block
	 * @param count - amount of scouts
	 */
	public void setScoutCount(int count)
	{
		scoutCount = count;
	}
	
	/**
	 * set the amount of soldiers present in the block
	 * @param count - amount of soldiers
	 */
	public void setSoldierCount(int count)
	{
		soldierCount = count;
	}
	
	/**
	 * set the amount of balas present in the block
	 * @param count - amount of balas
	 */
	public void setBalaCount(int count)
	{
		balaCount = count;
	}
	
	/**
	 * set the amount of food present in the block
	 * @param count - amount of food
	 */
	public void setFoodCount(int count)
	{
		foodCount = count;
	}
	
	/**
	 * set the pheromone level present in the block
	 * @param count - amount of pheromones
	 */
	public void setPheromoneCount(int count)
	{
		pheromoneCount = count;
	}
	
	// ****Getters****
	
	/**
	 * 
	 * @return whether block is visible or not
	 */
	public boolean getShown()
	{
		return shown;
	}
	
	/**
	 * 
	 * @return whether queen is present or not
	 */
	public boolean getQueenPresent()
	{
		return queenPresent;
	}
	
	/**
	 * 
	 * @return amount of foragers present in the block
	 */
	public int getForagerCount()
	{
		return foragerCount;
	}
	
	/**
	 * 
	 * @return amount of scouts present in the block
	 */
	public int getScoutCount()
	{
		return scoutCount;
	}
	
	/**
	 * 
	 * @return amount of soldiers present in the block
	 */
	public int getSoldierCount()
	{
		return soldierCount;
	}
	
	/**
	 * 
	 * @return amount of balas present in the block
	 */
	public int getBalaCount()
	{
		return balaCount;
	}
	
	/**
	 * 
	 * @return amount of food present in the block
	 */
	public int getFoodCount()
	{
		return foodCount;
	}
	
	/**
	 * 
	 * @return amount of pheromones present in the block
	 */
	public int getPheromoneCount()
	{
		return pheromoneCount;
	}
	
	
	//For setting/retrieving location information.
	public void setXLoc(int x)
	{
		XLoc = x;
	}
	
	public void setYLoc(int y)
	{
		YLoc = y;
	}
	
	public int getXLoc()
	{
		return XLoc;
	}
	
	public int getYLoc()
	{
		return YLoc;
	}
}
