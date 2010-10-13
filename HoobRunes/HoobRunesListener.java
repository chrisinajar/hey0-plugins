/**
 * HoobRunesListener.java - Listens for events
 * @author Ho0ber
 */
import java.util.Hashtable;
import java.util.ArrayList;
 
public class HoobRunesListener extends PluginListener
{
	private Hashtable playerSettings = new Hashtable();
	//private enum Direction { NORTH, SOUTH, EAST, WEST, UP, DOWN, NONE }
	
	public class HoobRunesSettings
	{
		public Player targetPlayer;
		public boolean tunnelMode = false;
	}
	
	//Returns a HoobRunesSettings for the player, making a new one if it has to
	public HoobRunesSettings getSettings(Player player)
	{
		HoobRunesSettings settings = (HoobRunesSettings)playerSettings.get(player.getName());
		if (settings == null)
		{
			playerSettings.put(player.getName(), new HoobRunesSettings());
			settings = (HoobRunesSettings)playerSettings.get(player.getName());
		}

		return(settings);
	}
	
	//Toggles through players on server as targets and tunnel mode
	public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand)
	{
		if ((player.canUseCommand("/HoobRunes")) && (itemInHand == 76))
		{
			HoobRunesSettings settings = getSettings(player);
			
			//List<int[][]> runes = new 
			
			boolean done = false;
			

			
			
			for (HoobRune r : HoobRunes.runes)
			{
				HoobRune.Direction rune_dir = r.matches(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
				
	/*			switch (rune_dir)
				{
					case NORTH:
						player.sendMessage("NORTH!");
						break;
					case SOUTH:
						player.sendMessage("SOUTH!");
						break;
					case EAST:
						player.sendMessage("EAST!");
						break;
					case WEST:
						player.sendMessage("WEST!");
						break;
					case NONE:
						player.sendMessage("NONE!");
						break;
				} */

				if (rune_dir != HoobRune.Direction.NONE)
				{
					if (r.name == "Tower")
					{
						player.sendMessage("Tower!");
						for (int y = 0; y < 10; y++)
						 for (int x = 0; x < 3; x++)
						  for (int z = 0; z < 3; z++)
						   if (x != 1 || z != 1)
							etc.getServer().setBlockAt(4, blockClicked.getX() + x - 1, blockClicked.getY() + 1 + y, blockClicked.getZ() + z - 1);
					}
					else if (r.name == "Pit")
					{
						player.sendMessage("Pit!");
						for (int y = 0; y > -11; y--)
						 for (int x = 0; x < 3; x++)
						  for (int z = 0; z < 3; z++)
						   etc.getServer().setBlockAt(0, blockClicked.getX() + x - 1, blockClicked.getY() + 1 + y, blockClicked.getZ() + z - 1);
					}
					if (r.name == "StairTower")
					{
						int[][][] steps = {
											{	{4,4,4,4,4},
												{4,0,0,4,4},
												{4,50,4,67,4},
												{4,0,0,0,4},
												{4,4,4,4,4}		},
												
											{	{4,4,4,4,4},
												{4,4,67,0,4},
												{4,0,4,0,4},
												{4,0,50,0,4},
												{4,4,4,4,4}		},
												
											{	{4,4,4,4,4},
												{4,0,0,0,4},
												{4,67,4,50,4},
												{4,4,0,0,4},
												{4,4,4,4,4}		},
												
											{	{4,4,4,4,4},
												{4,0,50,0,4},
												{4,0,4,0,4},
												{4,0,67,4,4},
												{4,4,4,4,4}		} };
											
						player.sendMessage("StairTower!");
						for (int y = 0; y < 16; y++)
						 for (int x = 0; x < 5; x++)
						  for (int z = 0; z < 5; z++)
						  {
							if (y > 2 || (z != 4 ||	x != 2))
							 etc.getServer().setBlockAt(steps[y%4][z][x], blockClicked.getX() + x - 2, blockClicked.getY() + 1 + y, blockClicked.getZ() + z - 2);
						  }
					}
					break; //break out of for loop?
				}
			}


			
		}
		return(false);
	}

}
