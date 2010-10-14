import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HoobRunes extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	private HoobRunesListener listener = new HoobRunesListener();
	public static ArrayList<HoobRune> runes = new ArrayList<HoobRune>();
	private List<RuneCraftRegisteredListener> regListeners = new ArrayList<RuneCraftRegisteredListener>();

	
	public RuneCraftRegisteredListener addListener(HoobRune rune, RuneCraftListener listener, Plugin plugin)
	{
    	RuneCraftRegisteredListener reg = new RuneCraftRegisteredListener(rune, listener, plugin);
	    regListeners.add(reg);
    	return reg;
    }
	
	public static Hashtable playerSettings = new Hashtable();

	public static HoobRunesSettings getSettings(Player player)
	{
		HoobRunesSettings settings = (HoobRunesSettings)playerSettings.get(player.getName());
		if (settings == null)
		{
			playerSettings.put(player.getName(), new HoobRunesSettings());
			settings = (HoobRunesSettings)playerSettings.get(player.getName());
		}

		return(settings);
	}
	
	
	//private enum Direction { NORTH, SOUTH, EAST, WEST, UP, DOWN, NONE }
	
	public static class HoobRunesSettings
	{
		public Player targetPlayer;
		public boolean tunnelMode = false;
	}
	
	public void enable()
	{
		/*
			runes.add(new HoobRune("Tower", new int[][] { 	{-55, -55, -55, -55, -55},
															{-55,  55,  55,  55, -55},
															{-55,  55, -55,  55, -55},
															{-55,  55,  55,  55, -55},
															{-55, -55, -55, -55, -55}	}));
														
			runes.add(new HoobRune("Pit", new int[][] { 	{-55, -55, -55, -55, -55},
															{-55,  55, -55,  55, -55},
															{-55, -55, -55, -55, -55},
															{-55,  55, -55,  55, -55},
															{-55, -55, -55, -55, -55}	}));														

			runes.add(new HoobRune("StairTower", new int[][] { 	{ 55,  55,  55,  55,  55},
																{ 55, -55, -55, -55,  55},
																{ 55, -55, -55, -55,  55},
																{ 55, -55, -55, -55,  55},
																{ 55,  55,  55,  55,  55}	}));														

			runes.add(new HoobRune("RedTorches", new int[][] { 	{ -55, -55,  55, -55, -55},
																{ -55, -55,  55, -55, -55},
																{ -55, -55, -55, -55, -55},
																{ -55, -55, -55, -55, -55},
																{ -55, -55, -55, -55, -55}	}));	
		*/
		
		a.log(Level.INFO, "HoobRunes Enabled!");

	}

	public void disable()
	{
	}
	
	public void initialize()
	{
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, listener, this, PluginListener.Priority.MEDIUM);
		a.log(Level.INFO, "HoobRunes Initialized!");
    }
	
	public class HoobRunesListener extends PluginListener
	{
		
		//Toggles through players on server as targets and tunnel mode
		public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand)
		{
			if ((player.canUseCommand("/HoobRunes")) && (itemInHand == 76))
			{
				player.sendMessage("GOT A TORCH!");
			
				HoobRunes.HoobRunesSettings settings = HoobRunes.getSettings(player);
				
				for (RuneCraftRegisteredListener rl : regListeners)
				{
					
				
				/*
				for (HoobRune r : HoobRunes.runes) */
				//{
					HoobRune.Direction rune_dir = rl.getRune().matches(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
					
					//NORTH IS IN -X direction
					//SOUTH IS IN +X direction
					//EAST IS IN -Z direction
					//WEST IS IN +Z direction
					
					switch (rune_dir)
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
					}

					if (rune_dir != HoobRune.Direction.NONE)
					{
						RuneCraftListener listener = rl.getListener();
						listener.runeCreated(player);
					}
					
					/*
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
						else if (r.name == "RedTorches")
						{
							boolean done = false;
							player.sendMessage("GOT TORCHES!");
							switch (rune_dir)
							{
								case NORTH:
									player.sendMessage("NORTH!");
									//for(int i = 0; i < 100)
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
							}
						}

						break; //break out of for loop?
						*/
				
				}

			
				
			}
			return(false);
		}

	}

	
}
