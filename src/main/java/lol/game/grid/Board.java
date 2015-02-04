package lol.game.grid;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

import lol.game.cells.Cell;
import lol.game.cells.CellContainer;
import lol.game.cells.FloorCell;
import lol.game.cells.GroundCell;
import lol.game.cells.OceanCell;

public class Board
{
	private CellContainer[][] grid;
	private int width;
	private int height;
	private int selectLoc;
	public Board(int width, int height)
	{
		this.init(width, height);
	}
	
	/**
	 * Fill the board with plain old GroundCell's
	 */
	private void init(int width, int height)
	{
		this.width = width; 
		this.height = height;
		
		this.grid = new CellContainer[width][height];
		for (int y=0; y<this.height; y++)
		{
			for (int x=0; x<this.width; x++)
			{
				CellContainer container = new CellContainer(this, x, y);
				
				FloorCell toAdd;
				if (x < 2 || x >= this.width - 2 ||
					y < 2 || y >= this.height- 2)
				{
					toAdd = new OceanCell(container);
				}
				else
				{
					toAdd = new GroundCell(container);
				}
					
				container.setCell(toAdd);
				this.grid[y][x] = container;
			}	
		}
		for (int y=0; y<this.height; y++)
		{
			for (int x=0; x<this.width; x++)
			{
				this.grid[y][x].calculateNeighbours();
			}	
		}
	}
	
	/**
	 * Ask each cell to render itself...
	 * @param g The context to render to...
	 */
	public void render(Graphics2D g, int newLoc)
	{
		selectLoc = newLoc;
		render(g);
	}
	
	public void render(Graphics2D g)
	{
		//Prevent doing these calculations many many times... 
		//int cos30TimesSize = (int)(Math.cos(30 * Math.PI/180) * Cell.PIXEL_SIZE);
		//int sin30TimesSize = (int)(Math.sin(30 * Math.PI/180) * Cell.PIXEL_SIZE);

		int totalTranslationX = 0;
		int totalTranslationY = 0;
		
		int originX = 0;
		int originY = 0;
		
		int dx = Cell.ISOMETRIC_PIXEL_WIDTH * this.width;
		int dy = 0;
		
		g.translate(dx, 10);
		totalTranslationX += dx;
		totalTranslationY += 10;
		
		for (int y=0; y<this.height; y++)
		{		

			dx = Cell.ISOMETRIC_PIXEL_WIDTH * y;
			dy = Cell.ISOMETRIC_PIXEL_HEIGHT * y;
			
			originX = -dx;
			originY = dy;
			
			g.translate(-dx, dy);
			totalTranslationX -= dx;
			totalTranslationY += dy;
			
			
			for (int x=0; x<this.width; x++)
			{
				// this.grid[y][x].getCell().render(g);
				
				//
				// selectLoc = the mouse-over grid location
				//
				this.grid[y][x].getCell().render(g);

				if((x+y*this.width)==selectLoc)
				{
					Image tile = Toolkit.getDefaultToolkit().getImage("images/selection.png");
					g.drawImage(tile, 0, 0, null);
				}

				dx = Cell.ISOMETRIC_PIXEL_WIDTH;
				dy = Cell.ISOMETRIC_PIXEL_HEIGHT;
				originX += dx;
				originY += dy;
				
				g.translate(dx, dy);
				totalTranslationX += dx;
				totalTranslationY += dy;
			}
			g.translate(-originX, -originY);
			totalTranslationX -= originX;
			totalTranslationY -= originY;
		}
		
		g.translate(-totalTranslationX, -totalTranslationY);
	}
	
	/**
	 * Return a cell at a specific location <b>(x, y)</b>
	 * @param x
	 * @param y
	 * @return The current cell occupying (x, y)
	 */
	public FloorCell getCellAt(int x, int y)
	{
		return this.grid[y][x].getCell();
	}
	
	/**
	 * Return a cell at a specific index <b>selectedIndex</b>, where selected
	 * index is the index to the grid as if it was a 1D Array.
	 * @param selectedIndex
	 * @return The cell at <b>selectedIndex</b>
	 */
	public FloorCell getCellAt(int selectedIndex)
	{
		if (selectedIndex > this.width * this.height || selectedIndex < 0)
		{
			return null;
		}
		else
		{
			int x = selectedIndex % this.width;
			int y = selectedIndex / this.width;
			return this.grid[y][x].getCell();
		}
	}
	
	
	public CellContainer getContainerAt(int x, int y)
	{
		while (x < 0) x += this.width;
		while (y < 0) y += this.height;
		if (x >= this.width) x %= this.width-1;
		if (y >= this.height) y %= this.height-1;
		
		return this.grid[y][x];
	}

	  //tickInteraction([variable]) // 1 round of interactions and changes
	public void advanceBoard(){}
	
	public int getWidth(){return this.width;}
	public int getHeight(){return this.height;}

}
