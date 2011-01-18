import java.util.ArrayList;

// extends PluginListener so that you can add hooks from hMod too! Yay!
public class HeyRunesListener extends PluginListener
{
	// Old version, no longer used, but here for reverse compatibility
	public void internalRuneCreated(Player pl, String name, ArrayList<Integer> pattern, Block block)
	{
		runeCreated(pl, new HeyRune(name, pattern), block);
	}

	// Fix for passing direction, no longer used, but here for reverse compatibility
	public void internalRuneCreated(Player pl, String name, ArrayList<Integer> pattern, int direction, Block block)
	{
		runeCreated(pl, new HeyRune(name, pattern, direction), block);
	}

	// Added different match types, newest and used
	public void internalRuneCreated(Player pl, String name, ArrayList<Integer> pattern, int direction, int matchType, Block block)
	{
		runeCreated(pl, new HeyRune(name, pattern, direction), block);
		if (matchType >= HeyRune.MatchType.values().length)
			matchType = 0; // For when I add new match types! *thinking ahead* :D

		runeCreated(pl, new HeyRune(name, pattern, direction), block, HeyRune.MatchType.values()[matchType]);
	}

	public void runeCreated(Player player, HeyRune runePlaced, Block block) {}
	public void runeCreated(Player player, HeyRune runePlaced, Block block, HeyRune.MatchType mt) {}
}
