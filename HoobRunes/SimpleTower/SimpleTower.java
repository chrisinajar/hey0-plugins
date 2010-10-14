import java.util.Hashtable;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleTower extends RunePlugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	//private Listener l = new SimpleTowerListener();
	private SimpleTowerListener listener = new SimpleTowerListener();

	public void enable() { }
	
	public void initialize()
	{
		HoobRune rune = new HoobRune("Tower", new int[][] { 	{-55, -55, -55, -55, -55},
															{-55,  55,  55,  55, -55},
															{-55,  55, -55,  55, -55},
															{-55,  55,  55,  55, -55},
															{-55, -55, -55, -55, -55}	});
		//etc.getLoader().addListener(PluginLoader.Hook.CHAT, l, this, PluginListener.Priority.MEDIUM);
		HoobRunes hr = (HoobRunes)etc.getLoader().getPlugin("HoobRunes");
		hr.runeloader.addListener(rune, listener, this);
	}
	public void disable() { }
	
	public class SimpleTowerListener extends RuneCraftListener
	{
		public void runeCreated(Player player, Block blockPlaced, Block blockClicked, int itemInHand)
		{
			player.sendMessage("Simple Tower!");
			for (int y = 0; y < 10; y++)
			 for (int x = 0; x < 3; x++)
			  for (int z = 0; z < 3; z++)
			   if (x != 1 || z != 1)
				etc.getServer().setBlockAt(4, blockClicked.getX() + x - 1, blockClicked.getY() + 1 + y, blockClicked.getZ() + z - 1);
		}
	}

}