import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleTower extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	private SimpleTowerListener listener = new SimpleTowerListener();

	public void enable() { }
	public void disable() { }

	public void initialize()
	{
		// Creating the nested array for the rune... The numbers are ID's, just draw it out!
		HeyRune rune = new HeyRune("Tower", new int[][] {
								{-101,  1,   2,     1, -101},
								{   1,  1,   1, -1001,    1},
								{-102,  1,   2,     2, -102}
								});
		HeyRunes.addListener(rune, listener, this);
	}
	
	public class SimpleTowerListener extends HeyRunesListener
	{
		public void runeCreated(Player player, HeyRune runePlaced, Block block)
		{
			// We just build a simple tower to show off it working
			player.sendMessage("Simple Tower!");
			for (int y = 0; y < 10; y++) {
				for (int x = 0; x < 3; x++) {
					for (int z = 0; z < 3; z++) {
						if (x != 1 || z != 1)
							etc.getServer().setBlockAt(4, block.getX() + x - 1, block.getY() + 1 + y, block.getZ() + z - 1);
					}
				}
			}
			etc.getServer().setBlockAt(1, block.getX(), block.getY() + 1, block.getZ() + 1);
		}
	}

}



