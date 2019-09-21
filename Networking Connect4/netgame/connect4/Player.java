package netgame.connect4;
import java.awt.Color;

import grid.Location;

/**
 * Connect4Player.java
 *
 * This is the top-level class for all player classes.
 * 
 *  @author  
 *  @version 
 *  @author  Period: 
 *  @author  Assignment: 
 * 
 *  @author  Sources: 
 * 
 *  
 */
public class Player
{

	public Location move;
	/** The world */
	private Connect4World world;

	/** The grid */
	private grid.Grid<Piece> g;
	
	/** The name of the player */
	private String name;

	/** The color of this player's game pieces */
	private Color co;
	int gridLength;


	/**
	 * Constructs an Connect4 player object.
	 * @param w the world
	 * @param n the name 
	 * @param c the color
	 */
	public Player(Connect4World w, String n, Color c,int gridLengthN)
	{
		world = w;
		g = world.getGrid();
		name = n;
		co = c;
		gridLength=gridLengthN;
	}

	/**
	 * Retrieves the next play for the human.
	 * Postcondition: the returned location is an allowed play.
	 * @return the location for the next play
	 */
	public Location getPlay()
	{
		Location loc;
		do
		{
			loc = getWorld().getPlayerLocation();
		}
		while (! isAllowedPlay(loc));
		return loc;
	}
	
	public String getName()
	{
		return name;	
	}

	/**
	 * Gets the Connect4 world.
	 * @return the Connect4 world
	 */
	public Connect4World getWorld()
	{
		return world;	
	}

	/**
	 * Determines if the player can make a play.
	 * @return true if the player can play; false otherwise
	 */
	public boolean canPlay()
	{
		for(int r = 0; r < gridLength; r++)
		{
			for(int c = 0; c < gridLength; c++)
			{
				Location l = new Location(r, c);
				if(isAllowedPlay(l))
				{
					return true;
				}
			}
		}
		return false;	
	}


	/**
	 * Determines if this play is allowed by the rules
	 * @param loc location to be checked
	 * @return true if this location is allowed to be played;
	 *         false otherwise
	 */
	public boolean isAllowedPlay(Location loc)
	{

		int emptyCnt=0;
		
		if (world.getGrid().get(loc) ==null) {
			emptyCnt++;
		}
		
		
		try
		{ 
			Thread.sleep(30); 
		}
		catch (InterruptedException e)
		{ 
			System.out.println("InterruptedException occurred."); 
		}
		
		return (emptyCnt>0);
	}

	/**
	 * Make the play indicated by calling getPlay.
	 * (Place a piece and "flip" the appropriate other pieces.)
	 */
	public void play()
	{
		
		move = getPlay();
		
	}
	
	public void updateGrid()
	{
		int row = move.getRow();
		int col = move.getCol();

		for (int i = gridLength-1; i >= 0 ; i--) {
			Location l= new Location(i, col);
			if (world.getGrid().get(l) == null) {
				world.add(l, new Piece(co));
				break;
			}
		}
	}

}
