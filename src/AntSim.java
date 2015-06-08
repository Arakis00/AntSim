import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;


public class AntSim implements SimulationEventListener, ActionListener
{
	javax.swing.Timer Timer;
	
	//****Attributes****
	//size of the grid (both height and width)
	final int SIZE = 27;
	//middle part of the grid where queen is located
	final int QUEEN_SPOT = 13;
	//starting food amount for center square
	final int FOOD_START = 1000;
	//starting amount of soldiers/foragers/scouts
	final int NUM_SOLDIERS = 10;
	final int NUM_FORAGERS = 50;
	final int NUM_SCOUTS = 4;
	
	//creating a 27x27 instance of ColonyView (GUI)
	private ColonyView colonyView = new ColonyView(SIZE, SIZE);
	//creating a 2D array of colonyNodeView objects (GUI)
	private ColonyNodeView[][] colonyNodeView = new ColonyNodeView[SIZE][SIZE];
	//creating a 2D array of Node objects
	private Node[][] colony = new Node[SIZE][SIZE];
	//holding day/turn information
	private int year = 0;
	private int day = 0;
	private int turn = 0;
	//setting up main GUI window
	private AntSimGUI antSim = new AntSimGUI();
	//master ant list
	LinkedList<Ant> antList = new LinkedList<Ant>();
	//list for balas
	LinkedList<BalaAnt> balaList = new LinkedList<BalaAnt>();
	public int balaID; //for assigning a bala ID number
	
	
	//for obtaining/holding random values
	public static Random randomGen = new Random();
	public int randomNum;
	
	
	/**
	 * Constructor
	 */
	public AntSim() 
	{
		antSim.initGUI(colonyView);
		//creating listener
		antSim.addSimulationEventListener(this);
	}
	
