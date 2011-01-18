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
	private List<HeyRunesRegisteredListener> armSwingListeners = new ArrayList<HeyRunesRegisteredListener>();
	private List<HeyRunesRegisteredListener> playerMoveListeners = new ArrayList<HeyRunesRegisteredListener>();


	public void enable()
	{
		JarPlugins.checkUpdatrFile(getClass().getName(), "0.3.0");
	}

	public void disable()
	{
		a.log(Level.SEVERE, "WARNING: HeyRunes plugin is being disabled, if you're reloading this plugin you must reload ALL plugins that use runes for them to continue working properly.");
	}
	
	public void initialize()
	{
		etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener, this, PluginListener.Priority.HIGH);
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
		a.log(Level.INFO, "HeyRunes by chrisinajar Initialized!");
	}
	
	public void addListener(String name, ArrayList<Integer> pattern, Plugin pl, Object listener) {
		addListener2(name, pattern, pl, listener, HeyRune.MatchType.NONE);
	}

	public void addListener2(String name, ArrayList<Integer> pattern, Plugin pl, Object listener, HeyRune.MatchType mt)
	{
		try {
		switch (mt)
		{
			case NONE:
				regListeners.add(new HeyRunesRegisteredListener(new HeyRune(name, pattern), listener, pl));
				break;
			case onArmSwing:
				armSwingListeners.add(new HeyRunesRegisteredListener(new HeyRune(name, pattern), listener, pl));
				break;
			case onPlayerMove:
				playerMoveListeners.add(new HeyRunesRegisteredListener(new HeyRune(name, pattern), listener, pl));
				break;
		}
		} catch (Exception ex) {
			a.log(Level.SEVERE, "Exception while internally registering (new) HeyRunes listener: " + ex);
		}
				
	}
	
	public class HRListener extends PluginListener
	{
		private void matchRune(Player player, Block block, HeyRune.MatchType mt, List<HeyRunesRegisteredListener> listeners)
		{
			if (block == null)
				return;
			boolean permissionOverride = player.canUseCommand("/HeyRunes");
			ArrayList<HeyRune> runeList = new ArrayList<HeyRune>();
			Hashtable<HeyRune, HeyRunesListener> runeTable = new Hashtable<HeyRune, HeyRunesListener>();
			int largestRune = 0;

			List<HeyRunesRegisteredListener> myListeners = new ArrayList<HeyRunesRegisteredListener>();
			myListeners.addAll(listeners);
			myListeners.addAll(regListeners);

			for (HeyRunesRegisteredListener regListener : myListeners) {
				if (!regListener.getPlugin().isEnabled()) {
					continue;
				}
				if (permissionOverride ||
					player.canUseCommand("/Rune-" + regListener.getRune().name()) ||
					player.canUseCommand("/Rune-" + regListener.getPlugin().getName()) ||
					player.canUseCommand("/Rune-" + regListener.getPlugin().getName() + "-" + regListener.getRune().name()))
				{
					runeList.add(regListener.getRune());
					runeTable.put(regListener.getRune(), regListener.getListener());
				}
			}
			ArrayList<HeyRune> matches = HeyRune.match(runeList, block.getX(), block.getY(), block.getZ());
			if (etc.getServer().getBlockIdAt(block.getX(), block.getY() + 1, block.getZ()) == 0)
			{
				// It could be a center air activation!
				ArrayList<HeyRune> airMatches = HeyRune.match(runeList, block.getX(), block.getY() + 1, block.getZ());
				if (matches.size() < 1)
					matches = airMatches;
				else if(airMatches.size() > 0)
				{
					if (airMatches.get(0).size() > matches.get(0).size())
						 matches = airMatches;
				}
			}
			if (matches.size() > 0) {
				for(HeyRune match : matches)
				{
					runeTable.get(match).runeCreated(player, match, block, mt);
				}
			}
		}

		public void onPlayerMove(Player player, Location from, Location to)
		{
			matchRune(player, etc.getServer().getBlockAt((int)Math.floor(player.getX()), (int)Math.floor((player.getY() - 1)), (int)Math.floor(player.getZ())), HeyRune.MatchType.onPlayerMove, playerMoveListeners);
		}

		public void onArmSwing(Player player)
		{
			HitBlox blox = new HitBlox(player);
			Block block = blox.getTargetBlock();
			matchRune(player, block, HeyRune.MatchType.onArmSwing, armSwingListeners);
		}
	}

}
