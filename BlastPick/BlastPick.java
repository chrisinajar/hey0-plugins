import java.util.Hashtable;

public class BlastPick extends Plugin
{
	private Listener l = new Listener(this);
	private Hashtable playerSettings = new Hashtable();
	
	public void enable()
	{
	}

	public void disable()
	{
	}
	
	public void initialize()
	{
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, l, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, l, this, PluginListener.Priority.MEDIUM);
	}
	
	
	
	private class BlastPickSettings
	{
		public boolean blastpickOn = false;
	}
	
	//Returns a BlastPickSettings for the player, making a new one if it has to
	public BlastPickSettings getSettings(Player player)
	{
		BlastPickSettings settings = (BlastPickSettings)playerSettings.get(player.getName());
		if (settings == null)
		{
			playerSettings.put(player.getName(), new BlastPickSettings());
			settings = (BlastPickSettings)playerSettings.get(player.getName());
		}

		return(settings);
	}
	
	public class Listener extends PluginListener {
		BlastPick plugin;
		Listener(BlastPick p) {
			plugin = p;
		}
		
		public boolean onCommand(Player player, String[] split)
		{
			if (split[0].equalsIgnoreCase("/blastpick") && player.canUseCommand("/blastpick"))
			{
				BlastPickSettings settings = getSettings(player);
				if (settings.blastpickOn)
				{
					player.sendMessage("BlastPick: OFF");
					settings.blastpickOn = false;
				}
				else
				{
					player.sendMessage("BlastPick: ON (Right-click with diamond pick)");
					settings.blastpickOn = true;
				}
				return true;
			}
			return false;
		}
		
		
		public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand)
		{
			if (player.canUseCommand("/blastpick") && itemInHand == 278)
			{
				BlastPickSettings settings = getSettings(player);
				if (settings.blastpickOn)
				{
					Location myLoc = player.getLocation();
					int offx = 0;
					int offy = 0;
					int offz = 0;
					float rotx = myLoc.rotX % 360;
					if (rotx < 0)
						rotx += 360;

					float roty = myLoc.rotY;

					for (int i = 0; i < 10; i++)
								{
						
						if (roty > 60)
						{
							offy = -i;
						}
						else if (roty < -60)
						{
							offy = i;
						}
						else if ((rotx < 45) || (rotx >= 315))
						{
							offz = i;
						}
						else if ((rotx < 135) && (rotx >= 45))
						{
							offx = -i;
						}
						else if ((rotx < 225) && (rotx >- 135))
						{
							offz = -i;
						}
						else
						{
							offx = i;
						}
						int id = etc.getServer().getBlockIdAt(blockClicked.getX() + offx, blockClicked.getY() + offy, blockClicked.getZ() + offz);
						
						if (id != 54 && id != 7)
							etc.getServer().setBlockAt(0, blockClicked.getX() + offx, blockClicked.getY() + offy, blockClicked.getZ() + offz);
					}
				}
				return true;
			}
			return false;
			
		}
	}
}