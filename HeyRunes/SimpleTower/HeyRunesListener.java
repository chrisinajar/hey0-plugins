import java.util.ArrayList;

// extends PluginListener so that you can add hooks from hMod too! Yay!
public class HeyRunesListener extends PluginListener
{
	public void internalRuneCreated(Player pl, String name, ArrayList<Integer> pattern, Block block)
	{
		runeCreated(pl, new HeyRune(name, pattern), block);
	}
	public void runeCreated(Player player, HeyRune runePlaced, Block block) {}
}
