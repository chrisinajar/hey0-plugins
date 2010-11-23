import java.util.Hashtable;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeyRunes extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	private HRListener listener = new HRListener();
	public static ArrayList<HeyRune> runes = new ArrayList<HeyRune>();
	private List<HeyRunesRegisteredListener> regListeners = new ArrayList<HeyRunesRegisteredListener>();


	public void enable()
	{
	}

	public void disable()
	{
	}
	
	public void initialize()
	{
		etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener, this, PluginListener.Priority.HIGH);
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
		a.log(Level.INFO, "HeyRunes by chrisinajar Initialized!");
	}
	
	public void addListener(String name, ArrayList<Integer> pattern, Plugin pl, Object listener) {
		try {
		regListeners.add(new HeyRunesRegisteredListener(new HeyRune(name, pattern), listener, pl));
		} catch (Exception ex) {
			a.log(Level.SEVERE, "Exception while internally registering HeyRunes listener: " + ex);
		}
	}
	
	public class HRListener extends PluginListener
	{
		public void onArmSwing(Player player)
		{
			if ((player.canUseCommand("/HeyRunes")))
			{
				HitBlox blox = new HitBlox(player);
				Block block = blox.getTargetBlock();
				if (block == null)
					return;
				ArrayList<HeyRune> runeList = new ArrayList<HeyRune>();
				Hashtable<HeyRune, HeyRunesListener> runeTable = new Hashtable<HeyRune, HeyRunesListener>();
				int largestRune = 0;

				for (HeyRunesRegisteredListener regListener : regListeners) {
					if (!regListener.getPlugin().isEnabled()) {
						continue;
					}
					runeList.add(regListener.getRune());
					runeTable.put(regListener.getRune(), regListener.getListener());
				}
				HeyRune match = HeyRune.match(runeList, block.getX(), block.getY(), block.getZ());
				if (match != null) {
					runeTable.get(match).runeCreated(player, match, block);
				}
			}
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
