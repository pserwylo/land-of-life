package lol.clouds;

//import java.awt.*;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

public class CloudGUI extends JFrame implements ActionListener
{
 
	Clouds map = new Clouds(16, 12);
	DrawPanel canvas;
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuFile = new JMenu("File");

	private JMenuItem menuItemReset = new JMenuItem("Reset");
	private JMenuItem menuItemExit = new JMenuItem("Exit");
	
	public CloudGUI()
	{
		init(640, 480);
	}
	
	public CloudGUI(int w, int h)
	{
		init(w, h);
	}
	
	private void init(int w, int h)
	{
		setSize(w, h);
		setLayout(new BorderLayout());

		//Set up menu system...
		menuFile.add(menuItemReset);
		menuFile.add(menuItemExit);
		menuBar.add(menuFile);
		add(menuBar, BorderLayout.NORTH);
		menuItemReset.addActionListener(this);
		menuItemExit.addActionListener(this);
		
		canvas = new DrawPanel(map);
		add(canvas, BorderLayout.CENTER);
		
		canvas.repaint(); 
		
		setTitle("Noise Function");
		
		addWindowListener(new WindowHandler());
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == menuItemExit)
		{
			System.exit(0);
		}
		else if (e.getSource() == menuItemReset)
		{
			map.seed();
			canvas.repaint();
		}
	}
	
	/**
	 * Inner class to handle window events...
	 * @author god
	 */
	public class WindowHandler extends WindowAdapter
	{
		/**
		 * As it stands, simply shuts down the jvm when the window is closed...
		 */
		public void windowClosing(WindowEvent we)
		{
			System.exit(0);
		}
	}
	
	public static void main(String[] args)
	{
		CloudGUI myApp = new CloudGUI();
		myApp.setVisible(true);
	}
	
	public static final long serialVersionUID = 99999999991l;
}
