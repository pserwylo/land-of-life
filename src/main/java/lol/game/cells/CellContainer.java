package lol.game.cells;


import lol.game.grid.Board;

/**
 * A grid is full of cell containers.<br/>
 * Each container holds a form of <b>FloorCell</b>.
 * @author god
 *
 */
public class CellContainer
{
	
	private int x, y;
	private FloorCell cell;
	private CellContainer[] neighbours;
	private Board owner;
	
	public CellContainer(Board owner, int x, int y)
	{
		this.owner = owner;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * The "first" neighbour is the one at the top left on the board,
	 * and we work our way around clockwise from there...
	 */
	public void calculateNeighbours()
	{
		neighbours = new CellContainer[8];
		this.neighbours[0] = this.owner.getContainerAt(this.x-1, this.y-1);
		this.neighbours[1] = this.owner.getContainerAt(this.x, this.y-1);
		this.neighbours[2] = this.owner.getContainerAt(this.x+1, this.y-1);
		this.neighbours[3] = this.owner.getContainerAt(this.x+1, this.y);
		this.neighbours[4] = this.owner.getContainerAt(this.x+1, this.y+1);
		this.neighbours[5] = this.owner.getContainerAt(this.x, this.y+1);
		this.neighbours[6] = this.owner.getContainerAt(this.x-1, this.y+1);
		this.neighbours[7] = this.owner.getContainerAt(this.x-1, this.y);
	}
	
	/**
	 * The "first" neighbour is the one at the top left on the board,
	 * and we work our way around clockwise from there...
	 */
	public int searchNeighbours(int size, int typeSearch)
	{
		switch(size)
		{
		case 1:
			if(this.neighbours[1].cell.type == typeSearch) return 1;
			if(this.neighbours[3].cell.type == typeSearch) return 3;
			if(this.neighbours[5].cell.type == typeSearch) return 5;
			if(this.neighbours[7].cell.type == typeSearch) return 7;
			return -1;
		case 2:
			if(this.neighbours[1].cell.type == typeSearch) return 1;
			if(this.neighbours[3].cell.type == typeSearch) return 3;
			if(this.neighbours[5].cell.type == typeSearch) return 5;
			if(this.neighbours[7].cell.type == typeSearch) return 7;
			if(this.neighbours[0].cell.type == typeSearch) return 1;
			if(this.neighbours[2].cell.type == typeSearch) return 3;
			if(this.neighbours[4].cell.type == typeSearch) return 5;
			if(this.neighbours[6].cell.type == typeSearch) return 7;
			return -1;
		case 3:
			if(this.neighbours[1].cell.type == typeSearch) return 1;
			if(this.neighbours[3].cell.type == typeSearch) return 3;
			if(this.neighbours[5].cell.type == typeSearch) return 5;
			if(this.neighbours[7].cell.type == typeSearch) return 7;
			if(this.neighbours[0].cell.type == typeSearch) return 1;
			if(this.neighbours[2].cell.type == typeSearch) return 3;
			if(this.neighbours[4].cell.type == typeSearch) return 5;
			if(this.neighbours[6].cell.type == typeSearch) return 7;
			if(this.neighbours[1].neighbours[0].cell.type == typeSearch) return 1;
			if(this.neighbours[1].neighbours[1].cell.type == typeSearch) return 1;
			if(this.neighbours[1].neighbours[2].cell.type == typeSearch) return 1;
			if(this.neighbours[3].neighbours[2].cell.type == typeSearch) return 3;
			if(this.neighbours[3].neighbours[3].cell.type == typeSearch) return 3;
			if(this.neighbours[3].neighbours[4].cell.type == typeSearch) return 3;
			if(this.neighbours[5].neighbours[4].cell.type == typeSearch) return 5;
			if(this.neighbours[5].neighbours[5].cell.type == typeSearch) return 5;
			if(this.neighbours[5].neighbours[6].cell.type == typeSearch) return 5;
			if(this.neighbours[7].neighbours[6].cell.type == typeSearch) return 7;
			if(this.neighbours[7].neighbours[7].cell.type == typeSearch) return 7;
			if(this.neighbours[7].neighbours[0].cell.type == typeSearch) return 7;
			return -1;
		default:
			return -1;
		}
	}
	
	public int reverseDirection(int originalDirection) // for when you need the opposite side
	{
		switch(originalDirection)
		{
		case 0: return 4;
		case 1: return 5;
		case 2: return 6;
		case 3: return 7;
		case 4: return 0;
		case 5: return 1;
		case 6: return 2;
		case 7: return 3;
		default: return -1;
		}
	}
	
	/**
	 * Puts a different cell in this container...
	 * @param cell
	 */
	public void setCell(FloorCell cell)
	{
		this.cell = cell;
		cell.setContainer(this);
	}
	
	public FloorCell getCell()
	{
		return this.cell;
	}
	
	public CellContainer getNeighbour(int direction)
	{
		return this.neighbours[direction];
	}
	
	public CellContainer[] getNeighbours()
	{
		return this.neighbours;
	}
}
