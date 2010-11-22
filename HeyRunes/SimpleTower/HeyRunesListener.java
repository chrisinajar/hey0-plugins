import java.util.ArrayList;


public class HeyRunesListener
{
	public void internalRuneCreated(Player pl, String name, ArrayList<Integer> pattern, Block block)
	{
		runeCreated(pl, new HeyRune(name, pattern), block);
	}
	public void runeCreated(Player player, HeyRune runePlaced, Block block) {}
}
