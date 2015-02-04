package lol.game.cells;

import java.awt.Graphics2D;

public abstract class FloorCell extends Cell
{
	public final static int MAX_LEVEL = 5;
	
	/**
	 * Each different kind of cell has a maximum level of only 5.
	 */
	protected int level = 1;
	
	protected float rainingThreshold = 1.0f;

	/**
	 * All floor cells have potential to be rained on, therefore they
	 * can all collect moisture. 
	 */
	protected float moisture = 0.0f;

	public final static int TYPE_DEFAULT = 0;
	public final static int TYPE_GROUND = 1;
	public final static int TYPE_MOUNTAIN = 2;
	public final static int TYPE_RIVER = 3;
	public final static int TYPE_PLANT = 4;
	public final static int TYPE_OCEAN = 5;
	
	/**
	 * All floor cells have a value corresponding to the cell type, to
	 * identify it's class.
	 * 0: default, no cell should have this value
	 * 1: ground
	 * 2: mountain
	 * 3: river
	 * 4: plant 
	 * 5: ocean (inert class)
	 */
	protected int type = 0;


	public final static int CHANGE_NO_CHANGE = 0;
	public final static int CHANGE_MOUNTAIN_PLUS_LEVEL = 1;
	public final static int CHANGE_MOUNTAIN_MINUS_LEVEL = 2;
	public final static int CHANGE_RIVER_PLUS_LEVEL = 3;
	public final static int CHANGE_RIVER_MINUS_LEVEL = 4;
	public final static int CHANGE_RIVER_LEVEL = 30;
	public final static int CHANGE_RIVER_LEVEL_SOURCE = 40;
	public final static int CHANGE_PLANT_PLUS_LEVEL = 5;
	public final static int CHANGE_PLANT_MINUS_LEVEL = 6;
	public final static int CHANGE_SET_GROUND = 7;
	
	/**
	 * Changes are done simultaneously except river which is grown. Cells are first checked for which
	 * update to implement, then they're all called at once.
	 * 0: no change
	 * 1: mountain +level (if not mountain, change to mountain level 1)
	 * 2: mountain -level (if level 1, change to ground) (unused)
	 * 3: river +level (if not river, change to river level 1) (unused)
	 * 4: river -level (if level 1, change to ground) (unused)
	 * 3x: change to river level x, source = false; (unused)
	 * 4x: change to river level x, source = true; 
	 * 5: plant +level (if not plant, change to plant level 1)
	 * 6: plant -level (if level 1, change to ground)
	 * 7: set ground
	 */
	protected int changeType = 0;
	
	protected CellContainer owner;
	
	/**
	 * Each tile needs a renderer to draw itself.
	 */
	protected TileRenderer renderer = TileRenderer.NULL_TILE;
	
	public FloorCell(CellContainer owner, String type)
	{
		this.renderer = TileManager.getRenderer(type, 1);
		this.owner = owner;
	}
	
	/**
	 * Ask the renderer to render itself.
	 * Can be overridden if you like, but not needed.
	 * @param g The graphics context to render to.
	 */
	public void render(Graphics2D g)
	{
		this.renderer.render(g);
	}
	
	/**
	 * Assignes a new renderer to this cell.
	 * @param renderer
	 */
	protected final void setRenderer(TileRenderer renderer)
	{
		this.renderer = renderer;
	}
	
	/**
	 * Gets the current level of this Cell.<br/>
	 * Levels must be increased in the sub class, because only they have the
	 * logic required to perform the increase.
	 * @return Cell level.
	 */
	public final int getLevel()
	{
		return this.level;
	}
	
	protected boolean changeLevel(int x)
	{
		this.level += x;
		if (x <= 0)
		{
			x = 0;
			return false;
		}
		return true;
	}
	
