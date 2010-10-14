import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeyRunes extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	private HeyRunesListener listener = new HeyRunesListener();
	public static ArrayList<HeyRune> runes = new ArrayList<HeyRune>();
	private List<HeyRunesRegisteredListener> regListeners = new ArrayList<HeyRunesRegisteredListener>();

    public abstract class Listener
	{
		public void runeCreated(Player player, Block blockPlaced, Block blockClicked, int itemInHand) {}
	}

	public void enable()
	{
		a.log(Level.INFO, "HeyRunes Enabled!");
	}

	public void disable()
	{
	}
	
	public void initialize()
	{
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, listener, this, PluginListener.Priority.MEDIUM);
        etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
		a.log(Level.INFO, "HeyRunes Initialized!");
    }
	
	public class HeyRunesListener extends PluginListener
	{
		
		//Toggles through players on server as targets and tunnel mode
		public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand)
		{
			if ((player.canUseCommand("/HeyRunes")) && (itemInHand == 76))
			{
                Iterator<HeyRunesRegisteredListener> iter = regListeners.iterator();
                while (iter.hasNext()) {
					try {
						HeyRunesRegisteredListener listener = iter.next();
						Plugin pl = listener.getPlugin();
						Listener li = listener.getListener();
						HeyRune ru = listener.getRune();
						if (pl == null || li == null ||  ru == null) {
							a.log(Level.INFO, "Disabling NULL plugin from HeyRunes");
							iter.remove();
							continue;
						}
						if (ru.matches(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ()) == HeyRune.Direction.NONE)
							li.runeCreated(player, blockPlaced, blockClicked, itemInHand);
					} catch (Throwable ex) {
						a.log(Level.SEVERE, "Exception while calling HeyRune listener", ex);
					}
				}
				// runeloader.callHook(player, blockPlaced, blockClicked, itemInHand);
			}
			return false;
		}
        
        public boolean onCommand(Player player, String[] split)
        {
		/*
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
		*/
            return(false);
        }
	}

}
