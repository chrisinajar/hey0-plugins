import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WaypointPlugin extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	private Dictionary<String, User> users = new Hashtable<String, User>();
	private boolean initialized = false;
	public String wpLoc = "waypoints.txt";
	private WaypointListener l = new WaypointListener(this);

        public void enable()
        {
                JarPlugins.checkUpdatrFile(getClass().getName(), "1.3.0");
                etc.getInstance().addCommand("/wp", "(Player) [wp name] - Teleport to Player's wp. Only name is required.");
                etc.getInstance().addCommand("/setwp", "[name] - Create or update waypoint here.");
                etc.getInstance().addCommand("/rmwp", "[name] - Delete named waypoint.");
                etc.getInstance().addCommand("/listwp", "(Player) - List your, or Player's is specified, waypoints.");
        }

	public class Waypoint {
		public String name;
		public Location location = new Location();
		public Waypoint(String wp)
		{
			String[] locs = wp.split(" ");
			if(locs.length == 3)
			{
				try {
					location.x = Double.parseDouble(locs[0]);
					location.y = Double.parseDouble(locs[1]);
					location.z = Double.parseDouble(locs[2]);
					location.rotX = 0.0F;
					location.rotY = 0.0F;
				} catch (NumberFormatException e) {
					Logger.getLogger("Minecraft").log(Level.SEVERE, "Exception parsing waypoint", e);
				}
			} else if(locs.length == 4)
			{
				try {
					name = locs[0];
					location.x = Double.parseDouble(locs[1]);
					location.y = Double.parseDouble(locs[2]);
					location.z = Double.parseDouble(locs[3]);
					location.rotX = 0.0F;
					location.rotY = 0.0F;
				} catch (NumberFormatException e) {
					Logger.getLogger("Minecraft").log(Level.SEVERE, "Exception parsing waypoint", e);
				}
			}
		}
		public String toString()
		{
			try {
				StringBuilder builder = new StringBuilder();
				if(name != "home")
				{
					builder.append(name);
					builder.append(" ");
				}
				builder.append(location.x);
				builder.append(" ");
				builder.append(location.y);
				builder.append(" ");
				builder.append(location.z);
				return builder.toString();
			} catch (Exception e2) {
				a.log(Level.SEVERE, "Exception while building waypoint string", e2);
			}
			return "";
		}
	}

	public class User {
		public Dictionary<String, Waypoint> waypoints = new Hashtable<String, Waypoint>();
		public Waypoint spawnPoint = new Waypoint("");
		public User()
		{
			this.spawnPoint.name = "home";
			this.spawnPoint.location = etc.getServer().getSpawnLocation();
		}
	}

	public WaypointPlugin()
	{
		setName("Waypoints by chrisinajar");
	}

	public void initialize()
	{
		 etc.getLoader().addListener(PluginLoader.Hook.COMMAND, l, this, PluginListener.Priority.MEDIUM);
	}

	public void init()
	{
		if(initialized)
			return;
		initialized = true;
        	if (!(new File(wpLoc).exists())) {
			FileWriter writer = null;
			try {
				writer = new FileWriter(wpLoc);
				writer.write("#Don't edit this file\r\n");
	                } catch (Exception e) {
				a.log(Level.SEVERE, "Exception while creating " + wpLoc, e);
                	} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
					}
                		}
			}
		}
		this.reload();
	}

	public void disable()
	{
		etc.getInstance().removeCommand("/wp");
		etc.getInstance().removeCommand("/setwp");
		etc.getInstance().removeCommand("/rmwp");
		etc.getInstance().removeCommand("/listwp");
	}

	public class WaypointListener extends PluginListener
	{
	WaypointPlugin plugin;
	WaypointListener(WaypointPlugin pl)
	{
		plugin = pl;
	}
	public boolean onCommand(Player e, String[] split)
	{
		plugin.init();
		try {
			if(!e.canUseCommand(split[0]))
				return false;
			if (split[0].equalsIgnoreCase("/setwp")) {
				if (split.length < 2) {
					e.sendMessage("Correct usage is: /setwp [name]");
					return true;
				}
				Player player = e;
				if (split.length > 2) {
					if (e.canUseCommand("/setwpother"))
					{
						e.sendMessage("You do not have access to set waypoints for other players.");
						return true;
					}
				}
				plugin.setWaypoint(e, split[1]);
			} else if (split[0].equalsIgnoreCase("/wp")) {
				if (split.length < 2) {
					if(e.canUseCommand("/wpother"))
						e.sendMessage("Correct usage is: /wp (player) [name] -- only name is required");
					else
						e.sendMessage("Correct usage is: /wp [name]");
					return true;
				}
				String wpname = split[1];
				String player = e.getName();
				if (split.length > 2) {
					if(e.canUseCommand("/wpother"))
						player = split[1];
					wpname = split[2];
				}
				Waypoint wp = plugin.getWaypoint(player, wpname);
				if(wp == null)
				{
					e.sendMessage("Failed to find a waypoint by that name");
					return true;
				}
				
				a.info(e.getName() + " used " + player + "'s wp");
				e.teleportTo(wp.location);
			} else if (split[0].equalsIgnoreCase("/listwp")) {
				String player = e.getName();
				if (split.length > 1) {
					if (e.canUseCommand("/listwpother")) {
						player = split[1];
					}
					else
					{
						e.sendMessage("You cannot list other player's Waypoints");
						return true;
					}
				}
				Player p = etc.getServer().matchPlayer(player);
				if (p != null) {
					player = p.getName();
				}
				e.sendMessage("Waypoints: " + plugin.listWaypoints(player));
			} else if (split[0].equalsIgnoreCase("/rmwp")) {
				if (split.length < 2) {
					e.sendMessage("Correct usage is: /rmwp [name]");
					return true;
				}
				if(plugin.removeWaypoint(e, split[1]))
					e.sendMessage("Waypoint removed.");
				else
					e.sendMessage("No such waypoint: " + split[1]);
			} else {
				return false;
			}
		} catch (Exception ex) {
			a.log(Level.SEVERE, "Exception in command handler (Report this to chrisinajar):", ex);
			return false;
		}
		return true;
	}
	}

	public static String combineSplit(int startIndex, String[] string, String seperator) {
		StringBuilder builder = new StringBuilder();
		for (int i = startIndex; i < string.length; i++)
			builder.append(string[i] + seperator);
		builder.deleteCharAt(builder.length() - 1); // remove the extra
		// seperator
		return builder.toString();
	}

	public String getPlayerName(String name) {
		for (Enumeration i = users.keys(); i.hasMoreElements();)
		{
			String pname = i.nextElement().toString();
			if(name.equalsIgnoreCase(pname))
				return pname;
		}
		Player p = etc.getServer().matchPlayer(name);
		if (p == null)
			return name;
		return p.getName();
	}

	public String listWaypoints(String player)
	{
		player = getPlayerName(player);
		StringBuilder builder = new StringBuilder();
		try {
			for (Enumeration i = users.get(player).waypoints.keys(); i.hasMoreElements();)
			{
				builder.append(i.nextElement().toString());
				if(i.hasMoreElements())
					builder.append(", ");
			}
		} catch (Exception e1) {
		}
		return builder.toString();
	}

	public Waypoint getWaypoint(String player, String name)
	{
		player = getPlayerName(player);
		try {
			return users.get(player).waypoints.get(name);
		} catch (Exception e1) {
		}
		return null;
	}
	
	public String setWaypoint(String e, String name) {
		User user = null;
		try {
			user = users.get(e);
		} catch (Exception e1) {
		}
		
		if (user == null) {
			// User doesn't exist, add it to the array.
			try {
				user = new User();
				users.put(e, user);
			} catch (Exception e2) {
				a.log(Level.SEVERE, "Exception while adding user to array", e2);
				return;
			}
		}
		Waypoint wp = null;
		try {
			wp = user.waypoints.get(name);
		} catch (Exception e1) {
		}
		if(wp == null)
		{
			wp = new Waypoint("");
			user.waypoints.put(name, wp);
			wp.name = name;
			wp.location = e.getLocation();
			writeWaypoint(user);
			e.sendMessage("Created new waypoint called: " + name);
		}
		else
			e.sendMessage("Updated location for waypoint: " + name);
		writeWaypoint(user);
	}
	
	public boolean removeWaypoint(Player e, String name) {
		User user = null;
		try {
			user = users.get(e.getName());
		} catch (Exception e1) {
		}
		
		if (user == null) {
			// User doesn't exist, add it to the array.
			try {
				user = new User();
				users.put(e.getName(), user);
			} catch (Exception e2) {
				a.log(Level.SEVERE, "Exception while adding user to array", e2);
				return false;
			}
		}
		try {
			if(user.waypoints.get(name) == null)
				return false;
			user.waypoints.remove(name);
		} catch (Exception e1) {
			return false;
		}
		writeWaypoint(e, user);
		return true;
	}

	private void writeWaypoint(Player e, User user) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(wpLoc)));
			String line = "", text = "", newline = "";
			boolean foundIt = false;
			StringBuilder builder = new StringBuilder();
			builder.append(e.getName());
			builder.append(":");
			builder.append(user.spawnPoint.toString());
			builder.append(":");
			for (Enumeration i = user.waypoints.elements(); i.hasMoreElements();)
			{
				builder.append(i.nextElement().toString());
				builder.append(":");
			}
			newline = builder.toString();
			while ((line = reader.readLine()) != null) {
				if (!line.contains(e.getName() + ":"))
					text += line + "\r\n";
				else {
					if(!foundIt)
					{
						foundIt = true;
						text += newline + "\r\n";
					}
				}
			}
			if(!foundIt)
				text += newline + "\r\n";
			reader.close();
			
			FileWriter writer = new FileWriter(wpLoc);
			writer.write(text);
			writer.close();
		} catch (Exception e1) {
			a.log(Level.SEVERE, "Exception while editing waypoints in " + wpLoc, e);
		}
	}
	
	public void reload() {
		if (new File(wpLoc).exists()) {
			try {
				Scanner scanner = new Scanner(new File(wpLoc));
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					if (line.startsWith("#") || line.length() == 0)
						continue;
					String[] split = line.split(":");
					if(split.length < 2)
						continue;
					String name = split[0];
					User user = null;
					try {
						user = users.get(name);
					} catch (Exception e1) {
						// do nothing!
					}
					
					if (user == null) {
						// User doesn't exist, add it to the array.
						try {
							user = new User();
							users.put(name, user);
						} catch (Exception e2) {
							a.log(Level.SEVERE, "Exception while adding user to array", e2);
						}
					}
					a.info("Loading in waypoints for: " + name);
					user.waypoints = new Hashtable<String, Waypoint>();
					if (split[1].length() > 5)
					{
						// they have a custom spawn point!
						user.spawnPoint = new Waypoint(split[1]);
						user.spawnPoint.name = "home";
						a.info("Loading in spawn point: " + split[1]);
					}
					for(int i = 2; i < split.length; i++)
					{
						Waypoint wp = new Waypoint(split[i]);
						user.waypoints.put(wp.name, wp);
						a.info("Loading in waypoint: " + split[i]);
					}
				}
			} catch (Exception e) {
				a.log(Level.SEVERE, "Exception while reading " + wpLoc, e);
			}
		}
	}
}

