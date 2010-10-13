import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LowOrbitIonCannon extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	private Listener listener = new Listener();
	public class Listener extends PluginListener {
	ArrayList<String> cannons = new ArrayList<String>();
	
	public boolean onCommand(Player player, String[] split) {
		if(!player.canUseCommand(split[0]))
			return false;
		if(split[0].equalsIgnoreCase("/ion_cannon")) {
			if(cannons.contains(player.getName())) {
				player.sendMessage("Ion Cannon has been disabled.");
				cannons.remove(player.getName());
			} else {
				player.sendMessage(" :: WARNING :: ION CANNON LOADED");
				cannons.add(player.getName());
			}
			return true;
		}
		return false;
	}
	public void onArmSwing(Player player) {
		//If holding a block, left click to place anywhere!
		if ((player.canUseCommand("/ion_cannon")) && (cannons.contains(player.getName())))
		{
			HitBlox blox = new HitBlox(player);
			Block block = blox.getTargetBlock();
			if (block == null)
				return;
			player.sendMessage("BOOM");
			cannons.remove(player.getName());
			//int beamType = 0;
			int beamType = 46;
			int borderType = 57;
			for(int i = 0; i < 128; i++)
			{
				etc.getServer().setBlockAt(beamType, block.getX(), i, block.getZ());
				etc.getServer().setBlockAt(beamType, block.getX() - 1, i, block.getZ());
				etc.getServer().setBlockAt(beamType, block.getX() - 1, i, block.getZ() + 1);
				etc.getServer().setBlockAt(beamType, block.getX() - 1, i, block.getZ() - 1);
				etc.getServer().setBlockAt(beamType, block.getX() - 1, i, block.getZ() + 2);
				etc.getServer().setBlockAt(beamType, block.getX() - 1, i, block.getZ() - 2);
				etc.getServer().setBlockAt(beamType, block.getX() - 1, i, block.getZ() + 3);
				etc.getServer().setBlockAt(beamType, block.getX() - 1, i, block.getZ() - 3);
				etc.getServer().setBlockAt(beamType, block.getX() - 2, i, block.getZ());
				etc.getServer().setBlockAt(beamType, block.getX() - 2, i, block.getZ() + 1);
				etc.getServer().setBlockAt(beamType, block.getX() - 2, i, block.getZ() - 1);
				etc.getServer().setBlockAt(beamType, block.getX() - 2, i, block.getZ() + 2);
				etc.getServer().setBlockAt(beamType, block.getX() - 2, i, block.getZ() - 2);
				etc.getServer().setBlockAt(beamType, block.getX() - 3, i, block.getZ());
				etc.getServer().setBlockAt(beamType, block.getX() - 3, i, block.getZ() + 1);
				etc.getServer().setBlockAt(beamType, block.getX() - 3, i, block.getZ() - 1);
				etc.getServer().setBlockAt(beamType, block.getX() + 1, i, block.getZ());
				etc.getServer().setBlockAt(beamType, block.getX() + 1, i, block.getZ() + 1);
				etc.getServer().setBlockAt(beamType, block.getX() + 1, i, block.getZ() - 1);
				etc.getServer().setBlockAt(beamType, block.getX() + 1, i, block.getZ() + 2);
				etc.getServer().setBlockAt(beamType, block.getX() + 1, i, block.getZ() - 2);
				etc.getServer().setBlockAt(beamType, block.getX() + 1, i, block.getZ() + 3);
				etc.getServer().setBlockAt(beamType, block.getX() + 1, i, block.getZ() - 3);
				etc.getServer().setBlockAt(beamType, block.getX() + 2, i, block.getZ());
				etc.getServer().setBlockAt(beamType, block.getX() + 2, i, block.getZ() + 1);
				etc.getServer().setBlockAt(beamType, block.getX() + 2, i, block.getZ() - 1);
				etc.getServer().setBlockAt(beamType, block.getX() + 2, i, block.getZ() + 2);
				etc.getServer().setBlockAt(beamType, block.getX() + 2, i, block.getZ() - 2);
				etc.getServer().setBlockAt(beamType, block.getX() + 3, i, block.getZ());
				etc.getServer().setBlockAt(beamType, block.getX() + 3, i, block.getZ() + 1);
				etc.getServer().setBlockAt(beamType, block.getX() + 3, i, block.getZ() - 1);
				etc.getServer().setBlockAt(beamType, block.getX(), i, block.getZ() - 1);
				etc.getServer().setBlockAt(beamType, block.getX(), i, block.getZ() + 1);
				etc.getServer().setBlockAt(beamType, block.getX(), i, block.getZ() - 2);
				etc.getServer().setBlockAt(beamType, block.getX(), i, block.getZ() + 2);
				etc.getServer().setBlockAt(beamType, block.getX(), i, block.getZ() - 3);
				etc.getServer().setBlockAt(beamType, block.getX(), i, block.getZ() + 3);
				
				// Border
				etc.getServer().setBlockAt(borderType, block.getX() + 4, i, block.getZ());
				etc.getServer().setBlockAt(borderType, block.getX() + 4, i, block.getZ() + 1);
				etc.getServer().setBlockAt(borderType, block.getX() + 4, i, block.getZ() - 1);
				etc.getServer().setBlockAt(borderType, block.getX() - 4, i, block.getZ());
				etc.getServer().setBlockAt(borderType, block.getX() - 4, i, block.getZ() + 1);
				etc.getServer().setBlockAt(borderType, block.getX() - 4, i, block.getZ() - 1);
				etc.getServer().setBlockAt(borderType, block.getX(), i, block.getZ() + 4);
				etc.getServer().setBlockAt(borderType, block.getX() - 1, i, block.getZ() + 4);
				etc.getServer().setBlockAt(borderType, block.getX() + 1, i, block.getZ() + 4);
				etc.getServer().setBlockAt(borderType, block.getX(), i, block.getZ() - 4);
				etc.getServer().setBlockAt(borderType, block.getX() - 1, i, block.getZ() - 4);
				etc.getServer().setBlockAt(borderType, block.getX() + 1, i, block.getZ() - 4);
				
				etc.getServer().setBlockAt(borderType, block.getX() + 3, i, block.getZ() - 3);
				etc.getServer().setBlockAt(borderType, block.getX() + 3, i, block.getZ() + 3);
				etc.getServer().setBlockAt(borderType, block.getX() - 3, i, block.getZ() - 3);
				etc.getServer().setBlockAt(borderType, block.getX() - 3, i, block.getZ() + 3);
				
				etc.getServer().setBlockAt(borderType, block.getX() - 3, i, block.getZ() - 2);
				etc.getServer().setBlockAt(borderType, block.getX() - 3, i, block.getZ() + 2);
				etc.getServer().setBlockAt(borderType, block.getX() + 3, i, block.getZ() - 2);
				etc.getServer().setBlockAt(borderType, block.getX() + 3, i, block.getZ() + 2);
				
				etc.getServer().setBlockAt(borderType, block.getX() - 2, i, block.getZ() - 3);
				etc.getServer().setBlockAt(borderType, block.getX() - 2, i, block.getZ() + 3);
				etc.getServer().setBlockAt(borderType, block.getX() + 2, i, block.getZ() - 3);
				etc.getServer().setBlockAt(borderType, block.getX() + 2, i, block.getZ() + 3);
				
			}
			etc.getServer().setBlockAt(51, block.getX(), 127, block.getZ());
		}
    }
	}
	
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


