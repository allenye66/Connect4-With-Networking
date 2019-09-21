package netgame.connect4;
import java.util.concurrent.Semaphore;

import grid.BoundedGrid;
import grid.Location;
import world.World;

/**
 * Connect4World.java
 * 
 * An <CODE>Connect4World</CODE> object represents an Connect4 world.
 * 
 *  @author  
 *  @version 
 *  @author  Period:
 *  @author  Assignment:
 * 
 *  @author  Sources:
 * 
 */
public class Connect4World extends World<Piece>
{
	public Location move;
	/** The Connect4 game */
	private Connect4Game game;

	/** A semaphore to prevent getPlayerLocation from executing
	 *  before setPlayerLocation */
	private Semaphore lock;

	/** The last selected player location */
	private Location playerLocation;

	/**
	 * Construct an Connect4 world
	 * game The Connect4 game
	 */
	public Connect4World(Connect4Game game, int gridLengthN)
	{
		super(new BoundedGrid<Piece>(gridLengthN, gridLengthN));
		

		this.game = game;
		lock = new Semaphore(0);
		playerLocation = null;
		String[] funFacts = new String[] {"Did you know TinkleMeep was founded in 2018?", 
		    "Did you know TinkleMeep is based off the character Meap from Phineas and Ferb?", 
		    "Did you know TinkleMeep was founded by three highschoolers for their final project?", 
		    "Did you know the name TinkleMeep was created by a random company name generator online?"};
		String fun = funFacts[(int)(Math.random() * 4)];
		setMessage("Please wait for your opponent to connect. While you wait, here is a fun fact... \n" + fun);


		System.setProperty("gui.selection", "hide");
		System.setProperty("gui.tooltips", "hide");
		System.setProperty("gui.watermark", "hide");
        int gridLength=gridLengthN;
		//add(new Location(gridLength/2-1, gridLength/2-1), new Piece(Color.RED));
		//add(new Location(gridLength/2-1,gridLength/2), new Piece(Color.BLUE));
		//add(new Location(gridLength/2, gridLength/2-1), new Piece(Color.BLUE));
		//add(new Location(gridLength/2, gridLength/2), new Piece(Color.RED));
	}

	/**
	 * Handles the mouse location click.
	 * @param loc the location that was clicked
	 * @return true because the click has been handled
	 */
	@Override
	public boolean locationClicked(Location loc)
	{
		setPlayerLocation(loc);
		return true;

	}

	/**
	 * Sets <CODE>playerLocation</CODE>.
	 * @param loc the location to be used to set the player location
	 */
	private void setPlayerLocation(Location loc)
	{
		lock.drainPermits();	// Remove all permits
		playerLocation = loc;
		if (game.gamePlaying) {
			lock.release();			// Allow getPlayerLocation to run once
		}
	}

	/**
	 * Gets the last player location chosen by the human player.
	 * @return the last location chosen by the human player
	 */
	public Location getPlayerLocation()
	{
		try
		{
			lock.acquire();		// Block until setPlayerLocation runs
			return playerLocation;
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(
				"Had catastrophic InterruptedException");
		}
	}

	/**
	 * Sets the message in the GridWorld GUI.<br>
	 * Postcondition: At least a second has elapsed before returning.
	 * @param msg the message text
	 */
	@Override
	public void setMessage(String msg)
	{
		super.setMessage(msg);
		try
		{ Thread.sleep(33); }
		catch (InterruptedException e)
		{ System.out.println("InterruptedException occurred."); }
	}
}
