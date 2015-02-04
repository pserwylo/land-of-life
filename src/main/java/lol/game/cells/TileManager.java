package lol.game.cells;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TileManager
{
	private static HashMap<String, ArrayList<TileRenderer>[]> tiles = new HashMap<String, ArrayList<TileRenderer>[]>();
	
	/**
	 * Returns an appropriate renderer for the object given.
	 * Gets random tile from available ones...
	 * 
	 * @params type The type of tile we need...
	 * @return Tile renderer for particular type.
	 */
	public static TileRenderer getRenderer(String type, int level)
	{
		ArrayList<TileRenderer>[] maps = TileManager.tiles.get(type);
		if (maps == null)
		{
			return TileRenderer.NULL_TILE;
		}
		else
		{
			int index = (int)(Math.random() * maps[level-1].size());
			return maps[level-1].get(index);
		}
	}
	
	public static void initTiles()
	{
		String line = "";
		try
		{
			//Read tile list...
			BufferedReader input = new BufferedReader(new FileReader("offsets.tlz"));
			line = input.readLine();
			
			while (line != null)
			{
				//Ignore blank lines and comments...
				if (line.trim().length() > 0 && ! line.substring(0, 1).equals("#"))
				{
					String[] parts = line.split("-");
					for (int i=0; i < parts.length; i ++)
					{
						parts[i] = parts[i].trim();
					}
					
					String type = parts[0];
					ArrayList<TileRenderer>[] toInsert = tiles.get(type);
					if (toInsert == null)
					{
						toInsert = new ArrayList[5];
						//instantiate all of the needed collections...
						for (int i=0; i<FloorCell.MAX_LEVEL; i++)
						{
							toInsert[i] = new ArrayList<TileRenderer>();
						}
						
						//And add it to our HashMap
						TileManager.tiles.put(type, toInsert);
					}
					
					toInsert[Integer.parseInt(parts[3]) - 1].add(
							new TileRenderer(
									"images/tiles/" + parts[1], 
									(int)Double.parseDouble(parts[2])));
				}
				line = input.readLine();
			}
			
			System.out.println("Reading Tiles complete!");
			
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		catch (NumberFormatException nfe)
		{
			System.err.println("Error reading tile info @ '" + line + "'");
		}
	}
}
