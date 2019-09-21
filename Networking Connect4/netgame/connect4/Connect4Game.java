package netgame.connect4;

import java.awt.Color;

import grid.Grid;
import grid.Location;

/**
 * Connect4Game.java
 * 
 * An <CODE>Connect4Game</CODE> object represents an Connect4 game.
 * 
 * @author 
 * @version 
 * @author Period:
 * @author Assignment:
 * 
 * @author Sources: 
 */
public class Connect4Game 
{
	public boolean gamePlaying;
	public Connect4Window myWindow;
	public int receiveCount;
	public Connect4World world;

	/** The array of two players (human, computer) */
	public Player[] players;

	/** The index into players for the next player to play */
	public int playerIndex;

	private String nameA;
	private String nameB;
	public int gridLength;
	public int connectN;
	public int gameMode;

	/**
	 * Constructs an Connect4 game.<br>
	 * Postcondition: <CODE>players.length == 2</CODE>; <CODE>players[0]</CODE>
	 * contains a human Connect4 player; <CODE>players[1]</CODE> contains a computer
	 * Connect4 player; The world has been shown.
	 * 
	 * @param show
	 *            if true world is displayed. Used for testing
	 */
	public Connect4Game(boolean show, String name1, String name2, Color color1, Color color2, Connect4Window win,
			int gridLengthN) {
		gamePlaying = false;
		gridLength = gridLengthN;
		receiveCount = 2;
		nameA = name1;
		nameB = name2;
		world = new Connect4World(this, gridLengthN);
		this.myWindow = win;
		connectN=win.connectN;
		//System.out.print("C=" +connectN);
		players = new Player[2];
		players[0] = new Player(world, name1, color1, gridLengthN);
		players[1] = new Player(world, name2, color2, gridLengthN);
		playerIndex = 0;

		if (show) 
		{
			world.show();
		}
	}

	/**
	 * Plays the game until it is over (no player can play).
	 */

	public void playGame() 
	{
		int row;
		int col;
		boolean gameOn = true;
		while ((gameOn == true) && (players[0].canPlay() || players[1].canPlay())) {
			if (receiveCount >= 2) 
			{
				gamePlaying = true;
				if (myWindow.myID == 1) 
				{
					if (!players[0].canPlay())
						gameOn = false;
					else 
					{
						players[0].play(); //gets click position
						row = players[0].move.getRow();
						col = players[0].move.getCol();
						//System.out.println("ID1 move");
						myWindow.connection.send(new int[] { row, col }); //sends location to hub
						receiveCount = 0;
					}
				} 
				else if (myWindow.myID == 2) 
				{
					if (!players[1].canPlay())
						gameOn = false;
					else 
					{
						players[1].play();
						row = players[1].move.getRow();
						col = players[1].move.getCol();
						//System.out.println("ID2 move");
						myWindow.connection.send(new int[] { row, col });
						receiveCount = 0;
					}
				}
			} 
			else
				gamePlaying = false;
			if (myWindow.gameOver)
				break;
		}
	}

	public boolean check_direction(int[][] grid, int v, int i, int j, int dx, int dy,int connectN) {
		
		int sameCnt=0;
		
		for(int s=1; s<connectN; s++) {
			if (((i+s*dx)>=0 && (i+s*dx)<=(gridLength-1) && (j+s*dy)>=0 && (j+s*dy)<=(gridLength-1)))
				if (v==grid[i+s*dx][j+s*dy]) {
					sameCnt++;
				}
		}
		return (sameCnt==(connectN-1));
	}
	public int connected() {
		System.out.println("enter testing");
        int grid[][]= new int[gridLength][gridLength];
		for (int i = 0; i < gridLength; i++) {
			for (int j = 0; j < gridLength; j++) {
				Location l = new Location(i, j);
				if (world.getGrid().get(l) !=null) {
					if (world.getGrid().get(l).getColor().equals(Color.RED)) grid[i][j]=1;
					else if (world.getGrid().get(l).getColor().equals(Color.BLUE)) grid[i][j]=2;
					else grid[i][j]=0;
				}
				else  grid[i][j]=0;
			}
		}
		int result = 0;
		for (int i = 0; i < gridLength; i++) {
			//for (int j = 0; j < gridLength; j++) {
			for (int j = gridLength-1; j >=0; j--) {
				int v = grid[i][j];
				if (v == 0)
					continue;
				else {
					if (check_direction(grid,v,i,j,1,0,connectN)) result = v;
				    if (check_direction(grid,v,i,j,0,-1,connectN)) result = v;
					if (check_direction(grid,v,i,j,1,-1,connectN)) result = v;
					if (check_direction(grid,v,i,j,-1,-1,connectN)) result = v;
					/*
					//horizontal check
					if (i + 3 < gridLength && grid[i + 1][j] == v && grid[i + 2][j] == v && grid[i + 3][j] == v) {
						result = v;
					}
					//vertical check
					if (j >= 3 && grid[i][j - 1] == v && grid[i][j - 2] == v && grid[i][j - 3] == v) {
						result = v;
					}

					if (i + 3 < gridLength && (j >=3) && grid[i + 1][j - 1] == v && grid[i + 2][j - 2] == v
							&& grid[i + 3][j - 3] == v) {
						result = v;
					}
					if (i - 3 >= 0 && j >=3) {
						if (grid[i - 1][j - 1] == v && grid[i - 2][j - 2] == v && grid[i - 3][j - 3] == v) {
							result = v;
						}

					}
					*/
				}
			}
		}
		
		if (result==0 && !(players[0].canPlay() || players[1].canPlay())) result=3;
		return result;
	}
	
	public boolean judge() 
	{
		boolean gameOver = true;
		int result = 0;

		result = connected();
		if (result ==1) 
		{
			if (myWindow.myID == 2)
				world.setMessage("You win");
			else if (myWindow.myID == 1)
				world.setMessage("You lose");
		} 
		else if (result ==2) 
		{
			if (myWindow.myID == 2)
				world.setMessage("You lose");
			else if (myWindow.myID == 1)
				world.setMessage("You win");
		}
		else if (result ==3) 
		{
		    world.setMessage("Tie!");
			
		}
		else if (result == 0) 
		{
			gameOver= false;
		}

		return gameOver;
	}


	/**
	 * Creates a string with the current game state. (used for the GUI message).
	 */
	public String toString() 
	{
		int numRed = 0;
		int numBlue = 0;

		Grid<Piece> board = world.getGrid();

		for (Location loc : board.getOccupiedLocations())
			if (board.get(loc).getColor().equals(Color.RED))
				numRed++;
			else
				numBlue++;

		String result = "Red: " + numRed + "    Blue: " + numBlue + "\n";
		if (!players[0].canPlay() && !players[1].canPlay())
			if (numRed > numBlue)
				result += nameA + " won!";
			else if (numRed < numBlue)
				result += nameB + " won!";
			else
				result += "It's a tie!";
		else
			result += players[playerIndex].getName() + " to play.";

		return result;
	}

	// accessors used primarily for testing

	protected Connect4World getConnect4World() 
	{
		return world;
	}

	protected Player[] getPlayer() 
	{
		return players;
	}

	protected int getPlayerIndex() 
	{
		return playerIndex;
	}
}
