package lol.game.cells;

public class MountainCell extends FloorCell
{

	public final static int TYPE = 2;
	public final static String TYPE_STRING = "mountain";
	
	public MountainCell(CellContainer owner)
	{
		super(owner, MountainCell.TYPE_STRING);
		this.type = FloorCell.TYPE_MOUNTAIN;
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
		return (float)this.level/20;
	}

	@Override
	public void recieveRain(float amount)
	{
		CellContainer[] neighbours = this.owner.getNeighbours();
		for (int i=0; i<neighbours.length; i++)
		{
			if (amount > 0.1)
			{
				neighbours[i].getCell().recieveRain(amount/5);
			}
		}
	}

	/**
	 * A mountain can increase its level if it has (level-1) mountains of
	 * (level-1) level around it.
	 */
	@Override
	public boolean performAction()
	{
		if (this.level < FloorCell.MAX_LEVEL) 
		{
			CellContainer[] neighbours = this.owner.getNeighbours();
			//Inspect neightbours...
			int correctLevels = 0;
			for (int i=0; i<neighbours.length; i++)
			{
				if (neighbours[i].getCell() instanceof MountainCell)
				{
					if (((MountainCell)neighbours[i].getCell()).getLevel() == this.level)
					{
						correctLevels ++;
					}
				}
			}
	
			if (correctLevels >= this.level)
			{
				this.level ++;
				this.setRenderer(TileManager.getRenderer(MountainCell.TYPE_STRING, this.level));;
				return true;
			}
		}	
		return false;
	}

	@Override
	public void checkUpdate()
	{
		// TODO Auto-generated method stub
		
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
		return false;
	}
}
