package netgame.connect4;
import java.awt.Color;

/**
 * Piece.java
 * 
 * A <CODE>Piece</CODE> object represents a piece in the Connect4 game.
 * 
 *  @author  
 *  @version 
 *  @author  Period: 
 *  @author  Assignment:
 * 
 *  @author  Sources: We received help from Mr. Peck!!
 */
public class Piece
{
	/** The color of the piece */
	private Color c;

	/**
	 * Constructs a piece.
	 * @param color the color of the piece
	 */
	public Piece(Color color)
	{
		c = color;
	}

	/**
	 * Gets the color of the piece.
	 * @return the color of the piece
	 */
	public Color getColor()
	{
		return c;
	}

	/**
	 * Sets the color of the piece.
	 * @param newColor the new color for the piece
	 */
	public void setColor(Color newColor)
	{
		c = newColor;
	}
}
