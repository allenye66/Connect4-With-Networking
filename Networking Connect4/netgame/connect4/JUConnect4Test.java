package netgame.connect4;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.junit.Test;

import grid.Grid;
import grid.Location;
import static org.junit.Assert.assertTrue;

/**
 * Connect4 tests: Piece
 *
 * @author 
 * @version 
 * @author Period: 
 * @author Assignment: 
 * 
 */

public class JUConnect4Test 
{
	// --Test Piece
	/**
	 * Constructs Piece sets and gets Color
	 */

	@Test
	public void pieceConstructorAndAccessors() 
	{
		Piece piece = new Piece(Color.RED);
		assertTrue("Invalid Color", piece.getColor().equals(Color.RED));
		piece.setColor(Color.BLUE);
		assertTrue("Invalid Color", piece.getColor().equals(Color.BLUE));
	}

	// --Test Game
	/**
	 * Constructs game
	 * 
	 */
	private Connect4Game getGame() 
	{
		Connect4Game og = new Connect4Game(false, "Bob", "John", Color.BLUE, Color.RED, null, 8);
		og.gamePlaying = true;
		return og;

	}

	private Connect4Window getWindow() 
	{
		JLabel message = new JLabel("Welcome to Networked Connect4!", JLabel.CENTER);
        message.setFont(new Font("Serif", Font.BOLD, 16));
        JTextField hostInput = new JTextField(30);
        Connect4Window ow=null;
		try 
		{
			ow = new Connect4Window("localhost",45017,false);
			return ow;
		}
		catch (IOException e) 
		{
            message.setText("Could not connect to specified host and port.");
            hostInput.selectAll();
            hostInput.requestFocus();
        }
       return ow;
	}
	
	private Connect4GameHub getHub() 
	{
		JLabel message = new JLabel("Welcome to Networked Connect4!", JLabel.CENTER);
        message.setFont(new Font("Serif", Font.BOLD, 16));
        Connect4GameHub hub=null;
        try 
        {
            hub = new Connect4GameHub(45017);
        }
        catch (Exception e) 
        {
            message.setText("Error: Can't listen on port ");
        }
        return hub;
	}
	/**
	 * Constructs Connect4 Game and tests its existence
	 */
	@Test
	public void connect4Game() 
	{
		Connect4Game game = getGame();
		assertTrue(game != null);
	}

	/**
	 * Constructs Connect4 Client and tests its existence
	 */

	@Test
	public void connect4Window() 
	{
		Connect4GameHub hub = getHub();
		Connect4Window win = getWindow();
		assertTrue(win != null);
	}
	
	/**
	 * Constructs Connect4 Client and tests its existence
	 */

	@Test
	public void connect4Hub() {
		Connect4GameHub hub = getHub();
		assertTrue(hub != null);
	}

	/**
	 * tests windowID
	 */
	@Test
	public void windowID() 
	{
		Connect4GameHub hub = getHub();
		Connect4Window win = getWindow();
		System.out.println(win.myID);
		assertTrue(win.myID>0);
		//hub.getState().playerDisconnected = true;
	   // hub.sendToAll(hub.getState());
	}
	
	/**
	 * tests connection and communication in client
	 */
	@Test
	public void comm() 
	{
		Connect4GameHub hub = getHub();
		Connect4Window win = getWindow();
		assertTrue(win.connection != null);
		//hub.getState().playerDisconnected = true;
	    //hub.sendToAll(hub.getState());
	}
	
	/**
	 * tests player1 and its name
	 */
	@Test
	public void player1Name() 
	{
		Connect4Game og = getGame();
		Player player1 = (Player) og.getPlayer()[0];
		assertTrue(player1 != null);
		assertTrue("test names", "Bob".equals(player1.getName()));
	}

	/**
	 * tests player2 and its name
	 */
	@Test
	public void player2Name() 
	{
		Connect4Game og = getGame();

		Player player2 = (Player) og.getPlayer()[1];
		assertTrue(player2 != null);
		assertTrue("test names", "John".equals(player2.getName()));
	}

