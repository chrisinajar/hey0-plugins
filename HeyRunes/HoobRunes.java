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
        etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
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
        
        public boolean onCommand(Player player, String[] split)
        {
            if((split[0].equalsIgnoreCase("/HeyRunes") || split[0].equalsIgnoreCase("/hr")) && player.canUseCommand("/HeyRunes"))
            {
                if (split.length <= 1 || (split.length > 1 && split[1].equalsIgnoreCase("help")))
                {
                    player.sendMessage("COMMANDS:");
                    player.sendMessage(" /hr enable <RuneName>");
                    player.sendMessage(" /hr disable <RuneName>");
                    player.sendMessage(" /hr reload <RuneName>");
                }
                else if (split.length > 1)
                {
                    if (split[1].equalsIgnoreCase("enable"))
                    {
                        if (split.length == 3)
                        {
                            player.sendMessage("Enabling rune: " + split[2]);
                            runeloader.enableRunePlugin(split[2]);
                        }
                        else
                        {
                            player.sendMessage("USAGE: /hr enable <RuneName>");
                        }
                    }
                    else if (split[1].equalsIgnoreCase("disable"))
                    {
                        if (split.length == 3)
                        {
                            player.sendMessage("Disabling rune: " + split[2]);
                            runeloader.disableRunePlugin(split[2]);
                        }
                        else
                        {
                            player.sendMessage("USAGE: /hr disable <RuneName>");
                        }
                    }
                    else if (split[1].equalsIgnoreCase("reload"))
                    {
                        if (split.length == 3)
                        {
                            player.sendMessage("Reloading rune: " + split[2]);
                            runeloader.reloadRunePlugin(split[2]);
                        }
                        else
                        {
                            player.sendMessage("USAGE: /hr reload <RuneName>");
                        }
                    }

                }
                return(true);
            }
            return(false);
        }

	}

}
