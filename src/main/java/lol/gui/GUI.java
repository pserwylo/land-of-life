package lol.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import lol.Geco;
import lol.game.Engine;


public class GUI extends JFrame 
	implements 
		ActionListener, 
		MouseMotionListener, 
		MouseListener,
		ChangeListener
{
	/**
	 * Swing likes this...
	 */
	private static final long serialVersionUID = 195365781723573839L;

	/**
	 * Swing likes this here...
	 */

	private Geco game;
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fileMenu = new JMenu("File");
	private JMenuItem exitMenuItem;

	private JMenu gameMenu = new JMenu("Game");
	private JMenuItem nextTurnMenuItem;
	private JSeparator gameMenuSeparator = new JSeparator();
	private JRadioButtonMenuItem speedSlowMenuItem;
	private JRadioButtonMenuItem speedMediumMenuItem;
	private JRadioButtonMenuItem speedFastMenuItem;
	private ButtonGroup speedGroup = new ButtonGroup();
	
	private JMenu helpMenu = new JMenu("Help");
	private JMenuItem aboutMenuItem, helpMenuItem;
	
	private JButton btnNextTurn;
	private JPanel panelSouth = new JPanel();
	private JLabel statusBar = new JLabel("I'm a nice status bar...");
	
	private DrawPanel canvas;
	
	private Engine engine;
	
	public GUI(Engine engine, Geco game)
	{
		
		this.setTitle("Game Deelio");
		this.setSize(850, 600);
		this.setLayout(new BorderLayout());
		
		this.engine = engine;
		this.game   = game;
		
		ImageIcon exitIcon = new ImageIcon("images/icons/x.gif");
		this.exitMenuItem = new JMenuItem("Exit", exitIcon);
		this.exitMenuItem.addActionListener(this);
		this.fileMenu.add(this.exitMenuItem);
		this.menuBar.add(this.fileMenu);
		
		ImageIcon nextIcon = new ImageIcon("images/icons/skip.gif");
		this.nextTurnMenuItem = new JMenuItem("Next Turn", nextIcon);
		this.nextTurnMenuItem.addActionListener(this);

		this.speedSlowMenuItem = new JRadioButtonMenuItem("Slow");
		this.speedMediumMenuItem = new JRadioButtonMenuItem("Medium");
		this.speedFastMenuItem = new JRadioButtonMenuItem("Fast");
		this.speedGroup.add(speedSlowMenuItem);
		this.speedGroup.add(speedMediumMenuItem);
		this.speedGroup.add(speedFastMenuItem);

		this.speedSlowMenuItem.addChangeListener(this);
		this.speedMediumMenuItem.addChangeListener(this); 
		this.speedFastMenuItem.addChangeListener(this); 		
		
		this.speedMediumMenuItem.setSelected(true);
		
		this.gameMenu.add(this.nextTurnMenuItem);
		this.gameMenu.add(this.gameMenuSeparator);
		this.gameMenu.add(this.speedSlowMenuItem);
		this.gameMenu.add(this.speedMediumMenuItem);
		this.gameMenu.add(this.speedFastMenuItem);
		this.menuBar.add(this.gameMenu);

		ImageIcon helpIcon = new ImageIcon("images/icons/question-mark.gif");
		ImageIcon aboutIcon = new ImageIcon("images/icons/info.gif");
		this.helpMenuItem = new JMenuItem("Help", helpIcon);
		this.aboutMenuItem = new JMenuItem("About", aboutIcon);
		this.helpMenuItem.addActionListener(this);
		this.aboutMenuItem.addActionListener(this);
		this.helpMenu.add(this.helpMenuItem);
		this.helpMenu.add(this.aboutMenuItem);
		this.menuBar.add(this.helpMenu);
		
		this.helpMenuItem.setEnabled(false);
		
		this.add(menuBar, BorderLayout.NORTH);
		
		this.canvas = new DrawPanel(this.engine);
		this.canvas.addMouseMotionListener(this);
		this.canvas.addMouseListener(this);
		this.add(this.canvas, BorderLayout.CENTER);
		
		//ImageIcon nextIcon = new ImageIcon("images/icons/skip.gif");
		this.btnNextTurn = new JButton("Next", nextIcon);
		this.btnNextTurn.addActionListener(this);
		this.panelSouth.add(btnNextTurn);
		this.add(this.panelSouth, BorderLayout.SOUTH);
		
		this.canvas.repaint();
		
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.exitMenuItem)
		{
			this.game.closeGame();
		}
		else if (e.getSource() == this.nextTurnMenuItem ||
				e.getSource() == this.btnNextTurn)
		{
			this.game.nextTurn();
		}
	}
	
	public void mouseDragged(MouseEvent e)
	{
    	if (this.game.getState() == Geco.STATE_LISTENING_FOR_INPUT)
    	{
			 this.engine.onMousePressed(e.getX(), e.getY());
			 this.canvas.repaint();
    	}
	}
	
	/**
	 * Reads mouse location and passes it to engine for picking...
	 */
    public void mouseMoved(MouseEvent me)
    {
    	if (this.game.getState() == Geco.STATE_LISTENING_FOR_INPUT)
    	{
	    	this.engine.getInput(me.getX(), me.getY());
	    	this.canvas.repaint();
    	}
    }

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent e){}
	
	public void mousePressed(MouseEvent e)
	{
    	if (this.game.getState() == Geco.STATE_LISTENING_FOR_INPUT)
    	{
			 this.engine.onMousePressed(e.getX(), e.getY());
			 this.canvas.repaint();
    	}
	}

	public void stateChanged(ChangeEvent e)
	{
		if (this.speedSlowMenuItem.isSelected())
			this.game.adjustSpeed(Geco.SPEED_SLOW);
		else if (this.speedMediumMenuItem.isSelected())
			this.game.adjustSpeed(Geco.SPEED_MEDIUM);
		else if (this.speedFastMenuItem.isSelected())
			this.game.adjustSpeed(Geco.SPEED_FAST);
	}
		
	
}
