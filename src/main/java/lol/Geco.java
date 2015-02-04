package lol;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import lol.game.Engine;
import lol.gui.GUI;
import lol.gui.MainMenu;

public class Geco implements ActionListener
{

	private MainMenu menuGui = null;
	private GUI gameGui = null;
	private Engine engine = null;
	private Timer timer = null;
	
	public final static int STATE_LISTENING_FOR_INPUT = 1;
	public final static int STATE_RUNNING_SIMULATION  = 2;
	
	
	private int state = STATE_LISTENING_FOR_INPUT;

	public final static int SPEED_SLOW = 800;
	public final static int SPEED_MEDIUM = 400;
	public final static int SPEED_FAST = 100;
	
	private int simulationSpeed = SPEED_MEDIUM;
	
	public Geco()
	{
		this.timer = new Timer(this.simulationSpeed, this);
	}
	
	/**
	 * You only get one guess what this does...
	 */
	public void showMainMenu()
	{
		this.menuGui = new MainMenu(this);
	}
	
	public void nextTurn()
	{
		this.timer.start();
		this.state = Geco.STATE_RUNNING_SIMULATION;
	}
	
	/**
	 * Change the speed at which the simulation runs...
	 * @param speed Milliseconds between steps. Either specify manually
	 * 		or use Geco.SPEED_*
	 */
	public void adjustSpeed(int speed)
	{
		this.simulationSpeed = speed;
		this.timer.setDelay(this.simulationSpeed);
	}
	
	/**
	 * Creates a game and a gui to go with it, 
	 * and hides the main menu...
	 */
	public void startGame()
	{
		//Create new engine and start...
		engine = new Engine(20, 20);
		this.gameGui = new GUI(engine, this);
		
		//Hide the menu gui...
		this.menuGui.setVisible(false);	
		
	}
	
	/**
	 * Gets rid of the game gui and shows the main menu...
	 */
	public void closeGame()
	{
		this.timer.stop();
		this.timer = null;
		this.gameGui.setVisible(false);
		this.gameGui = null;
		this.menuGui.setVisible(true);
	}
	
	/**
	 * Starts up a main menu.
	 * @param args Command Line args to the app (Do nothing here)
	 */
	public static void main(String[] args)
	{
		Geco myApp = new Geco();
		myApp.showMainMenu();
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.timer)
		{
			this.engine.advanceGame();
			this.gameGui.repaint();
			if ( ! this.engine.isRunning() )
			{
				this.timer.stop();
				this.state = Geco.STATE_LISTENING_FOR_INPUT;
			}
		}
	}
	
	/**
	 * Queries Geco for its state (Listening or running)
	 * @return Geco.STATE_*
	 */
	public int getState()
	{
		return this.state;
	}
}
