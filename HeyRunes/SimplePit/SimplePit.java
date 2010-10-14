import java.util.Hashtable;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimplePit extends RunePlugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	//private Listener l = new SimplePitListener();
	private SimplePitListener listener = new SimplePitListener();

	public void enable() { }
	
	public void initialize()
	{
		HeyRune rune = new HeyRune("Pit", new int[][] { 	{-55, -55, -55, -55, -55},
															{-55,  55, -55,  55, -55},
															{-55, -55, -55, -55, -55},
															{-55,  55, -55,  55, -55},
															{-55, -55, -55, -55, -55}	});		
		//etc.getLoader().addListener(PluginLoader.Hook.CHAT, l, this, PluginListener.Priority.MEDIUM);
		HeyRunes hr = (HeyRunes)etc.getLoader().getPlugin("HeyRunes");
		hr.runeloader.addListener(rune, listener, this);
	}
	public void disable() { }
	
	public class SimplePitListener extends RuneCraftListener
	{
		public void runeCreated(Player player, Block blockPlaced, Block blockClicked, int itemInHand)
		{
			player.sendMessage("Simple Pit!");
			for (int y = 0; y > -11; y--)
			 for (int x = 0; x < 3; x++)
			  for (int z = 0; z < 3; z++)
			   etc.getServer().setBlockAt(0, blockClicked.getX() + x - 1, blockClicked.getY() + 1 + y, blockClicked.getZ() + z - 1);
		}
	}

}