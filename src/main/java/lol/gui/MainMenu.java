package lol.gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.*;

import javax.swing.*;

import lol.Geco;

public class MainMenu extends JFrame implements ActionListener
{

	private Geco game;
	
	private JPanel panelButtons = new JPanel();
	private JButton btnNewGame = new JButton("New Game");
	private JButton btnQuit = new JButton("Quit");
    private JLabel splash;
	
	public MainMenu(Geco game)
	{
		this.game = game;
		
		this.setTitle("Land Of Life - Main Menu");
		this.setSize(800, 600);
		
		this.getContentPane().setBackground(Color.BLACK);
		this.panelButtons.setBackground(Color.BLACK);
		
		this.setLayout(new BorderLayout());
		
		//Setup splash image...
		ImageIcon icon = new ImageIcon("images/splash.png", "Land of Life!");
        this.splash = new javax.swing.JLabel(icon);
        this.add(this.splash, BorderLayout.CENTER);
        this.splash.setBounds(30, 30, 395, 85);
        
        this.panelButtons.setLayout(new FlowLayout());
        this.panelButtons.add(this.btnQuit);
        this.panelButtons.add(this.btnNewGame);
        this.btnNewGame.requestFocusInWindow();

        this.btnNewGame.addActionListener(this);
        this.btnQuit.addActionListener(this);
        
        this.add(this.panelButtons, BorderLayout.SOUTH);
    
        this.setLNF();
        
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnNewGame)
		{
			this.game.startGame();
		}
		else if (e.getSource() == this.btnQuit)
		{
			int response = 
				JOptionPane.showConfirmDialog(
					this, "Are you sure you would like to quit?", 
					"Confirm Quit", JOptionPane.YES_NO_OPTION);
			if (response == JOptionPane.YES_OPTION)
			{
				System.exit(0);
			}		
		}
	}
	
	private void setLNF()
	{
		try
		{
		    //com.jgoodies.looks.windows.WindowsLookAndFeel
		    //com.jgoodies.looks.plastic.PlasticLookAndFeel
		    //com.jgoodies.looks.plastic.Plastic3DLookAndFeel
		    //com.jgoodies.looks.plastic.PlasticXPLookAndFeel
			UIManager.setLookAndFeel(
					"com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
		}
		catch(Exception e)
		{
			System.err.println("Couldn't find LNF, reverting to System LNF.");
			try
			{
				UIManager.setLookAndFeel(
						UIManager.getSystemLookAndFeelClassName());
			}
			catch (Exception ex)
			{
				//stick with crappy java ocean look if that all we can manage
			}
		}
	}
	
	/**
	 * Swing likes this here...
	 */
	private static final long serialVersionUID = 23094823905721390L;

}
