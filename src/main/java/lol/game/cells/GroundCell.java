package lol.game.cells;

public class GroundCell extends FloorCell
{

	/*
	 * When the moisture rises, it is transformed into a low level 
	 * form of plant life...
	 */
	private static final float MOISTURE_EVAPORATION_RATE = 0.1f;
	
	public final static String TYPE_STRING = "ground";
	
	/**
	 * The amount of moisture needed to turn into a plant cell...
	 */
	private static final float UPGRADE_LEVEL = 0.1f;

	
	public GroundCell(CellContainer owner)
	{
		super(owner, GroundCell.TYPE_STRING);
		this.type = FloorCell.TYPE_GROUND;
	}

	@Override
	public float moistureReturnedToCloud()
	{
		return this.moisture / MOISTURE_EVAPORATION_RATE;
	}

	/**
	 * When receiving rain, the cell decides if it has enough to harbour
	 * a plant cell or not...
	 */
	@Override
	public void recieveRain(float amount)
	{
		this.moisture += amount;
		if (this.moisture > GroundCell.UPGRADE_LEVEL)
		{
			this.changeType = FloorCell.CHANGE_PLANT_PLUS_LEVEL;
		}
		if(this.owner.searchNeighbours(2, FloorCell.TYPE_MOUNTAIN)!=-1)
		{
			int riverSourceLevel = RiverCell.checkRiverLimit(this.moisture);
			if(riverSourceLevel > 0 && riverSourceLevel >= this.level)
			{
				this.changeType = FloorCell.CHANGE_RIVER_LEVEL_SOURCE + riverSourceLevel;
				return;
			}
		}
	}

	@Override
	public boolean performAction()
	{
		this.owner.setCell(new MountainCell(this.owner));
		return true;
	}

	@Override
	public void checkUpdate()
	{
		if(changeType == FloorCell.CHANGE_NO_CHANGE)
		{
			if(this.owner.searchNeighbours(3, FloorCell.TYPE_RIVER)!=-1)
			{
				this.changeType = FloorCell.CHANGE_PLANT_PLUS_LEVEL;
			}
		}
	}

	@Override
	public boolean decreaseLevel()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean increaseLevel()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
