package lol.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import lol.game.Engine;

public class DrawPanel extends JPanel
{
	/**
	 * Swing likes this...
	 */
	private static final long serialVersionUID = 1L;
	int gridLoc;
	private Engine engine;
	
	public DrawPanel(Engine engine)
	{
		this.engine = engine;
		gridLoc = -1;
	}
	
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		//this.engine.render(g);
		this.engine.render(g2d);
	}
		
}
