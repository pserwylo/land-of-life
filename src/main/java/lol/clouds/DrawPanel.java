package lol.clouds;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics2D;


public class DrawPanel extends JPanel
{
	
	private Clouds  toDraw;
	
	public DrawPanel(Clouds  map)
	{
		toDraw = map;
	}
	
	public void paint(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(new Color(0xCCAA33));
		g.fillRect(0, 0, getWidth(), getHeight());

		int width = this.getWidth() / (toDraw.getWidth()-1);
		int height = this.getHeight() / (toDraw.getHeight()-1);
		
		for (int y = 0; y < toDraw.getHeight(); y++)
		{
			for (int x = 0; x < toDraw.getWidth(); x++)
			{
				g.setColor(
						new Color(
								toDraw.getValue(x, y), 
								toDraw.getValue(x, y), 
								toDraw.getValue(x, y)));
				g.fillRect(x*width, y*height, width, height);
			}
		}
		
	}
	
	//Don't know wtf this does... just needed by swing
	static final long serialVersionUID = 9999999999l;
}
