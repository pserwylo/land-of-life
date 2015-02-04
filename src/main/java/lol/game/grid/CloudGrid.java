package lol.game.grid;

import java.awt.Graphics2D;
import lol.game.cells.Cell;
import lol.game.cells.CloudCell;
import lol.clouds.*;

public class CloudGrid
{

	private CloudCell[][] cellGrid;
	private int width;
	private int height;
	
	public CloudGrid(int width, int height)
	{
		this.width = width;
		this.height = height;
		this.cellGrid = new CloudCell[height][width];
		this.seed();
	}
	
	public void render(Graphics2D g)
	{
		//Prevent doing these calculations many many times... 
		//int cos30TimesSize = (int)(Math.cos(30 * Math.PI/180) * Cell.PIXEL_SIZE);
		//int sin30TimesSize = (int)(Math.sin(30 * Math.PI/180) * Cell.PIXEL_SIZE);
		
		int originX = 0;
		int originY = 0;
		
		int dx = Cell.ISOMETRIC_PIXEL_WIDTH * this.width/2;
		int dy = 0;
		g.translate(dx, 10);
		
		for (int y=0; y<this.height; y++)
		{		
			g.translate(-originX, -originY);

			dx = Cell.ISOMETRIC_PIXEL_WIDTH * y;
			dy = Cell.ISOMETRIC_PIXEL_HEIGHT * y;
			
			originX = -dx;
			originY = dy;
			
			g.translate(-dx, dy);
			
			for (int x=0; x<this.width/2; x++)
			{
				// this.grid[y][x].getCell().render(g);
				
				//
				// selectLoc = the mouse-over grid location
				//
				this.cellGrid[y][x].render(g);

				dx = Cell.ISOMETRIC_PIXEL_WIDTH;
				dy = Cell.ISOMETRIC_PIXEL_HEIGHT;
				originX += dx;
				originY += dy;
				
				g.translate(dx, dy);
			}
		}
	}
	
	/**
	 * Clouds need to be seeded each turn.<br/>
	 * seed() generates a new set of clouds to use from a random noise map...
	 */
	public void seed()
	{
		Clouds clouds = new Clouds(this.width, this.height);
		for (int y = 0; y < this.height; y ++)
		{
			for (int x = 0; x < this.width; x ++)
			{
				if (x == this.width - 1)
				{
					this.cellGrid[y][x] = new CloudCell(0);
				}
				else
				{
					this.cellGrid[y][x] = new CloudCell(clouds.getCloudCellAt(x, y));
				}
			}	
		}
	}
	
	public CloudCell getCellAt(int x, int y)
	{
		while (x < 0) x += this.width;
		while (y < 0) y += this.height;
		return this.cellGrid[y][x];
	}
	
	/**
	 * Shifts all of the cells across by 1.
	 */
	public void advanceCells()
	{
		for (int x = 0; x < this.width - 1; x++)
		{
			for (int y = 0; y < this.height; y++)
			{
				this.cellGrid[y][x] = this.cellGrid[y][x + 1];
			}
		}
	}
	
	public int getWidth(){return width;}
	public int getHeight(){return this.height;}
}
