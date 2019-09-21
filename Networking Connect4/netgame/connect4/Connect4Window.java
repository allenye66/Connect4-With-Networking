package netgame.connect4;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import grid.Location;
import netgame.common.Client;

/**
 * This window represents one player in a two-player networked game of Connect4.
 * The window is meant to be created by the program netgame.connect4.Main.
 * @author 
 * @version 
 * @author Period:
 * @author Assignment: 
 * 
 * @author Sources:
 */
 
public class Connect4Window extends JFrame {

	/**
	 * The state of the game. This state is a copy of the official state, which is
	 * stored on the server. When the state changes, the state is sent as a message
	 * to this window. (It is actually sent to the Connect4Client object that
	 * represents the connection to the server.) When that happens, the state that
	 * was received in the message replaces the value of this variable, and the
	 * board and UI is updated to reflect the changed state. This is done in the
	 * newState() method, which is called by the Connect4Client object.
	 */
	public Connect4GameState state;

	public Connect4Game game;

	public int myID; // The ID number that identifies the player using this window.
	boolean gameOver;
	public Connect4Client connection; // The Client object for sending and receiving
										// network messages.
	public int clickedCount=0;
	public int connectN=4;
	int gridLength;
	public int gameMode;

	/**
	 * This class defines the client object that handles communication with the Hub.
	 */
	public class Connect4Client extends Client 
	{

		/**
		 * Connect to the hub at a specified host name and port number.
		 */
		public Connect4Client(String hubHostName, int hubPort) throws IOException 
		{
			super(hubHostName, hubPort);
		}

		/**
		 * Responds to a message received from the Hub. The only messages that are
		 * supported are Connect4GameState objects. When one is received, the newState()
		 * method in the Connect4Window class is called. To avoid problems with
		 * synchronization, that method is called using SwingUtilities.invokeLater() so
		 * that it will run in the GUI event thread.
		 */
		protected void messageReceived(final Object message) 
		{
			if (message instanceof Connect4GameState) 
			{
				SwingUtilities.invokeLater(new Runnable() 
				{
					public void run() // calls a method at the end of the Connect4Window class
					{ 
						//System.out.print(message.addrow + " "+ message.addcol);
						newState((Connect4GameState) message);
					}
				});
			}
		}

		/**
		 * If a shutdown message is received from the Hub, the user is notified and the
		 * program ends.
		 */
		protected void serverShutdown(String message) 
		{
			SwingUtilities.invokeLater(new Runnable() 
			{
				public void run() 
				{
					JOptionPane.showMessageDialog(Connect4Window.this,
							"Your opponent has disconnected.\nThe game is ended.");
					System.exit(0);
				}
			});
		}

	}

	/**
	 * Creates and configures the window, opens a connection to the server, and
	 * makes the widow visible on the screen. This constructor can block until the
	 * connection is established.
	 * 
	 * @param hostName
	 *            the name or IP address of the host where the server is running.
	 * @param serverPortNumber
	 *            the port number on the server computer when the Hub is listening
	 *            for connections.
	 * @throws IOException
	 *             if some I/O error occurs while trying to open the connection.
	 * @throws Client.DuplicatePlayerNameException
	 *             it playerName is already in use by another player in the game.
	 */
	public Connect4Window(String hostName, int serverPortNumber, boolean isShow) throws IOException {

		super("Net Connect4");
		gridLength = 7;
		game = new Connect4Game(isShow, "Player1", "Player2", Color.BLUE, Color.RED, this, gridLength);
		connection = new Connect4Client(hostName, serverPortNumber);
		myID = connection.getID();
		gameOver = false;
		clickedCount=0;
		connectN=4;
		setLocation(200, 100);
		setVisible(false);
	}

	/**
	 * This method is called when the user clicks the Connect4 board. If the click
	 * represents a legal move at a legal time, then a message is sent to the Hub to
	 * inform it of the move. The Hub will change the game state and send the new
	 * state to both players. It is very important that the game clients do not
	 * change the game state directly, since the "official" game state is maintained
	 * by the Hub. Doing things this way guarantees that both players see the same
	 * board.
	 */

	/**
	 * This method is called when a new game state is received from the hub. It
	 * stores the new state in the instance variable that represents the game state
	 * and updates the user interface to reflect the state. Note that this method is
	 * called on the GUI event thread (using SwingUtilitites.invokeLater()) to avoid
	 * synchronization problems. (Synchronization is an issue when a method that
	 * manipulates the GUI is called from a thread other than the GUI event thread.
	 * In this problem, there is also the problem that a message can actually be
	 * received before the constructor has completed, which would lead to errors in
	 * this method from uninitialized variables, if SwingUtilities.invokeLater()
	 * were not used.)
	 */
	private void newState(Connect4GameState state) 
	{
		if (state.playerDisconnected) 
		{
			JOptionPane.showMessageDialog(this, "Your opponent has disconnected.\nThe game is ended.");
			System.exit(0);
		}
		this.state = state;
		
		if ((state.gameInProgress) && (clickedCount!=0)) //check if game is running
		{
			game.gamePlaying = true;
			gameOver = false;
			
			if (state.currentPlayer == 1) 
			{
				
				game.players[0].move = new Location(state.addrow, state.addcol);
				game.players[0].updateGrid();
			} 
			
			else 
			{
				game.players[1].move = new Location(state.addrow, state.addcol);
				game.players[1].updateGrid();
				
			}
			
			game.receiveCount++;
			gameOver = game.judge(); //judges who won/lost
		} 
		clickedCount++;
		
		if (!gameOver) 
		{
			if (myID == state.currentPlayer) 
			{
				game.getConnect4World().setMessage("Waiting for opponent's move");
			} 
			
			else 
			{
				if (myID == 1)
					game.getConnect4World().setMessage("Your move (Blue)");
				else
					game.getConnect4World().setMessage("Your move (Red)");
			}
		}
		else clickedCount=0;

	}

	public Connect4Game getGame() 
	{
		return game;
	}

	public int getID() 
	{
		return myID;
	}

	public boolean getgameOverFlag() 
	{
		return gameOver;
	}

	public void setgameOverFlag(boolean flag) 
	{
		gameOver = flag;
	}
}