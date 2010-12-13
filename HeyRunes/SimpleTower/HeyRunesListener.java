import java.util.ArrayList;

// extends PluginListener so that you can add hooks from hMod too! Yay!
public class HeyRunesListener extends PluginListener
{
	// Old version, no longer used, but here for reverse compatibility
	public void internalRuneCreated(Player pl, String name, ArrayList<Integer> pattern, Block block)
	{
		runeCreated(pl, new HeyRune(name, pattern), block);
	}
	// Fix for passing direction
	public void internalRuneCreated(Player pl, String name, ArrayList<Integer> pattern, int direction, Block block)
	{
		runeCreated(pl, new HeyRune(name, pattern, direction), block);
	}

	public void runeCreated(Player player, HeyRune runePlaced, Block block) {}
}
