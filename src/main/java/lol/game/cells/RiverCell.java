package lol.game.cells;

import java.awt.Graphics2D;

public class RiverCell extends FloorCell
{

	public final static String TYPE_STRING = "river";
	
	private static float[] MOISTURE_FOR_LEVELS = 
	{0.5f, 0.6f, 0.7f, 0.9f, 1f};
	
	private boolean source;
	private int direction;
	private FloorCell history; 
	public CellContainer sourceOwner;
	public RiverCell(CellContainer owner)
	{
		super(owner, RiverCell.TYPE_STRING);
	}
	
	public RiverCell(CellContainer owner, boolean isSource, int setLevel, FloorCell previousCell)
	{
		super(owner, RiverCell.TYPE_STRING);
		level = setLevel;
		if(level >=5)level = 4;
		source = isSource;
		direction = 1;
		history = previousCell;
		changeType = 0;
		this.type = FloorCell.TYPE_RIVER;
		if(source == true)checkFlow(this.owner,this.owner, 0);
	}
	public RiverCell(CellContainer owner, boolean isSource, int setLevel, FloorCell previousCell, int setDirection)
	{
		super(owner, RiverCell.TYPE_STRING);
		level = setLevel;
		if(level >=5)level = 4;
		source = isSource;
		direction = setDirection;
		history = previousCell;
		changeType = 0;
		this.type = FloorCell.TYPE_RIVER;
		sourceOwner = owner;
		if(source == true)checkFlow(this.owner,this.owner, 0);
	}
	public RiverCell(CellContainer owner, boolean isSource, int setLevel, FloorCell previousCell, int setDirection, CellContainer riverSource)
	{
		super(owner, RiverCell.TYPE_STRING);
		level = setLevel;
		if(level >=5)level = 4;
		source = isSource;
		direction = setDirection;
		history = previousCell;
		changeType = 0;
		sourceOwner = riverSource;
		this.type = FloorCell.TYPE_RIVER;
		if(source == true)checkFlow(this.owner,this.owner, 0);
	}
	
	public int getDirection()
	{
		return direction;
	}
	/**
	 * Rivers have plenty of water, and the bigger they are, the more water
	 * they have to offer.</br>
	 * Hence, the amount of water returned to the clouds above it is lots,
	 * and is proportional to the rivers level at this cell.
	 * 
	 * @return Value between 0 and 0.5
	 */
	@Override
	public float moistureReturnedToCloud()
	{
		return (float)this.level/10;
	}

	@Override
	public boolean increaseLevel()
	{
		// TODO Auto-generated method stub
		return false;
	}
	public boolean decreaseLevel()
	{
		return false;
	}

	@Override
	public void recieveRain(float amount)
	{
		// TODO Auto-generated method stub
		this.moisture += amount * 0.15;
		int riverSourceLevel = RiverCell.checkRiverLimit(this.moisture);
		System.out.println("Moisture: " + this.moisture + ", riverSourceLevel = " +riverSourceLevel);
		if(riverSourceLevel > 0 && riverSourceLevel >= this.level)
		{
			this.changeType = FloorCell.CHANGE_RIVER_LEVEL_SOURCE + riverSourceLevel;
			return;
		}
		
	}
	
	// recursive function to complete river flow. 
	// Will break if there's a 2 cell width border.
	private boolean checkFlow(CellContainer followCell, CellContainer initialCell, int breakout)
	{
		breakout++;
		if (followCell.searchNeighbours(1, 5)!= -1) return false;
		if (breakout > 40) return false;
		CellContainer targetCell;
		if (followCell.searchNeighbours(1, 3)!= -1 && direction != this.owner.reverseDirection(followCell.searchNeighbours(1, 3)))
		{
			direction = followCell.searchNeighbours(1, 3);
			targetCell = followCell.getNeighbour(direction);
			RiverCell tempCell = (RiverCell)targetCell.getCell();
			if(tempCell.sourceOwner != initialCell)
			{
				if(targetCell.getCell().level<this.level)
				{
					// reset code here
					resetCell(targetCell);
					targetCell.setCell(new RiverCell(targetCell,false,level,targetCell.getCell(),direction,this.owner));
				}
				else 
				{
					targetCell.setCell(new RiverCell(targetCell,false,level+1,targetCell.getCell(),direction,this.owner));
				}
				return checkFlow(targetCell, initialCell,breakout);
			}
			return false;
		}
		if (followCell.searchNeighbours(3, 2)!= -1)
		{
			direction = this.owner.reverseDirection(followCell.searchNeighbours(3, 2));
			targetCell = followCell.getNeighbour(direction);
			targetCell.setCell(new RiverCell(targetCell,false,level,targetCell.getCell(),direction,this.owner));
			return checkFlow(targetCell, initialCell,breakout);
		}
		if (followCell.searchNeighbours(3, 5)!= -1)
		{
			direction = followCell.searchNeighbours(3, 5);
			targetCell = followCell.getNeighbour(direction);
			targetCell.setCell(new RiverCell(targetCell,false,level,targetCell.getCell(),direction,this.owner));
			return checkFlow(targetCell, initialCell,breakout);
		}
		if (followCell.searchNeighbours(3, 1)!= -1)
		{
			direction = followCell.searchNeighbours(3, 1);
			targetCell = followCell.getNeighbour(direction);
			targetCell.setCell(new RiverCell(targetCell,false,level,targetCell.getCell(),direction,this.owner));
			return checkFlow(targetCell, initialCell,breakout);
		}
		targetCell = followCell.getNeighbour(direction);
		return checkFlow(targetCell, initialCell,breakout);
	}
	public void resetCell(CellContainer followCell)
	{
		RiverCell tempCell = (RiverCell)followCell.getCell();
		followCell.setCell(tempCell.history);
		if (followCell.getNeighbour(tempCell.getDirection()).getCell().type == 3)
		{
			resetCell(followCell.getNeighbour(tempCell.getDirection()));
		}
	}
	
	public void checkUpdate()
	{
		if(changeType==0)
		{
			if(source == true) changeType = 4;
			else this.owner.setCell(new GroundCell(this.owner));
		}
	}

	@Override
	public void render(Graphics2D g)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean performAction() {
		// TODO Auto-generated method stub
		return false;
	}
	public static int checkRiverLimit(float moistureCheck)
	{
		if(moistureCheck >= MOISTURE_FOR_LEVELS[4]) return 5;
		if(moistureCheck >= MOISTURE_FOR_LEVELS[3]) return 4;
		if(moistureCheck >= MOISTURE_FOR_LEVELS[2]) return 3;
		if(moistureCheck >= MOISTURE_FOR_LEVELS[1]) return 2;
		if(moistureCheck >= MOISTURE_FOR_LEVELS[0]) return 1;
		return 0;
	}
	

}
