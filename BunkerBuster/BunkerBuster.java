import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BunkerBuster extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	private Listener listener = new Listener();
	public class Listener extends PluginListener {
	Hashtable busters = new Hashtable();
	
	public boolean onCommand(Player player, String[] split) {
		if(!player.canUseCommand(split[0]))
			return false;
		if(split[0].equalsIgnoreCase("/bunker_buster")) {
			if(busters.containsKey(player.getName())) {
				player.sendMessage("Bunker Buster has been disabled.");
				busters.remove(player.getName());
			} else {
				int size = 10;
				if(split.length > 1)
					size = Integer.parseInt(split[1]);
				if(size < 1)
					size = 1;
				player.sendMessage(" :: WARNING :: BUNKER BUSTER LOADED");
				player.sendMessage(" :: WARNING :: BOMB SIZE: " + size);
				
				busters.put(player.getName(), size);
			}
			return true;
		}
		return false;
	}

	public void onArmSwing(Player player) {
		if ((player.canUseCommand("/bunker_buster")) && (busters.containsKey(player.getName())))
		{
			HitBlox blox = new HitBlox(player);
			Block block = blox.getTargetBlock();
			if (block == null)
				return;
			player.sendMessage("THUD .....");
			int size = ((Integer)busters.get(player.getName())).intValue();
			busters.remove(player.getName());
			int bombType = 46;
			int x = block.getX() - (size/2);
			int z = block.getZ() - (size/2);
			int y = block.getY() - (int)(size*3); // we build from the bottom up, so we have to subtract depth /and/ height
			
			if (block.getY() < (size * 1.1))
			{
				player.sendMessage("Our satelites indicate there isn't enough ground to fit the bomb");
				return;
			}

			if (y < 1)
			{
				y = 0;
			}

			for (int i = y; i <= block.getY(); ++i)
			{
				etc.getServer().setBlockAt(0, block.getX(), i, block.getZ()); // the whole it blases through
			}
			// If it falls into a cavern, we want to find the floor first to simulate the bomb "falling" into it
			for (;y > 0; y--)
			{
				if (etc.getServer().getBlockIdAt(block.getX(), y - 1, block.getZ()) != 0)
					break;
			}
			for (int i = 0; i < size; ++i) // x
			{
			for (int j = 0; j < size; ++j) // y
			{
			for (int k = 0; k < size; ++k) // z
			{
				etc.getServer().setBlockAt(bombType, x + i, y + j, z + k);
			}
			}
			}
			etc.getServer().setBlockAt(51, block.getX(), y, block.getZ()); // fuse
		}
	}
	} // end listener class
	
	public void initialize()
	{
		etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
	}
	
	public void enable()
	{
	}

	public void disable()
	{
	}
}


