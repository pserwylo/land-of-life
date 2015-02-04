package lol.game.cells;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * Simply charged with the task of rendering a particular image representation
 * of a tile to the screen.
 * A pool of these will be available to the <b>TileManager</b> class, where
 * they will be publicly available.
 * 
 * @author god
 */
public class TileRenderer
{
	private String filename;
	private Image image;
	private int verticalOffset;
	
	public static final TileRenderer NULL_TILE = new TileRenderer("/images/null.png", 0);
	
	public TileRenderer(String filename, int verticalOffset)
	{
		this.filename = filename;
		this.image = Toolkit.getDefaultToolkit().createImage(this.filename);
		this.verticalOffset = verticalOffset;
	}
	
	/**
	 * Renders image to screen...
	 * @param g The graphics context to draw.
	 */
	public void render(Graphics2D g)
	{
		g.translate(0, -this.verticalOffset);
			g.drawImage(this.image, 0, 0, null);
		g.translate(0, this.verticalOffset);
	}
}
