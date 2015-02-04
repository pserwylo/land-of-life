package lol.game.cells;

import java.awt.Graphics2D;

public abstract class Cell
{
	/**
	 * The length of a side of the isometric tiles used to represent the
	 * tiles in this game.
	 */
	public static final int PIXEL_SIZE = 26;
	
	/**
	 * The length of a tile edge (width) in an isometric projection.
	 */
	public static final int ISOMETRIC_PIXEL_WIDTH = 
		(int)(Math.cos(30/*degrees*/ * Math.PI/180) * Cell.PIXEL_SIZE);

	/**
	 * The length of a tile edge (height) in an isometric projection.
	 */
	public static final int ISOMETRIC_PIXEL_HEIGHT = 
		(int)(Math.sin(30/*degrees*/ * Math.PI/180) * Cell.PIXEL_SIZE);
	
	abstract void render(Graphics2D g);
}
