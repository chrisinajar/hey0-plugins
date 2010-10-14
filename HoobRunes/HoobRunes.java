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

	public static class HoobRunesSettings
	{
		public Player targetPlayer;
		public boolean tunnelMode = false;
	}

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
					HoobRune.Direction rune_dir = rl.getRune().matches(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());

					if (rune_dir != HoobRune.Direction.NONE)
					{
						RuneCraftListener listener = rl.getListener();
						listener.runeCreated(player);
					}

				}



			}
			return(false);
		}

	}


}