	public final void updateCell()
	{
		switch (changeType)
		{
			case FloorCell.CHANGE_MOUNTAIN_PLUS_LEVEL:	if (type == 1)
					{
						this.increaseLevel();
					}
					else
					{
						this.owner.setCell(new MountainCell(this.owner));
					}
					break;
			case FloorCell.CHANGE_MOUNTAIN_MINUS_LEVEL:	if (type == 1)
					{
						this.decreaseLevel();
					}
					else
					{
						this.owner.setCell(new GroundCell(this.owner)); // this shouldn't happen
					}
					break;
			case FloorCell.CHANGE_RIVER_LEVEL + 1:this.owner.setCell(new RiverCell(this.owner,false,0,this.owner.getCell()));
					break;
			case FloorCell.CHANGE_RIVER_LEVEL + 2:this.owner.setCell(new RiverCell(this.owner,false,1,this.owner.getCell()));
					break;					
			case FloorCell.CHANGE_RIVER_LEVEL + 3:this.owner.setCell(new RiverCell(this.owner,false,2,this.owner.getCell()));
					break;					
			case FloorCell.CHANGE_RIVER_LEVEL + 4:this.owner.setCell(new RiverCell(this.owner,false,3,this.owner.getCell()));
					break;					
			case FloorCell.CHANGE_RIVER_LEVEL + 5:
			case FloorCell.CHANGE_RIVER_LEVEL + 6:this.owner.setCell(new RiverCell(this.owner,false,4,this.owner.getCell()));
					break;
			case FloorCell.CHANGE_RIVER_LEVEL_SOURCE + 1:this.owner.setCell(new RiverCell(this.owner,true,0,this.owner.getCell()));
					break;
			case FloorCell.CHANGE_RIVER_LEVEL_SOURCE + 2:this.owner.setCell(new RiverCell(this.owner,true,1,this.owner.getCell()));
					break;
			case FloorCell.CHANGE_RIVER_LEVEL_SOURCE + 3:this.owner.setCell(new RiverCell(this.owner,true,2,this.owner.getCell()));
					break;
			case FloorCell.CHANGE_RIVER_LEVEL_SOURCE + 4:this.owner.setCell(new RiverCell(this.owner,true,3,this.owner.getCell()));
					break;
			case FloorCell.CHANGE_RIVER_LEVEL_SOURCE + 5:
			case FloorCell.CHANGE_RIVER_LEVEL_SOURCE + 6:this.owner.setCell(new RiverCell(this.owner,true,4,this.owner.getCell()));
					break;
			case FloorCell.CHANGE_RIVER_MINUS_LEVEL:	
					if(this.level >=1)
					{
						this.owner.setCell(new RiverCell(this.owner,true,this.level-1,this.owner.getCell()));
					}
					else this.owner.setCell(new GroundCell(this.owner));
					break;
			case FloorCell.CHANGE_PLANT_PLUS_LEVEL:	
				if (this.type == FloorCell.TYPE_PLANT)
				{
					this.increaseLevel();
				}
				else
				{
					this.owner.setCell(new PlantCell(this.owner));
				}
				break;
			case FloorCell.CHANGE_PLANT_MINUS_LEVEL:
				if (type == 4)
				{
					this.decreaseLevel();
				}
				else
				{
					this.owner.setCell(new GroundCell(this.owner)); //this shouldn't happen
				}
				break;
			case FloorCell.CHANGE_SET_GROUND:	
				if (type != 1)
				{
					this.owner.setCell(new GroundCell(this.owner));
				}
				break;
		}
	}
	
	/**
	 * If a cloud's density is above this raining threshold, it will start to rain.
	 * @return Clamped float between 0 and 1
	 */
	public final float getRainingThreshold()
	{
		return this.rainingThreshold;
	}
	
	public final float getMoisture()
	{
		return this.moisture;
	}
	
	public final void setContainer(CellContainer owner)
	{
		this.owner = owner;
	}
	
	/**
	 * Each ground cell must have a particular effect on a cloud cell.</br>
	 * The effect must be one of either:
	 * <ul>
	 * 	<li>Make the cloud denser (evaporate water from the cell)</li>
	 * 	<li>Make the cloud weaker (has no water to offer the cloud)</li>
	 * 	<li>Leave the cloud in equilibrium</li>
	 * </ul>
	 * @return Amount of moisture to add to cloud.
	 */
	public abstract float moistureReturnedToCloud();
	
	/**
	 * When a heavy cloud passes by and rains, it should have some effect on
	 * the ground below it.
	 * 
	 * @param amount Clamped value between 0.0f and 1.0f.
	 */
	public abstract void recieveRain(float amount);
	
	/**
	 * Perform some sort of action when interacted with.<br/>
	 * Right now, interaction is through a mouse click.
	 * @return True if something has changed.
	 */
	public abstract boolean performAction();
	
	/**
	 * Ask the object to increase its level. <br />
	 * It will look at all of the neighbours to decide if it grows or not.<br/>
	 * 
	 * @return true if level was increased
	 */
	public abstract boolean increaseLevel();
	public abstract boolean decreaseLevel();
	
	/**
	 * Ask cell if any changes needs to be done on itself or neighbours
	 */
	public abstract void checkUpdate();

}
