import java.util.Hashtable;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StairTower extends RunePlugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	//private Listener l = new StairTowerListener();
	private StairTowerListener listener = new StairTowerListener();

	public void enable() { }
	
	public void initialize()
	{
		HoobRune rune = new HoobRune("StairTower", new int[][] { 	{ 55,  55,  55,  55,  55},
																{ 55, -55, -55, -55,  55},
																{ 55, -55, -55, -55,  55},
																{ 55, -55, -55, -55,  55},
																{ 55,  55,  55,  55,  55}	});	
		//etc.getLoader().addListener(PluginLoader.Hook.CHAT, l, this, PluginListener.Priority.MEDIUM);
		HoobRunes hr = (HoobRunes)etc.getLoader().getPlugin("HoobRunes");
		hr.runeloader.addListener(rune, listener, this);
	}
	public void disable() { }
	
	public class StairTowerListener extends RuneCraftListener
	{
		public void runeCreated(Player player, Block blockPlaced, Block blockClicked, int itemInHand)
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
				if (y >= 2 || (z != 4 || x != 2))
				 etc.getServer().setBlockAt(steps[y%4][z][x], blockClicked.getX() + x - 2, blockClicked.getY() + 1 + y, blockClicked.getZ() + z - 2);
			  }
		}
	}

}