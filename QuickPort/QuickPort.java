import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

public class QuickPort extends Plugin
{
	private QuickPortListener listener = new QuickPortListener();
	private Hashtable playerSettings = new Hashtable();

	public void enable()
	{
	}

	public void disable()
	{
	}
	
	public void initialize()
	{
		etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, listener, this, PluginListener.Priority.MEDIUM);
	}

	//Returns a QuickPortSettings for the player, making a new one if it has to
	public QuickPortSettings getSettings(Player player)
	{
		QuickPortSettings settings = (QuickPortSettings)playerSettings.get(player.getName());
		if (settings == null)
		{
			playerSettings.put(player.getName(), new QuickPortSettings());
			settings = (QuickPortSettings)playerSettings.get(player.getName());
		}

		return(settings);
	}
	
	public class QuickPortSettings
	{
		public Player targetPlayer;
		public boolean tunnelMode = false;
	}
	
	public class QuickPortListener extends PluginListener
	{

		public void onArmSwing(Player player) {
			if ((player.canUseCommand("/QuickPort")) && (player.getItemInHand() == 345))
			{
				QuickPortSettings settings = getSettings(player);

				//if(etc.getLoader().getPlugin("QuickPort") != null && etc.getLoader().getPlugin("QuickPort") instanceof QuickPort)
				//	player.sendMessage("QuickPort: test: " + (QuickPort)(etc.getLoader().getPlugin("QuickPort")).test);
				
				Location playerLoc;
				if (settings.targetPlayer == null)
					playerLoc = player.getLocation();
				else
					playerLoc = settings.targetPlayer.getLocation();
				
				if (!settings.tunnelMode)
				{
					HitBlox blox = new HitBlox(player, 300, 0.3);
					if (blox.getTargetBlock() != null)
					{
						for(int i = 0; i<100; i++)
						{
							int cur = etc.getServer().getBlockAt(blox.getCurBlock().getX(), blox.getCurBlock().getY() + i, blox.getCurBlock().getZ()).getType();
							int above = etc.getServer().getBlockAt(blox.getCurBlock().getX(), blox.getCurBlock().getY() + i + 1, blox.getCurBlock().getZ()).getType();
							if (cur == 0 && above == 0)
							{
								playerLoc.x = blox.getCurBlock().getX() + .5;
								playerLoc.y = blox.getCurBlock().getY() + i;
								playerLoc.z = blox.getCurBlock().getZ() + .5;
								
								if (settings.targetPlayer == null)
									player.teleportTo(playerLoc);
								else
									settings.targetPlayer.teleportTo(playerLoc);
									
								settings.targetPlayer = null;
								i = 100;
							}
						}
					}
				}
				else
				{
					HitBlox blox = new HitBlox(player, 300, 0.3);
					while ((blox.getNextBlock() != null) && ((blox.getCurBlock().getType() != 0) || ((blox.getLastBlock().getType() == 0))));
					if (blox.getCurBlock() != null)
					{
						for(int i = 0; i > -100; i--)
						{
							int below = etc.getServer().getBlockAt(blox.getCurBlock().getX(), blox.getCurBlock().getY() + i - 1, blox.getCurBlock().getZ()).getType();
							int cur = etc.getServer().getBlockAt(blox.getCurBlock().getX(), blox.getCurBlock().getY() + i, blox.getCurBlock().getZ()).getType();
							int above = etc.getServer().getBlockAt(blox.getCurBlock().getX(), blox.getCurBlock().getY() + i + 1, blox.getCurBlock().getZ()).getType();
							if (below != 0 && cur == 0 && above == 0)
							{
								playerLoc.x = blox.getCurBlock().getX() + .5;
								playerLoc.y = blox.getCurBlock().getY() + i;
								playerLoc.z = blox.getCurBlock().getZ() + .5;

								player.teleportTo(playerLoc);

								settings.targetPlayer = null;
								i = -100;
							}
						}
					}
				}

			}
		}
		
		//Toggles through players on server as targets and tunnel mode
		public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand)
		{
			if ((player.canUseCommand("/QuickPort")) && (itemInHand == 345))
			{
				QuickPortSettings settings = getSettings(player);
				List<Player> players = etc.getServer().getPlayerList();
				int index = -1;
				
				if (settings.targetPlayer == null)
						index = -1;
				else
					index = players.indexOf(settings.targetPlayer);
					
					
				if ((settings.targetPlayer == null) && (settings.tunnelMode))
					settings.tunnelMode = false;
				else if (((index + 1 >= players.size()) || (players.get(index + 1) == player && index + 2 >= players.size())) || !player.canUseCommand("/QuickPortOthers"))
				{
					settings.tunnelMode = true;
					settings.targetPlayer = null;
				}
				else if (players.get(index + 1) == player && index + 2 < players.size())
					settings.targetPlayer = players.get(index + 2);
				else
					settings.targetPlayer = players.get(index + 1);

				if (settings.targetPlayer == null && settings.tunnelMode)
					player.sendMessage("QuickPort Target: [Self] (Tunnel)");
				else if (settings.targetPlayer == null)
					player.sendMessage("QuickPort Target: [Self]");
				else
					player.sendMessage("QuickPort Target: " + settings.targetPlayer.getName());
						
						
			}
			return(false);
		}

	}

}
