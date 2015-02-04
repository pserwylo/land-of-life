package lol.game.cells;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

public class OceanCell extends FloorCell
{
	private static final float MOISTURE_EVAPORATION_RATE = 0.1f;
	
	public final static String TYPE_STRING = "ocean";
	
	public OceanCell(CellContainer owner)
	{
		super(owner, OceanCell.TYPE_STRING);
		this.type = FloorCell.TYPE_OCEAN;
	}
	
	@Override
	public boolean increaseLevel()
	{
		return false;
	}
	public boolean decreaseLevel()
	{
		return false;
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
		
	}

	@Override
	public void render(Graphics2D g)
	{
		this.renderer.render(g);
	}
	
	public void checkUpdate()
	{
	}

	@Override
	public boolean performAction()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
