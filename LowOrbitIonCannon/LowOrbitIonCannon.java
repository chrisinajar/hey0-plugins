import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LowOrbitIonCannon extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	private Listener listener = new Listener();

        public void enable()
        {
                JarPlugins.checkUpdatrFile(getClass().getName(), "1.2.0");
        }

	public class Listener extends PluginListener {
	Hashtable cannons = new Hashtable();

	public boolean onCommand(Player player, String[] split) {
		if(!player.canUseCommand(split[0]))
			return false;
		if(split[0].equalsIgnoreCase("/ion_cannon")) {
			if(cannons.containsKey(player.getName())) {
				player.sendMessage("Ion Cannon has been disabled.");
				cannons.remove(player.getName());
			} else {
				int size = 3;
				if(split.length > 1)
					size = Integer.parseInt(split[1]);
				if(size < 1)
					size = 1;
				player.sendMessage(" :: WARNING :: ION CANNON LOADED");
				player.sendMessage(" :: WARNING :: BEAM SIZE: " + size);
				
				cannons.put(player.getName(), size);
			}
			return true;
		}
		return false;
	}
	private void placePoints(int cx, int cz, int x, int z, int y, int blockType)
	{
		if (x == 0)
		{
			// Draw the arms
			etc.getServer().setBlockAt(blockType, cx, y, cz + z);
			etc.getServer().setBlockAt(blockType, cx, y, cz - z);
			etc.getServer().setBlockAt(blockType, cx + z, y, cz);
			etc.getServer().setBlockAt(blockType, cx - z, y, cz);
		}
		else if (x == z)
		{
			etc.getServer().setBlockAt(blockType, cx + x, y, cz + z);
			etc.getServer().setBlockAt(blockType, cx - x, y, cz + z);
			etc.getServer().setBlockAt(blockType, cx + x, y, cz - z);
			etc.getServer().setBlockAt(blockType, cx - x, y, cz - z);
		}
		else if (x < z)
		{
			etc.getServer().setBlockAt(blockType, cx + x, y, cz + z);
			etc.getServer().setBlockAt(blockType, cx - x, y, cz + z);
			etc.getServer().setBlockAt(blockType, cx + x, y, cz - z);
			etc.getServer().setBlockAt(blockType, cx - x, y, cz - z);
			etc.getServer().setBlockAt(blockType, cx + z, y, cz + x);
			etc.getServer().setBlockAt(blockType, cx - z, y, cz + x);
			etc.getServer().setBlockAt(blockType, cx + z, y, cz - x);
			etc.getServer().setBlockAt(blockType, cx - z, y, cz - x);
		}
	}

	private void fillUpwards(int blockType, int x, int y, int z)
	{
		for (int i = 0; i < 5; ++i)
		{
			if (etc.getServer().getBlockIdAt(x, y+i, z) != 0)
				break;
			etc.getServer().setBlockAt(blockType, x, y+i, z);
		}
	}

	private void placeCylinderPoints(int cx, int cz, int x, int z, int y, int blockType)
	{
		if (x == 0)
		{
			// Draw the arms
			fillUpwards(blockType, cx, y, cz + z);
			fillUpwards(blockType, cx, y, cz - z);
			fillUpwards(blockType, cx + z, y, cz);
			fillUpwards(blockType, cx - z, y, cz);
		}
		else if (x == z)
		{
			fillUpwards(blockType, cx + x, y, cz + z);
			fillUpwards(blockType, cx - x, y, cz + z);
			fillUpwards(blockType, cx + x, y, cz - z);
			fillUpwards(blockType, cx - x, y, cz - z);
		}
		else if (x < z)
		{
			fillUpwards(blockType, cx + x, y, cz + z);
			fillUpwards(blockType, cx - x, y, cz + z);
			fillUpwards(blockType, cx + x, y, cz - z);
			fillUpwards(blockType, cx - x, y, cz - z);
			fillUpwards(blockType, cx + z, y, cz + x);
			fillUpwards(blockType, cx - z, y, cz + x);
			fillUpwards(blockType, cx + z, y, cz - x);
			fillUpwards(blockType, cx - z, y, cz - x);
		}
	}

	public void circleMidpoint(int xCenter, int yCenter, int radius, int y, int blockType)
	{
		int x = 0;
		int z = radius;
		int p = (5 - radius*4)/4;
		if(z == 2)
			p--;
		for(int i = z; i >= 0; --i)
			placePoints(xCenter, yCenter, x, i, y, blockType);
		while (x < z) {
			x++;
			if (p < 0) {
				p += 2*x+1;
			} else {
				z--;
				p += 2*(x-z)+1;
			}
			for(int i = z; i >= 0; --i)
				placePoints(xCenter, yCenter, x, i, y, blockType);
		}
	}
	public void cylinderModpoint(int xCenter, int yCenter, int radius, int y, int blockType)
	{
		int x = 0;
		int z = radius;
		int p = (5 - radius*4)/4;
		if(z == 2)
			p--;
		placeCylinderPoints(xCenter, yCenter, x, z, y, blockType);
		while (x < z) {
			x++;
			if (p < 0) {
				p += 2*x+1;
			} else {
				z--;
				p += 2*(x-z)+1;
			}
			placeCylinderPoints(xCenter, yCenter, x, z, y, blockType);
		}
	}

	public void onArmSwing(Player player) {
		//If holding a block, left click to place anywhere!
		if ((player.canUseCommand("/ion_cannon")) && (cannons.containsKey(player.getName())))
		{
			HitBlox blox = new HitBlox(player);
			Block block = blox.getTargetBlock();
			if (block == null)
				return;
			player.sendMessage("BOOM");
			int size = ((Integer)cannons.get(player.getName())).intValue();
			cannons.remove(player.getName());
			//int beamType = 1;
			int beamType = 46;
			int borderType = 57;
			//int i = block.getY();
			for(int i = 0; i < 128; i++)
			{
				circleMidpoint(block.getX(), block.getZ(), size, i, borderType);
				circleMidpoint(block.getX(), block.getZ(), size - 1, i, beamType);
			}
			circleMidpoint(block.getX(), block.getZ(), size, 0, 7);
			cylinderModpoint(block.getX(), block.getZ(), size + 1, 0, 7);
			etc.getServer().setBlockAt(51, block.getX(), 127, block.getZ());
		}
	}
	}
	
	public void initialize()
	{
		etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.MEDIUM);
	}

	public void disable()
	{
	}
}


