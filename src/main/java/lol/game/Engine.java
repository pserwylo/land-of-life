package lol.game;

import java.awt.Graphics2D;

import lol.game.cells.Cell;
import lol.game.cells.CloudCell;
import lol.game.cells.FloorCell;
import lol.game.cells.TileManager;
import lol.game.grid.*;

public class Engine
{
	Board floorCells;
	CloudGrid cloudCells;
	int selectedGrid;
	int inputX;
	int inputY;
	int boardSize;
	int originX;
	int originY;
	float XtoYRatio;
	
	/**
	 * Each time the simulation runs, it does a certain number of iterations.
	 * When they are complete, the engine needs to be able to find out if
	 * it should stop the timer which runs the simulation...
	 */
	private int iterationsLeft = 0;
	
	/**
	 * Determins if the simulation is running or not.
	 */
	private boolean running = false;
	
	public Engine(int width, int height)
	{
		this.init(width, height);
		inputX = 0;
		inputY = 0;
    	
    	boardSize = floorCells.getWidth();
       	
    	originX = (int) (Cell.ISOMETRIC_PIXEL_WIDTH * (boardSize+1));
    	originY = 10;
    	XtoYRatio = (float)(1/Math.tan(30 * Math.PI/180));
    	
	}
	
	public Engine()
	{
		this.init(20, 20);
	}
	
	private void init(int width, int height)
	{
		TileManager.initTiles();
		
		this.floorCells = new Board(width, height);
		this.cloudCells = new CloudGrid(width * 2, height);
		this.selectedGrid = -1;
	}
	
	/**
	 * Go forth and figure out what to do with each cloud cell at this point in
	 * time, then move the clouds accross...
	 *
	 */
	public void advanceGame()
	{
		if ( ! this.running )
		{
			this.running = true;
			this.cloudCells = 
				new CloudGrid(
						this.floorCells.getWidth() * 2,
						this.floorCells.getHeight());
			this.iterationsLeft = this.floorCells.getWidth();
		}
		CloudCell currentCloud = null;
		FloorCell currentFloor = null;
		
		for (int y=0; y<this.floorCells.getHeight(); y++)
		{
			for (int x=0; x<this.floorCells.getWidth(); x++)
			{
				currentCloud = this.cloudCells.getCellAt(x, y);
				currentFloor = this.floorCells.getCellAt(x, y);

				//If the cloud is now raining, pass that rain on to the cell
				//below...
				if (currentCloud.isRaining())
				{
					currentCloud.rainOnCell(currentFloor);
				}
				
				//pass each floor cell to its corresponding cloud cell...
				currentCloud.passOverCell(currentFloor);
			}
		}
		
		for (int y=0; y<this.floorCells.getHeight(); y++)
		{
			for (int x=0; x<this.floorCells.getWidth(); x++)
			{
				currentFloor = this.floorCells.getCellAt(x, y);
				currentFloor.checkUpdate();
			}
		}
		
		for (int y=0; y<this.floorCells.getHeight(); y++)
		{
			for (int x=0; x<this.floorCells.getWidth(); x++)
			{
				currentFloor = this.floorCells.getCellAt(x, y);
				currentFloor.updateCell();
			}
		}
		
		this.cloudCells.advanceCells();
		
		this.iterationsLeft --;
		if (this.iterationsLeft <= 0)
		{
			this.running = false;
		}
	}
	
	/**
	 * Advance the game an arbitrary amount of steps...
	 * @param steps Times to advance...
	 */
	public void advanceGame(int steps)
	{
		for (int i=0; i<steps; i++)
		{
			this.advanceGame();
		}
	}
	
	/**
	 * Asks if the simulation is still running...
	 * @return True if running
	 */
	public boolean isRunning()
	{
		return this.running;
	}
	
	/**
	 * Converts screen coordinates to grid coordinates and returns the cell
	 * at that location.
	 * @param mouseX Screen x ordinate
	 * @param mouseY Screen x ordinate
	 * @return The cell found at the given screen 
	 * 			coordinates <b>(mouseX, mouseY)</b>
	 */
	private FloorCell getCellAt(int mouseX, int mouseY)
	{
    	int selectedIndex = this.getSelectedIndex(mouseX, mouseY);
    	return this.floorCells.getCellAt(selectedIndex);
	}
	
	/**
	 * When the mouse is pressed, find out which cell it clicked on
	 * and act accordingly...
	 * @param x Screen x ordinate
	 * @param y Screen y ordinate
	 */
	public void onMousePressed(int x, int y)
	{
		if ( ! this.isRunning() )
		{
			FloorCell clickedCell = this.getCellAt(x, y);
			if (clickedCell != null)
			{
				clickedCell.performAction();
			}
		}
	}
	
	/**
	 * Converts screen coordinates to isometric grid coordinates.
	 * @param mouseX Mouse x ordinate
	 * @param mouseY Mouse y ordinate
	 * @return Selected index in board array of cells...
	 */
	private int getSelectedIndex(int mouseX, int mouseY)
	{
   	    int selectedIndex = -1;
   	    
    	float checkX = (mouseX-originX)/XtoYRatio-1 ; // distance from origin, non-isometric
    	float checkY =  mouseY-originY-1;   // distance from origin

    	for(int y=0; y<boardSize; y++)
    	{
    		for(int x=0; x<=y; x++)
    		{
    			// (-Cell.ISOMETRIC_CELL_WIDTH/XtoYRatio/2*y+Cell.ISOMETRIC_CELL_WIDTH/XtoYRatio*x) is the x point to check
    			// (Cell.ISOMETRIC_PIXEL_HEIGHT/2*y) is the y point to check
    			// the next part basically checks if the input is within a certain distance from the center point of a cell
    			if((Math.abs(checkX-(-Cell.ISOMETRIC_PIXEL_WIDTH*2/XtoYRatio/2*y+Cell.ISOMETRIC_PIXEL_WIDTH*2/XtoYRatio*x))+Math.abs(checkY-Cell.ISOMETRIC_PIXEL_HEIGHT*2/2*(y+1)))<(Cell.ISOMETRIC_PIXEL_HEIGHT*2/2))
    			{
    				selectedIndex = x+(y-x)*boardSize;// counting from top to bottom row by row
    			}
    		}
    	}
    	for(int y=boardSize; y<(boardSize*2-1); y++) //repeat, 2nd half
    	{
    		for(int x=y-boardSize+1; x<boardSize; x++)
    		{
    			if((Math.abs(checkX-(-Cell.ISOMETRIC_PIXEL_WIDTH*2/XtoYRatio/2*y+Cell.ISOMETRIC_PIXEL_WIDTH*2/XtoYRatio*x))+Math.abs(checkY-Cell.ISOMETRIC_PIXEL_HEIGHT*2/2*(y+1)))<(Cell.ISOMETRIC_PIXEL_HEIGHT*2/2))
    			{
    				selectedIndex = x+(y-x)*boardSize;// counting from top to bottom row by row
    			}
    		}
    	}
    	return selectedIndex;
	}
	
	/**
	 * Handler for mouse moves...
	 * @param _inputX
	 * @param _inputY
	 */
	public void getInput(int _inputX, int _inputY)
	{
		inputX = _inputX;
		inputY = _inputY;
    	this.selectedGrid = this.getSelectedIndex(_inputX, _inputY); // this value represents grid value which the mouse is over.
    
	}
	
	public int getLoc()
	{
		return this.selectedGrid;
	}
	
	/**
	 * Ask board to render all cells...
	 * @param g
	 */
	public void render(Graphics2D g)
	{
		this.floorCells.render(g, selectedGrid);
		this.cloudCells.render(g);
	}
}
