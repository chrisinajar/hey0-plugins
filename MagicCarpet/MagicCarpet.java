import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MagicCarpet extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	private Hashtable carpets = new Hashtable();
	public MagicCarpet()
	{
	}
	public class Carpet
	{
		public Boolean[] destroyed = {false, false, false, false, false, false, false, false, false};
		public Boolean[] imadeit = {false, false, false, false, false, false, false, false, false};
		public Boolean destructable = false;
	}
	public void enable()
	{
	}
	
	public void disable()
	{
	}

	public boolean onCommand(Player player, String[] split)
	{
		try {
			if (split[0].equalsIgnoreCase("/magiccarpet")) {
				Carpet carpet = (Carpet)carpets.get(player.getName());
				if (carpet == null)
				{
					player.sendMessage("You are now on a carpet! OH SHIT!");
					carpets.put(player.getName(), new Carpet());
					return true;
				}
				else
				{
					player.sendMessage("Poof! The carpet disappears");
					carpets.remove(player.getName());
					Location from = player.getLocation();
					if (carpet.imadeit[0]) etc.getServer().setBlockAt(0, (int)from.x + 1, (int)from.y - 1, (int)from.z + 1);
					if (carpet.imadeit[1]) etc.getServer().setBlockAt(0, (int)from.x + 1, (int)from.y - 1, (int)from.z);
					if (carpet.imadeit[2]) etc.getServer().setBlockAt(0, (int)from.x + 1, (int)from.y - 1, (int)from.z - 1);
					if (carpet.imadeit[3]) etc.getServer().setBlockAt(0, (int)from.x, (int)from.y - 1, (int)from.z + 1);
					if (carpet.imadeit[4]) etc.getServer().setBlockAt(0, (int)from.x, (int)from.y - 1, (int)from.z);
					if (carpet.imadeit[5]) etc.getServer().setBlockAt(0, (int)from.x, (int)from.y - 1, (int)from.z - 1);
					if (carpet.imadeit[6]) etc.getServer().setBlockAt(0, (int)from.x - 1, (int)from.y - 1, (int)from.z + 1);
					if (carpet.imadeit[7]) etc.getServer().setBlockAt(0, (int)from.x - 1, (int)from.y - 1, (int)from.z);
					if (carpet.imadeit[8]) etc.getServer().setBlockAt(0, (int)from.x - 1, (int)from.y - 1, (int)from.z - 1);
				}
				return true;
			}
		} catch (Exception ex) {
			a.log(Level.SEVERE, "Exception in command handler (Report this to chrisinajar):", ex);
			return false;
		}
		return false;
	}
	
	public boolean onBlockDestroy(Player player, Block block) {
		Carpet carpet = (Carpet)carpets.get(player.getName());
		if (carpet == null)
			return false;
		if(!carpet.imadeit[4])
			return false;
		Location from = player.getLocation();
		if((int)from.x != block.getX() || ((int)from.y -1) != block.getY() || (int)from.z != block.getZ())
			return false;
			
		if (carpet.imadeit[0]) etc.getServer().setBlockAt(0, (int)from.x + 1, (int)from.y - 1, (int)from.z + 1);
		if (carpet.imadeit[1]) etc.getServer().setBlockAt(0, (int)from.x + 1, (int)from.y - 1, (int)from.z);
		if (carpet.imadeit[2]) etc.getServer().setBlockAt(0, (int)from.x + 1, (int)from.y - 1, (int)from.z - 1);
		if (carpet.imadeit[3]) etc.getServer().setBlockAt(0, (int)from.x, (int)from.y - 1, (int)from.z + 1);
		if (carpet.imadeit[4]) etc.getServer().setBlockAt(0, (int)from.x, (int)from.y - 1, (int)from.z);
		if (carpet.imadeit[5]) etc.getServer().setBlockAt(0, (int)from.x, (int)from.y - 1, (int)from.z - 1);
		if (carpet.imadeit[6]) etc.getServer().setBlockAt(0, (int)from.x - 1, (int)from.y - 1, (int)from.z + 1);
		if (carpet.imadeit[7]) etc.getServer().setBlockAt(0, (int)from.x - 1, (int)from.y - 1, (int)from.z);
		if (carpet.imadeit[8]) etc.getServer().setBlockAt(0, (int)from.x - 1, (int)from.y - 1, (int)from.z - 1);
		
		return true;
	}

	public void onPlayerMove(Player player, Location from, Location to)
	{
		Carpet carpet = (Carpet)carpets.get(player.getName());
		if (carpet == null)
			return;
		// Remove my old carpet!
		if (carpet.imadeit[0]) etc.getServer().setBlockAt(0, (int)from.x + 1, (int)from.y - 1, (int)from.z + 1);
		if (carpet.imadeit[1]) etc.getServer().setBlockAt(0, (int)from.x + 1, (int)from.y - 1, (int)from.z);
		if (carpet.imadeit[2]) etc.getServer().setBlockAt(0, (int)from.x + 1, (int)from.y - 1, (int)from.z - 1);
		if (carpet.imadeit[3]) etc.getServer().setBlockAt(0, (int)from.x, (int)from.y - 1, (int)from.z + 1);
		if (carpet.imadeit[4]) etc.getServer().setBlockAt(0, (int)from.x, (int)from.y - 1, (int)from.z);
		if (carpet.imadeit[5]) etc.getServer().setBlockAt(0, (int)from.x, (int)from.y - 1, (int)from.z - 1);
		if (carpet.imadeit[6]) etc.getServer().setBlockAt(0, (int)from.x - 1, (int)from.y - 1, (int)from.z + 1);
		if (carpet.imadeit[7]) etc.getServer().setBlockAt(0, (int)from.x - 1, (int)from.y - 1, (int)from.z);
		if (carpet.imadeit[8]) etc.getServer().setBlockAt(0, (int)from.x - 1, (int)from.y - 1, (int)from.z - 1);
		
		if (!carpet.destroyed[0] && etc.getServer().getBlockAt((int)to.x + 1, (int)to.y - 1, (int)to.z + 1).getType() == 0) {
			carpet.imadeit[0] = true;
			etc.getServer().setBlockAt(20, (int)to.x + 1, (int)to.y - 1, (int)to.z + 1);
		} else { carpet.imadeit[0] = false; }
		if (!carpet.destroyed[1] && etc.getServer().getBlockAt((int)to.x + 1, (int)to.y - 1, (int)to.z).getType() == 0) {
			carpet.imadeit[1] = true;
			etc.getServer().setBlockAt(20, (int)to.x + 1, (int)to.y - 1, (int)to.z);
		} else { carpet.imadeit[1] = false; }
		if (!carpet.destroyed[2] && etc.getServer().getBlockAt((int)to.x + 1, (int)to.y - 1, (int)to.z - 1).getType() == 0) {
			carpet.imadeit[2] = true;
			etc.getServer().setBlockAt(20, (int)to.x + 1, (int)to.y - 1, (int)to.z - 1);
		} else { carpet.imadeit[2] = false; }
		
		if (!carpet.destroyed[3] && etc.getServer().getBlockAt((int)to.x, (int)to.y - 1, (int)to.z + 1).getType() == 0) {
			carpet.imadeit[3] = true;
			etc.getServer().setBlockAt(20, (int)to.x, (int)to.y - 1, (int)to.z + 1);
		} else { carpet.imadeit[3] = false; }
		if (!carpet.destroyed[4] && etc.getServer().getBlockAt((int)to.x, (int)to.y - 1, (int)to.z).getType() == 0) {
			carpet.imadeit[4] = true;
			etc.getServer().setBlockAt(20, (int)to.x, (int)to.y - 1, (int)to.z);
		} else { carpet.imadeit[4] = false; }
		if (!carpet.destroyed[5] && etc.getServer().getBlockAt((int)to.x, (int)to.y - 1, (int)to.z - 1).getType() == 0) {
			carpet.imadeit[5] = true;
			etc.getServer().setBlockAt(20, (int)to.x, (int)to.y - 1, (int)to.z - 1);
		} else { carpet.imadeit[5] = false; }
		
		if (!carpet.destroyed[6] && etc.getServer().getBlockAt((int)to.x - 1, (int)to.y - 1, (int)to.z + 1).getType() == 0) {
			carpet.imadeit[6] = true;
			etc.getServer().setBlockAt(20, (int)to.x - 1, (int)to.y - 1, (int)to.z + 1);
		} else { carpet.imadeit[6] = false; }
		if (!carpet.destroyed[7] && etc.getServer().getBlockAt((int)to.x - 1, (int)to.y - 1, (int)to.z).getType() == 0) {
			carpet.imadeit[7] = true;
			etc.getServer().setBlockAt(20, (int)to.x - 1, (int)to.y - 1, (int)to.z);
		} else { carpet.imadeit[7] = false; }
		if (!carpet.destroyed[8] && etc.getServer().getBlockAt((int)to.x - 1, (int)to.y - 1, (int)to.z - 1).getType() == 0) {
			carpet.imadeit[8] = true;
			etc.getServer().setBlockAt(20, (int)to.x - 1, (int)to.y - 1, (int)to.z - 1);
		} else { carpet.imadeit[8] = false; }

	}
}

