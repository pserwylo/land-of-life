package lol.game.cells;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

public class CloudCell extends Cell
{
	
	/**
	 * This is a measure of how much water is in the cloud cell.
	 * Clamped value between 0 and 1.<br />
	 */
	private float density = 0.0f;
	
	public final static String TYPE_STRING = "cloud";
	
	/**
	 * If the density of the cloud is above the raining threshold,
	 * then it rains!
	 * Clamped value between 0 and 1.<br />
	 */
	private float rainingThreshold = 0.5f;
	
	private boolean raining = false;
	
	public CloudCell(float density)
	{
		this.density = density;
	}
	
	public CloudCell()
	{
		this.density = 0.0f;
	}
	
	/**
	 * Render the cloud.
	 * Clouds are rendered above the usual location...
	 * @params g The context to render to...
	 */
	@Override
	public void render(Graphics2D g)
	{
		int num = (int)(this.density * 6);
		if (num >= 1 && num <= 6)
		{
			
			if (this.raining)
			{
				String rainFilename = "images/tiles/rain.png";
				Image rainImage = Toolkit.getDefaultToolkit().getImage(rainFilename);
				
				//Raise above the ground...
				g.drawImage(rainImage, 0, -Cell.PIXEL_SIZE, null);
			}
			
			num *= 5;
			String cloudFilename = "images/tiles/cloud_a_" + num + ".png";
			Image cloudImage = Toolkit.getDefaultToolkit().getImage(cloudFilename);
			
			//Raise above the ground...
			g.drawImage(cloudImage, 0, -Cell.PIXEL_SIZE, null);
			
		}
	}
	
	/**
	 * Follow a concise set of rules when passing over particular types 
	 * of cells...
	 * @param passedOver The cell the cloud is passing over.
	 */
	public void passOverCell(FloorCell passedOver)
	{
		//See if the cell has any moister for us to evaporate...
		this.density += passedOver.moistureReturnedToCloud();
		
		//Mountains are unique, in that they are the only cells with the
		//ability to coax rain out of a cloud. The higher the mountain,
		//the weaker the cloud needs to be to encourage rain...
		if (passedOver instanceof MountainCell)
		{
			this.rainingThreshold = 1.0f - (float)passedOver.getLevel()/6;
			this.validate();
		}
	}
	
	/**
	 * Passes on rain to the floor cell and decrements our density...
	 * @param rainOn The cell the cloud is to rain on.
	 */
	public void rainOnCell(FloorCell rainOn)
	{
		//Rain on the cell
		rainOn.recieveRain(this.density);
		
		//and adjust out moisture content...
		this.density -= 0.2f;
		this.validate();
	}
	
	/**
	 * Checks if the cloud should be raining or not.<br />
	 * validate() the cloud whenever you change some of its values...
	 */
	private void validate()
	{
		if (this.density < 0)
		{
			this.density = 0;
		}
		else if (this.density > 1.0f)
		{
			this.density = 1.0f;
		}
		
		if (this.density > this.rainingThreshold)
		{
			this.raining = true;
		}
		else
		{
			this.raining = false;
		}
	}
	
	/**
	 * Asks if the cell is raining at this point in time...
	 */
	public boolean isRaining(){return raining;}
}