	/**
	 * initColonyNodes - stores separate nodes into a 2-dimensional array and adds each item to the ColonyView
	 */
	public void initColonyNodes()
	{	
		//creating new Nodes/NodeViews to populate the 2D arrays
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				colony[i][j] = new Node(i, j);
				colonyNodeView[i][j] = new ColonyNodeView();
				//setting node view id tags
				colonyNodeView[i][j].setID(i + ", " + j);
				//adding colonyNodeViews to the colonyView
				colonyView.addColonyNodeView(colonyNodeView[i][j], i, j);
			}
		}

		//1,000 units of food in central location at start
		colony[QUEEN_SPOT][QUEEN_SPOT].setFoodCount(FOOD_START);
			
		//generating food for unrevealed locations
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				//determining squares that are outside of the starting revealed area
				if (!(i > QUEEN_SPOT-2 && i < QUEEN_SPOT+2
					&& j > QUEEN_SPOT-2 && j < QUEEN_SPOT+2))
				{
					//generating food randomly
					if (randomGen.nextInt(100) < 25)
					{
						//assigning a value between 500 and 1,000
						colony[i][j].setFoodCount(randomGen.nextInt(501)+500);
					}
				}
				else //setting locations around queen to be shown at startup
				{
					colony[i][j].setShown(true);
				}
			}
		}
		//TEST
		this.createStartingAnts();
		//TEST
		updateGUI();
		//TEST
		
	}
	
	/**
	 * updateColonyView - updates the gui
	 */
	public void updateGUI()
	{
		//copying information from Node array to the ColonyNodeView array(GUI)
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				//determining whether to make spot visible
				if (colony[i][j].getShown())
					colonyNodeView[i][j].showNode();
				//setting whether queen is present (this is obviously inefficient to put here or to handle in this way) *NOTE*
				if (colony[i][j].getQueenPresent())
				{
					colonyNodeView[i][j].setQueen(true);
					//show icon if true
					colonyNodeView[i][j].showQueenIcon();
				}
				//updating ant counts
				colonyNodeView[i][j].setForagerCount(colony[i][j].getForagerCount());
				colonyNodeView[i][j].setScoutCount(colony[i][j].getScoutCount());
				colonyNodeView[i][j].setSoldierCount(colony[i][j].getSoldierCount());
				colonyNodeView[i][j].setBalaCount(colony[i][j].getBalaCount());
				//updating food/pheromone counts
				colonyNodeView[i][j].setFoodAmount(colony[i][j].getFoodCount());
				colonyNodeView[i][j].setPheromoneLevel(colony[i][j].getPheromoneCount());
				
				//determining ant graphical representations to show on the GUI
				if (colony[i][j].getForagerCount() > 0)
					colonyNodeView[i][j].showForagerIcon();
				else
					colonyNodeView[i][j].hideForagerIcon();
				
				if (colony[i][j].getScoutCount() > 0)
					colonyNodeView[i][j].showScoutIcon();
				else
					colonyNodeView[i][j].hideScoutIcon();
				
				if (colony[i][j].getSoldierCount() > 0)
					colonyNodeView[i][j].showSoldierIcon();
				else
					colonyNodeView[i][j].hideSoldierIcon();
				
				if (colony[i][j].getBalaCount() > 0)
					colonyNodeView[i][j].showBalaIcon();
				else
					colonyNodeView[i][j].hideBalaIcon();
				
				//updating time
				day = turn/10;
				year = day/365;
				//setting time
				antSim.setTime("Year: " + year + ", " + "Day: " + day%365 + ", Turn: " + turn%10);
			}
		}
	}
	
	
	/**
	 * populates the linkedlist with the desired amount of ants to start with
	 */
	public void createStartingAnts()
	{
		
		
		//1 queen ant, 10 soldier ants, 50 forager ants, and 4 scout ants at start
				antList.add(new QueenAnt());
				//setting central location to hold queen
				colony[QUEEN_SPOT][QUEEN_SPOT].setQueenPresent(true);
				for (int i = 0; i < NUM_SOLDIERS; i++)
				{
					antList.add(new SoldierAnt());
					//assigning ID numbers
					((SoldierAnt) antList.get(i+1)).setIDNum(i+1);
					//increment amount of soldiers each time a new ant is spawned
					colony[QUEEN_SPOT][QUEEN_SPOT].setSoldierCount(colony[QUEEN_SPOT][QUEEN_SPOT].getSoldierCount()+1);
				}
				for (int i = 0; i < NUM_FORAGERS; i++)
				{
					antList.add(new ForagerAnt());
					//assigning ID numbers
					((ForagerAnt) antList.get(i+11)).setIDNum(i+11);
					//increment amount of foragers each time a new ant is spawned
					colony[QUEEN_SPOT][QUEEN_SPOT].setForagerCount(colony[QUEEN_SPOT][QUEEN_SPOT].getForagerCount()+1);
				}
				for (int i = 0; i < NUM_SCOUTS; i++)
				{
					antList.add(new ScoutAnt());
					//assigning ID numbers
					((ScoutAnt) antList.get(i+61)).setIDNum(i+61);
					//increment amount of scout each time a new ant is spawned
					colony[QUEEN_SPOT][QUEEN_SPOT].setScoutCount(colony[QUEEN_SPOT][QUEEN_SPOT].getScoutCount()+1);
				}
		
	}
	
	
	/**
	 * determines whether queen is still alive or not
	 * @return whether queen is alive
	 */
	public boolean simNotOver()
	{
		return antList.get(0).getAlive();
	}
	
	
	/**
	 * processes a single turn in the simulation
	 */
	public void processTurn()
	{
		
		if (simNotOver()) //sim is still active
		{
			//determine whether to spawn a bala ant
			if (randomGen.nextInt(99) < 3)
			{
				balaList.add(new BalaAnt());
				//assigning ID number to new ant
				balaList.get(balaList.size()-1).setIDNum(balaID);
				//setting bala ant spawn turn
				balaList.get(balaList.size()-1).setSpawnTurn(turn);
				balaID++; //increment ID number for next assignment
			}
			
			//processing turns for friendly ants
			for (int i = 0; i < antList.size(); i++)
			{
				antList.get(i).takeTurn(colony, antList, turn, balaList);
			}
			
			//check for any bala ant deaths, should only apply to old age deaths
			Iterator<BalaAnt> balaItr = balaList.iterator();
			while (balaItr.hasNext())
			{
				if (!(balaItr.next().getAlive()))
				{
					balaItr.remove();
				}
			}
			
			//processing turns for enemy ants
			for (int i = 0; i < balaList.size(); i++)
			{
				balaList.get(i).takeTurn(colony, antList, turn, balaList);
			}
			
			//check for any friendly ant deaths, should only apply to old age deaths
			Iterator<Ant> antItr = antList.iterator();
			while (antItr.hasNext())
			{
				if (!(antItr.next().getAlive()))
				{
					antItr.remove();
				}
			}
			
			//TODO if first turn of day (turn mod 10 = 0), then decrease pheromone levels by half(rounded down)
			if (turn%10 == 0)
			{
				for (int i = 0; i < SIZE; i++)
				{
					for (int j = 0; j < SIZE; j++)
					{
						//divide current pheromone levels by half(rounded down)
						colony[i][j].setPheromoneCount(colony[i][j].getPheromoneCount()/2);
					}
				}
			}
			
			
			//increment turn
			turn++;
			//update GUI
			updateGUI();
		}
		else //sim has ended due to the queen dying
			//this should be taken care of elsewhere, but added here too just in case
		{
			//show message stating sim has ended
			JOptionPane.showMessageDialog(null, "The simulation has ended due to the queen dying.  Press enter to continue...");
			//exit program
			System.exit(0);
		}
	}
	
	
	/**
	 * simulationEventOcurred - responds to when buttons in the gui are pressed
	 */
	public void simulationEventOccurred(SimulationEvent simEvent) 
	{ 
	    if (simEvent.getEventType() == SimulationEvent.NORMAL_SETUP_EVENT) 
	    { 
	        // set up the simulation for normal operation
	    	
	    	//populating the Colony with nodes(Storing components in a 2-D array as well)
			initColonyNodes();
			//populate starting ant list
			//createStartingAnts();
			//TEST - commented out above and moved into init method to try and test synching gui updates
			//update GUI with current information
			updateGUI();
			
	    } 
	    else if (simEvent.getEventType() == SimulationEvent.QUEEN_TEST_EVENT) 
	    { 
	    	/* Tests remaining: deaths from bala attacks */
	    	
	        // set up simulation for testing the queen ant 
	    	
	    	initColonyNodes();
	    	antList.add(new QueenAnt());
			//setting central location to hold queen
			colony[QUEEN_SPOT][QUEEN_SPOT].setQueenPresent(true);
	    	
	    	
	    	//testing turn taking
			//**add 1 to each loop to test starvation death**
	    	for (int i = 0; i < 1000; i++)
	    	{
	    		antList.get(0).takeTurn(colony, antList, turn, balaList);
	    		turn++;
	    		updateGUI();
	    	}
	    	
	    	/*remove turn increment to test this section*
	    	//displaying list classes
	    	for (int i = 0; i < 1000; i++)
	    	{
	    		System.out.println(antList.get(i).getClass());
	    	}*/
	    	
	    	updateGUI();
	    
	    } 
	    else if (simEvent.getEventType() == SimulationEvent.SCOUT_TEST_EVENT) 
	    { 
	        // set up simulation for testing the scout ant 
	    	initColonyNodes();
	    	antList.add(new ScoutAnt());
	    	colony[QUEEN_SPOT][QUEEN_SPOT].setScoutCount(colony[QUEEN_SPOT][QUEEN_SPOT].getScoutCount()+1);
	    	updateGUI();
	    	
	    	//movement test  -  ****PASSED****
	    	System.out.println(antList.get(0).getClass());
	    	
	    	for (int i = 0; i < 1000; i++)
	    	{
	    	((ScoutAnt) antList.get(0)).takeTurn(colony, antList, turn, balaList);
	    	updateGUI();
	   
	    	}
	    	
	    } 
	    else if (simEvent.getEventType() == SimulationEvent.FORAGER_TEST_EVENT) 
	    { 
	        // set up simulation for testing the forager ant 
	    } 
	    else if (simEvent.getEventType() == SimulationEvent.SOLDIER_TEST_EVENT) //shared test with bala ants
	    { 
	        // set up simulation for testing the soldier ant (block out if testing bala)
	    	if (antList.size() < 1 && balaList.size() < 1)
	    	{
	    		initColonyNodes();
	    		antList.add(new SoldierAnt());
		    	balaList.add(new BalaAnt());
		    	colony[QUEEN_SPOT][QUEEN_SPOT].setSoldierCount(1);
		    	updateGUI();
	    	}
	    	
	    	antList.get(0).takeTurn(colony, antList, turn, balaList);
	    	balaList.get(0).takeTurn(colony, antList, turn, balaList);
	    	updateGUI();
	    	
	    	
	    	//set up simulation for testing of bala ant (block out whichever test is not needed atm)
	    	/*
	    	initColonyNodes();
	    	balaList.add(new BalaAnt());
	    	colony[0][0].setBalaCount(colony[0][0].getBalaCount()+1);
	    	updateGUI();
	    	
	    	//movement test  -  ****PASSED****
	    	System.out.println(balaList.get(0).getClass());
	    	
	    	for (int i = 0; i < 1000; i++)
	    	{
	    	balaList.get(0).takeTurn(colony, antList, turn, balaList);
	    	updateGUI();
	    	}
	    	*/
	    	
	    } 
	    else if (simEvent.getEventType() == SimulationEvent.RUN_EVENT) 
	    { 
	        // run the simulation continuously
	    	Timer = new javax.swing.Timer(700, this);
	    	Timer.start();
	    	/*while (simNotOver())
	    	{
	    		processTurn();
	    	}*/
	    } 
	    else if (simEvent.getEventType() == SimulationEvent.STEP_EVENT) 
	    { 
	       // run the next turn of the simulation
	    	
	    	//running a single turn
	    	processTurn();
	    } 
	    else 
	    { 
	        // This should never execute
	    	System.out.println("Something bad just happened!");
	    } 
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		// TODO Auto-generated method stub
		if (!antList.get(0).getAlive())
		{
			Timer.stop();
		}
		else
		{
			processTurn();
		}
		
	}

}
