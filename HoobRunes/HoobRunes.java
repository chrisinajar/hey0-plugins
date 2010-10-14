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
	//private List<RuneCraftRegisteredListener> regListeners = new ArrayList<RuneCraftRegisteredListener>();
	public RunePluginLoader runeloader = new RunePluginLoader(this);
    
	public void enable()
	{
		a.log(Level.INFO, "HoobRunes Enabled!");
	}

	public void disable()
	{
	}
	
	public void initialize()
	{
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, listener, this, PluginListener.Priority.MEDIUM);
		a.log(Level.INFO, "HoobRunes Initialized!");
		runeloader.loadRunePlugins();
    }
	
	public class HoobRunesListener extends PluginListener
	{
		
		//Toggles through players on server as targets and tunnel mode
		public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand)
		{
			if ((player.canUseCommand("/HoobRunes")) && (itemInHand == 76))
			{
				runeloader.callHook(player, blockPlaced, blockClicked, itemInHand);
			}
			return false;
		}

	}

	
}