	/**
	 * There should be 2 red and 2 blue pieces initially. Gets the occupied
	 * positions on the board and counts and red and blue pieces on the board
	 */
	@Test
	public void countInitialRedPieces() 
	{
		Connect4Game og = getGame();

		/* Test initial State */
		Grid<Piece> board = og.getConnect4World().getGrid();
		int numRed = 0;
		for (Location loc : board.getOccupiedLocations()) {

			if (board.get(loc).getColor().equals(Color.RED)) {
				numRed++;
				System.out.println(loc + " red");
			}
		}
		assertTrue(numRed == 2);

	}

	/**
	 * There should be 2 red and 2 blue pieces initially. Gets the occupied
	 * positions on the board and counts and red and blue pieces on the board
	 */
	@Test
	public void countInitialBluePieces() 
	{
		Connect4Game og = getGame();

		/* Test initial State */
		Grid<Piece> board = og.getConnect4World().getGrid();

		int numBlue = 0;
		for (Location loc : board.getOccupiedLocations()) {

			if (board.get(loc).getColor().equals(Color.BLUE)) {
				numBlue++;
			}
		}
		assertTrue(numBlue == 2);
	}

	/**
	 * The initial location on a 8x8 grid are 3,3 and 4,4 for red and 3,4 and
	 * 4,3 for blue
	 */
	@Test
	public void initialRedLocation() 
	{
		Connect4Game og = getGame();

		/* Test initial State */
		Grid<Piece> board = og.getConnect4World().getGrid();

		Location three3 = new Location(3, 3);

		Location four4 = new Location(4, 4);
		ArrayList<Location> locations = new ArrayList<Location>();
		locations.add(three3);

		locations.add(four4);
		for (Location loc : board.getOccupiedLocations()) {
			if (loc.equals(three3) || loc.equals(four4)) {
				assertTrue(locations.contains(loc));
				assertTrue(board.get(loc).getColor().equals(Color.RED));
			}
		}

	}

	/**
	 * The initial location on a 8x8 grid are 3,3 and 4,4 for red and 3,4 and
	 * 4,3 for blue
	 */
	@Test
	public void initialBlueLocation() 
	{
		Connect4Game og = getGame();

		/* Test initial State */
		Grid<Piece> board = og.getConnect4World().getGrid();

		Location three4 = new Location(3, 4);
		Location four3 = new Location(4, 3);

		ArrayList<Location> locations = new ArrayList<Location>();

		locations.add(three4);
		locations.add(four3);

		for (Location loc : board.getOccupiedLocations()) {
			if (loc.equals(three4) || loc.equals(four3)) {
				assertTrue(locations.contains(loc));
				assertTrue(board.get(loc).getColor().equals(Color.BLUE));
			}
		}
	}





	public void player2PlayThree5(Connect4Game og) {
		og.gamePlaying=true;
		Player player2 = (Player) og.getPlayer()[1];

		Location three5 = new Location(3, 5);

		Connect4World ow = og.getConnect4World();
		ow.locationClicked(three5);
		player2.play(); 
		player2.updateGrid();

	}



	/**
	 * clicked location should be red
	 */
	@Test
	public void player2GameClickLocationRed() 
	{
		Connect4Game og = getGame();
		Grid<Piece> board = og.getConnect4World().getGrid();
		player2PlayThree5(og);

		Location three5 = new Location(3, 5);
		assertTrue(board.get(three5).getColor().equals(Color.RED));

	}

	/**
	 * locations up till the clicked location should turn red
	 */
	@Test
	public void player2GameLocationInRowRed() 
	{
		Connect4Game og = getGame();
		Grid<Piece> board = og.getConnect4World().getGrid();
		player2PlayThree5(og);
		assertTrue(board.get(new Location(3, 4)).getColor().equals(Color.RED));
	}


}

