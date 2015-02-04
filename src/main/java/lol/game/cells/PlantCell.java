package lol.game.cells;

import java.awt.Color;
import java.awt.Graphics2D;

public class PlantCell extends FloorCell
{

	private static float[] MOISTURE_FOR_LEVELS = 
		{0.3f, 0.35f, 0.4f, 0.5f, 0.7f};
	
	public final static String TYPE_STRING = "plant";

	public PlantCell(CellContainer owner)
	{
		super(owner, PlantCell.TYPE_STRING);
		this.type = FloorCell.TYPE_PLANT;
	}
	
	/**
	 * Plant cells also have water to offer the clouds as evaporation, 
	 * however the amount is not quite as great as with a river or other
	 * body of water.</br>
	 * 
	 * The amount of water returned to the skies is still proportional 
	 * to the level of the Plants on this cell.
	 * 
	 * @return Value between 0 and 0.25
	 */
	@Override
	public float moistureReturnedToCloud()
	{
		return (float)this.level/20;
	}

	@Override
	public void recieveRain(float amount)
	{
		this.moisture += amount * 0.1;
		System.out.println("Moisture: " + this.moisture);
		if (this.moisture > PlantCell.MOISTURE_FOR_LEVELS[this.level - 1] &&
			this.level < FloorCell.MAX_LEVEL)
		{
			this.changeType = FloorCell.CHANGE_PLANT_PLUS_LEVEL;
		}
		if(this.owner.searchNeighbours(2, FloorCell.TYPE_MOUNTAIN)!=-1)
		{
			int riverSourceLevel = RiverCell.checkRiverLimit(this.moisture);
			System.out.println("Moisture: " + this.moisture + ", riverSourceLevel = " +riverSourceLevel);
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
	public boolean decreaseLevel()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean increaseLevel()
	{
		if (this.level < FloorCell.MAX_LEVEL)
		{
			this.level ++;
			this.renderer = 
				TileManager.getRenderer(PlantCell.TYPE_STRING, this.level);
			return true;
		}
		return false;
	}
	
	public void checkUpdate()
	{
		
		if(changeType == FloorCell.CHANGE_NO_CHANGE)
		{
			
			
			this.moisture -= 0.05;
			
			if(this.owner.searchNeighbours(3, FloorCell.TYPE_RIVER)==-1)
			{
				//The plant will use up moisture through its regular processes...
				
				if (this.moisture < 0)
				{
					this.changeType = FloorCell.CHANGE_SET_GROUND;
				}
				else if (this.moisture > PlantCell.MOISTURE_FOR_LEVELS[this.level - 1])
				
					{
						this.changeType = FloorCell.CHANGE_PLANT_PLUS_LEVEL;
					}
					//Lower if the moisture has dropped too much...
					else if (this.moisture < PlantCell.MOISTURE_FOR_LEVELS[this.level - 1])
					{
						this.changeType = FloorCell.CHANGE_PLANT_MINUS_LEVEL;
					}
				
			}
			else 
			{
				if(this.level < 2 || this.moisture > PlantCell.MOISTURE_FOR_LEVELS[this.level - 2])
				{
					this.changeType = FloorCell.CHANGE_PLANT_PLUS_LEVEL;
				}
			}
			
		}
	}
	
	public void render(Graphics2D g)
	{
		System.out.println(
			this.level + " : " + this.moisture + " : " + 
			PlantCell.MOISTURE_FOR_LEVELS[this.level-1]);
		this.renderer.render(g);
		g.setColor(Color.WHITE);
		//g.drawString((int)(this.moisture*10) + "", 10, 10);
	}
}
