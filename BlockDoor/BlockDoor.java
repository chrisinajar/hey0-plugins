import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;

import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Random;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockDoor extends Plugin
{
    private static Logger a = Logger.getLogger("Minecraft");
    public static enum Type { TOGGLE, ON, OFF, DISABLED }
	private Listener l = new Listener(this);
	private List<Door> doors = new ArrayList<Door>();
    private List<Trigger> triggers = new ArrayList<Trigger>();
	private List<RedTrig> redtrigs = new ArrayList<RedTrig>();
    private List<Zone> zones = new ArrayList<Zone>();
    private Whitelist whitelist = new Whitelist();
	private int max_size = -1;
    
	private Hashtable playerSettings = new Hashtable();
    public String doorsLoc = "blockdoors.txt";
    
    private boolean initialized = false;
    
    private class Whitelist
    {
        private List<String> whitelist = new ArrayList<String>();
        
        public Whitelist()
        {
            PropertiesFile properties = new PropertiesFile("server.properties");
            whitelist = Arrays.asList(properties.getString("bdwhitelist", "").split(","));
        }
        
        public boolean canUseType(String in_type)
        {
            boolean found = false;
            
            if (whitelist.size() > 0)
            {
                for(String s : whitelist)
                {
                    if (s.equalsIgnoreCase(in_type))
                        found = true;
                }
            }
            else
            {
                if (Integer.parseInt(in_type) < 7 && Integer.parseInt(in_type) >= 0)
                    found = true;
            }
                
            return found;
        }
    }
	
    private class Trigger
    {
        public String name;
        public String creator;
        
        public int trigger_x, trigger_y, trigger_z;
        public boolean coordsSet;
        
        public List<Link> links = new ArrayList<Link>();
        
        public Trigger(String in_name, String in_creator)
        {
            name = in_name;
			creator = in_creator;
            coordsSet = false;
        }
        
        public Trigger(String in_string)
		{
            String[] split = in_string.split(":");
            if (split.length == 8)
            {
                creator = split[1];
				name = split[2];
                
                
                
                trigger_x   = Integer.parseInt(split[3]);
                trigger_y   = Integer.parseInt(split[4]);
                trigger_z   = Integer.parseInt(split[5]);
                coordsSet   = Boolean.parseBoolean(split[6]);
				String[] linksplit = split[7].split("\\|");
				for(int i = 0; i < linksplit.length; i++)
				{
					Link l = new Link(linksplit[i]);
					if (l.t_creator != "FAILED")
					{
						links.add(l);
					}
				}
            }
            else
            {
                creator = "FAILED";
                coordsSet = false;
            }
		}
        
        public void processLinks()
        {
            for (Link l : links)
			{
				l.process();
			}
        }
        
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
			builder.append("TRIGGER:");
			builder.append(creator);
			builder.append(":");
            builder.append(name);
			builder.append(":");
            
            builder.append(Integer.toString(trigger_x));
            builder.append(":");
            builder.append(Integer.toString(trigger_y));
            builder.append(":");
            builder.append(Integer.toString(trigger_z));
            builder.append(":");
            
            builder.append(Boolean.toString(coordsSet));	
			builder.append(":");
			for (Link l : links)
			{
				builder.append(l.t_name);
				builder.append(" ");
				builder.append(l.t_creator);
				builder.append(" ");
				builder.append(l.type.name());
				builder.append("|");
			}
			return builder.toString();
        }
    }
    private class RedTrig
    {
        public String name;
        public String creator;
        
        public int trigger_x, trigger_y, trigger_z;
        public boolean coordsSet;
        
        public List<Link> links = new ArrayList<Link>();
        
        public RedTrig(String in_name, String in_creator)
        {
            name = in_name;
			creator = in_creator;
            coordsSet = false;
        }
        
        public RedTrig(String in_string)
		{
            String[] split = in_string.split(":");
            if (split.length == 8)
            {
                creator = split[1];
				name = split[2];
                
                
                
                trigger_x   = Integer.parseInt(split[3]);
                trigger_y   = Integer.parseInt(split[4]);
                trigger_z   = Integer.parseInt(split[5]);
                coordsSet   = Boolean.parseBoolean(split[6]);
				String[] linksplit = split[7].split("\\|");
				for(int i = 0; i < linksplit.length; i++)
				{
					Link l = new Link(linksplit[i]);
					if (l.t_creator != "FAILED")
					{
						links.add(l);
					}
				}
            }
            else
            {
                creator = "FAILED";
                coordsSet = false;
            }
		}
        
        public void processLinks()
        {
            for (Link l : links)
			{
				l.process();
			}
        }
        
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
			builder.append("REDTRIG:");
			builder.append(creator);
			builder.append(":");
            builder.append(name);
			builder.append(":");
            
            builder.append(Integer.toString(trigger_x));
            builder.append(":");
            builder.append(Integer.toString(trigger_y));
            builder.append(":");
            builder.append(Integer.toString(trigger_z));
            builder.append(":");
            
            builder.append(Boolean.toString(coordsSet));	
			builder.append(":");
			for (Link l : links)
			{
				builder.append(l.t_name);
				builder.append(" ");
				builder.append(l.t_creator);
				builder.append(" ");
				builder.append(l.type.name());
				builder.append("|");
			}
			return builder.toString();
        }
    }

        
    
    private class Zone
    {
        public String name;
        public String creator;
        public int occupants;
        
        public int t_start_x, t_start_y, t_start_z;
        public int t_end_x, t_end_y, t_end_z;
        public boolean coordsSet;
        
        public List<Link> links = new ArrayList<Link>();
        
        public Zone(String in_name, String in_creator)
        {
            name = in_name;
			creator = in_creator;
            coordsSet = false;
        }
        
		public Zone(String in_string)
		{
            String[] split = in_string.split(":");
            if (split.length == 12)
            {
                
                creator = split[1];
				name = split[2];
                
                
                
                t_start_x   = Integer.parseInt(split[3]);
                t_start_y   = Integer.parseInt(split[4]);
                t_start_z   = Integer.parseInt(split[5]);

                t_end_x     = Integer.parseInt(split[6]);
                t_end_y     = Integer.parseInt(split[7]);
                t_end_z     = Integer.parseInt(split[8]);
                coordsSet   = Boolean.parseBoolean(split[9]);
                occupants   = Integer.parseInt(split[10]);
				
				String[] linksplit = split[11].split("\\|");
				for(int i = 0; i < linksplit.length; i++)
				{
					Link l = new Link(linksplit[i]);
					if (l.t_creator != "FAILED")
						links.add(l);
				}				
				
            }
            else
            {
                creator = "FAILED";
                coordsSet = false;
            }
		}
        
        public void processLinks()
        {
			for (Link l : links)
			{
				l.process();
			}
  
        }

        
        
		public boolean isInZone(int x, int y, int z)
		{
			return ( 	t_start_x <= x && x <= t_end_x 
					&&	t_start_y <= y && y <= t_end_y
					&&	t_start_z <= z && z <= t_end_z );
		}
        
		public boolean isInZone(double x, double y, double z)
		{
			return ( 	t_start_x <= x && x <= t_end_x 
					&&	t_start_y <= y && y <= t_end_y
					&&	t_start_z <= z && z <= t_end_z );
		}
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
			builder.append("ZONE:");
			builder.append(creator);
			builder.append(":");
            builder.append(name);
			builder.append(":");
            
            builder.append(Integer.toString(t_start_x));
            builder.append(":");
            builder.append(Integer.toString(t_start_y));
            builder.append(":");
            builder.append(Integer.toString(t_start_z));
            builder.append(":");

            builder.append(Integer.toString(t_end_x));
            builder.append(":");
            builder.append(Integer.toString(t_end_y));
            builder.append(":");
            builder.append(Integer.toString(t_end_z));
            builder.append(":");
            
            builder.append(Boolean.toString(coordsSet));	
            builder.append(":");

            builder.append(Integer.toString(occupants));
			builder.append(":");
			for (Link l : links)
			{
				builder.append(l.t_name);
				builder.append(" ");
				builder.append(l.t_creator);
				builder.append(" ");
				builder.append(l.type.name());
				builder.append("|");
			}

			return builder.toString();
        }
    }
    
    private class Link
    {
        
        public String t_name;
		public String t_creator;
        public Type type;
        
        public Link(String in_target, String in_creator, String in_type)
        {
            t_name = in_target;
			t_creator = in_creator;
            
            if (in_type.equalsIgnoreCase("TOGGLE") || in_type.equalsIgnoreCase("T"))
                type = Type.TOGGLE;
            else if (in_type.equalsIgnoreCase("ON") || in_type.equalsIgnoreCase("OPEN"))
                type = Type.ON;
            else if (in_type.equalsIgnoreCase("OFF") || in_type.equalsIgnoreCase("CLOSE"))
                type = Type.OFF;
            else
                type = Type.DISABLED;
        }
		
        public Link(String in_string)
        {
			String[] split = in_string.split("\\s");
			if (split.length == 3)
			{
				t_name = split[0];
				t_creator = split[1];
				
				if (split[2].equalsIgnoreCase("TOGGLE") || split[2].equalsIgnoreCase("T"))
					type = Type.TOGGLE;
				else if (split[2].equalsIgnoreCase("ON") || split[2].equalsIgnoreCase("OPEN"))
					type = Type.ON;
				else if (split[2].equalsIgnoreCase("OFF") || split[2].equalsIgnoreCase("CLOSE"))
					type = Type.OFF;
				else
					type = Type.DISABLED;
			}
			else
				t_creator = "FAILED";
        }
		
		public void process()
		{
			int id = findDoor(t_name, t_creator);
			switch(type)
			{
				case TOGGLE:
					doors.get(id).toggle();
					break;
				case ON:
					doors.get(id).open();
					break;
				case OFF:
					doors.get(id).close();
					break;
				default:
					a.log(Level.INFO, "ERROR PARSING LINK!");
					break;
			}
		}
    }
    
	private class Door
	{
		public String name;
		public String creator;
        public boolean coordsSet;
	
		public int d_start_x, d_start_y, d_start_z;
		public int d_end_x, d_end_y, d_end_z;
		
		public int fill;
        public int empty;
		
		public boolean isOpen;
		
		public Door(String in_name, String in_creator)
		{
			name = in_name;
			creator = in_creator;
			
			d_start_x   = 0;
			d_start_y   = 0;
			d_start_z   = 0;

			d_end_x     = 0;
			d_end_y     = 0;
			d_end_z     = 0;
            
            coordsSet   = false;
  

			fill		= 0;
            empty       = 0;
			
			isOpen		= false;

		}
        
		public Door(String in_string)
		{
            String[] split = in_string.split(":");
            if (split.length == 13)
            {
                
                creator = split[1];
				name = split[2];
                
                d_start_x   = Integer.parseInt(split[3]);
                d_start_y   = Integer.parseInt(split[4]);
                d_start_z   = Integer.parseInt(split[5]);

                d_end_x     = Integer.parseInt(split[6]);
                d_end_y     = Integer.parseInt(split[7]);
                d_end_z     = Integer.parseInt(split[8]);
                coordsSet   = Boolean.parseBoolean(split[9]);

                fill		= Integer.parseInt(split[10]);
                empty		= Integer.parseInt(split[11]);
                
                isOpen		= Boolean.parseBoolean(split[12]);
            }
            else
            {
                creator = "FAILED";
                coordsSet = false;
            }
		}
		
		public void toggle()
		{
			if (isOpen)
			{
				isOpen = false;
				for (int i = d_start_x; i <= d_end_x; i++)
				 for (int j = d_start_y; j <= d_end_y; j++)
				  for (int k = d_start_z; k <= d_end_z; k++)
				  {
					etc.getServer().setBlockAt(fill, i, j, k);
				  }
			}
			else
			{
				isOpen = true;
				for (int i = d_start_x; i <= d_end_x; i++)
				 for (int j = d_start_y; j <= d_end_y; j++)
				  for (int k = d_start_z; k <= d_end_z; k++)
				  {
					etc.getServer().setBlockAt(empty, i, j, k);
				  }
			}
		}
		
		public void open()
		{
			if (!isOpen)
			{
				isOpen = true;
				for (int i = d_start_x; i <= d_end_x; i++)
				 for (int j = d_start_y; j <= d_end_y; j++)
				  for (int k = d_start_z; k <= d_end_z; k++)
				  {
					etc.getServer().setBlockAt(empty, i, j, k);
				  }
			}
		}
		
		public void close()
		{
			if (isOpen)
			{
				isOpen = false;
				for (int i = d_start_x; i <= d_end_x; i++)
				 for (int j = d_start_y; j <= d_end_y; j++)
				  for (int k = d_start_z; k <= d_end_z; k++)
				  {
					etc.getServer().setBlockAt(fill, i, j, k);
				  }
			}
		}
        
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
			builder.append("DOOR:");
			builder.append(creator);
			builder.append(":");
            builder.append(name);
			builder.append(":");
            
            builder.append(Integer.toString(d_start_x));
            builder.append(":");
            builder.append(Integer.toString(d_start_y));
            builder.append(":");
            builder.append(Integer.toString(d_start_z));
            builder.append(":");

            builder.append(Integer.toString(d_end_x));
            builder.append(":");
            builder.append(Integer.toString(d_end_y));
            builder.append(":");
            builder.append(Integer.toString(d_end_z));
            builder.append(":");
            
            builder.append(Boolean.toString(coordsSet));	
            builder.append(":");

            builder.append(Integer.toString(fill));
            builder.append(":");

            builder.append(Integer.toString(empty));
            builder.append(":");

            builder.append(Boolean.toString(isOpen));	

			return builder.toString();
        }
	}
	
	//Returns a Door for the player, making a new one if it has to
	public int findDoor(String in_name, String in_creator)
	{
		int index = -1;
		for (int i = 0; i < doors.size(); i++)
			if (doors.get(i).name.equals(in_name) && doors.get(i).creator.equals(in_creator))
				index = i;
		
		if (index == -1)
		{
			doors.add(new Door(in_name, in_creator));
			for (int i = 0; i < doors.size(); i++)
				if (doors.get(i).name.equals(in_name) && doors.get(i).creator.equals(in_creator))
					index = i;
		}
		
		return index;
	}
	
	public int findZone(String in_name, String in_creator)
	{
		int index = -1;
		for (int i = 0; i < zones.size(); i++)
			if (zones.get(i).name.equals(in_name) && zones.get(i).creator.equals(in_creator))
				index = i;
		
		if (index == -1)
		{
			zones.add(new Zone(in_name, in_creator));
			for (int i = 0; i < zones.size(); i++)
				if (zones.get(i).name.equals(in_name) && zones.get(i).creator.equals(in_creator))
					index = i;
		}
		
		return index;
	}
	
	public int findTrigger(String in_name, String in_creator)
	{
		int index = -1;
		for (int i = 0; i < triggers.size(); i++)
			if (triggers.get(i).name.equals(in_name) && triggers.get(i).creator.equals(in_creator))
				index = i;
		
		if (index == -1)
		{
			triggers.add(new Trigger(in_name, in_creator));
			for (int i = 0; i < triggers.size(); i++)
				if (triggers.get(i).name.equals(in_name) && triggers.get(i).creator.equals(in_creator))
					index = i;
		}
		
		return index;
	}
	
	public int findRedTrig(String in_name, String in_creator)
	{
		int index = -1;
		for (int i = 0; i < redtrigs.size(); i++)
			if (redtrigs.get(i).name.equals(in_name) && redtrigs.get(i).creator.equals(in_creator))
				index = i;
		
		if (index == -1)
		{
			redtrigs.add(new RedTrig(in_name, in_creator));
			for (int i = 0; i < redtrigs.size(); i++)
				if (redtrigs.get(i).name.equals(in_name) && redtrigs.get(i).creator.equals(in_creator))
					index = i;
		}
		
		return index;
	}
	
	private class BlockDoorSettings
	{
		public String command = "";
		public String name = "";
		public int select = 0;
		public int verbosity = 1;
	}
	
	//Returns a BlockDoorSettings for the player, making a new one if it has to
	public BlockDoorSettings getSettings(Player player)
	{
		BlockDoorSettings settings = (BlockDoorSettings)playerSettings.get(player.getName());
		if (settings == null)
		{
			playerSettings.put(player.getName(), new BlockDoorSettings());
			settings = (BlockDoorSettings)playerSettings.get(player.getName());
		}

		return(settings);
	}
	
	public void enable()
	{
	}

	public void disable()
	{
	}
	
	public void initialize()
	{
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, l, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.PLAYER_MOVE, l, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, l, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener(PluginLoader.Hook.BLOCK_DESTROYED, l, this, PluginListener.Priority.MEDIUM);
        
        initFile();
		
		PropertiesFile properties = new PropertiesFile("server.properties");
		max_size = properties.getInt("bdmaxsize", -1);

	}
    
	public void initFile()
	{
		if(initialized)
			return;
		initialized = true;
        	if (!(new File(doorsLoc).exists())) {
			FileWriter writer = null;
			try {
				writer = new FileWriter(doorsLoc);
				writer.write("#Don't edit this file\r\n");
            } catch (Exception e) {
				a.log(Level.SEVERE, "Exception while creating " + doorsLoc, e);
            } finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
					}
                }
			}
		}
		this.reloadFile();
	}
	
	private void deleteItem(String type, String creator, String name)
    {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(doorsLoc)));
			String line = "", text = "", newline = "";
			boolean foundIt = false;
            
			while ((line = reader.readLine()) != null) {
                if (!line.contains(type + ":" + creator + ":" + name)) //thanks vreon!
					text += line + "\r\n";
				else {
					if(!foundIt)
					{
						foundIt = true;
					}
				}
			}
            
			reader.close();
            
			FileWriter writer = new FileWriter(doorsLoc);
			writer.write(text);
			writer.close();
		} catch (Exception e1) {
			a.log(Level.SEVERE, "Exception while deleting objects in " + doorsLoc, e1);
		}
	}
	
	private void writeDoor(Door door)
    {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(doorsLoc)));
			String line = "", text = "", newline = "";
			boolean foundIt = false;
			
            newline = door.toString();
            
			while ((line = reader.readLine()) != null) {
				if (!line.contains("DOOR:" + door.creator + ":" + door.name))
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
            
			FileWriter writer = new FileWriter(doorsLoc);
			writer.write(text);
			writer.close();
		} catch (Exception e1) {
			a.log(Level.SEVERE, "Exception while editing waypoints in " + doorsLoc, e1);
		}
	}
    
	private void writeZone(Zone zone)
    {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(doorsLoc)));
			String line = "", text = "", newline = "";
			boolean foundIt = false;
			
            newline = zone.toString();
            
			while ((line = reader.readLine()) != null) {
				if (!line.contains("ZONE:" + zone.creator + ":" + zone.name))
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
            
			FileWriter writer = new FileWriter(doorsLoc);
			writer.write(text);
			writer.close();
		} catch (Exception e1) {
			a.log(Level.SEVERE, "Exception while editing waypoints in " + doorsLoc, e1);
		}
	}
    
	private void writeTrigger(Trigger trigger)
    {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(doorsLoc)));
			String line = "", text = "", newline = "";
			boolean foundIt = false;
			
            newline = trigger.toString();
            
			while ((line = reader.readLine()) != null) {
				if (!line.contains("TRIGGER:" + trigger.creator + ":" + trigger.name))
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
            
			FileWriter writer = new FileWriter(doorsLoc);
			writer.write(text);
			writer.close();
		} catch (Exception e1) {
			a.log(Level.SEVERE, "Exception while editing waypoints in " + doorsLoc, e1);
		}
	}
	
	private void writeRedTrig(RedTrig redtrig)
    {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(doorsLoc)));
			String line = "", text = "", newline = "";
			boolean foundIt = false;
			
            newline = redtrig.toString();
            
			while ((line = reader.readLine()) != null) {
				if (!line.contains("REDTRIG:" + redtrig.creator + ":" + redtrig.name))
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
            
			FileWriter writer = new FileWriter(doorsLoc);
			writer.write(text);
			writer.close();
		} catch (Exception e1) {
			a.log(Level.SEVERE, "Exception while editing waypoints in " + doorsLoc, e1);
		}
	}
	
	public void reloadFile()
    {
		if (new File(doorsLoc).exists()) {
			try {
                doors = new ArrayList<Door>();
				Scanner scanner = new Scanner(new File(doorsLoc));
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					if (line.startsWith("#") || line.length() == 0)
						continue;
                    if (line.startsWith("DOOR:"))
                    {
                        Door d = new Door(line);
                        if (d != null && d.creator != "FAILED")
                        {
                            a.log(Level.INFO, "Loading door: " + line);
                            doors.add(d);
                        }
                    }
                    else if (line.startsWith("ZONE:"))
                    {
                        Zone z = new Zone(line);
                        if (z != null && z.creator != "FAILED")
                        {
                            a.log(Level.INFO, "Loading zone: " + line);
                            zones.add(z);
                        }
                    }
                    else if (line.startsWith("TRIGGER:"))
                    {
                        Trigger t = new Trigger(line);
                        if (t != null && t.creator != "FAILED")
                        {
                            a.log(Level.INFO, "Loading trigger: " + line);
                            triggers.add(t);
                        }
                    }
                    else if (line.startsWith("REDTRIG:"))
                    {
                        RedTrig r = new RedTrig(line);
                        if (r != null && r.creator != "FAILED")
                        {
                            a.log(Level.INFO, "Loading redtrig: " + line);
                            redtrigs.add(r);
                        }
                    }
				}
			} catch (Exception e) {
				a.log(Level.SEVERE, "Exception while reading " + doorsLoc, e);
			}
		}
	}
    
    
    

	public class Listener extends PluginListener
    {
		BlockDoor plugin;
		Listener(BlockDoor p) {
			plugin = p;
		}
		
		public boolean onCommand(Player player, String[] split)
		{
			if ((player.canUseCommand("/blockdoor")))
			{
				if (split[0].equalsIgnoreCase("/ddoor") || split[0].equalsIgnoreCase("/blockdoor")) {
					if (split.length < 2)
					{
						player.sendMessage("Usage: /ddoor <doorname>");
					}
					else
					{
						BlockDoorSettings settings = getSettings(player);
						settings.command = "door";
						settings.name = split[1];
						settings.select = 1;
						if (settings.verbosity >= 1) { player.sendMessage("Creating door: '" + settings.name+ "'"); }
					}
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dtrig") || split[0].equalsIgnoreCase("/dtrigger")) {
					BlockDoorSettings settings = getSettings(player);
					settings.command = "trigger";
					if (split.length > 1)
						settings.name = split[1];
					settings.select = 1;
					if (settings.verbosity >= 1) { player.sendMessage("Creating trigger: '" + settings.name+ "'"); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dzero")) {
					BlockDoorSettings settings = getSettings(player);
					if (split.length > 1)
						settings.name = split[1];
					int id = findZone(settings.name, player.getName());
					zones.get(id).occupants = 0;
					
					writeZone(zones.get(id));
					
					if (settings.verbosity >= 1) { player.sendMessage("Zeroing: '" + settings.name + "'"); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dtoggle")) {
					BlockDoorSettings settings = getSettings(player);
					if (split.length > 1)
						settings.name = split[1];
					int id = findDoor(settings.name, player.getName());
					doors.get(id).toggle();
					
					writeDoor(doors.get(id));
					
					if (settings.verbosity >= 1) { player.sendMessage("Toggling: '" + settings.name + "'"); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dlink")) {
					BlockDoorSettings settings = getSettings(player);
					if (split.length < 5)
						player.sendMessage("Usage: /dlink <trigger/zone> <triggername> <doorname> <type>"); 
					else if (split[1].equalsIgnoreCase("trigger") || split[1].equalsIgnoreCase("t"))
					{
						int id = findTrigger(split[2], player.getName());
						triggers.get(id).links.add(new Link(split[3], player.getName(), split[4]));
						writeTrigger(triggers.get(id));
						if (settings.verbosity >= 1) { player.sendMessage("Added trigger link: " + split[3] + " - " + split[4]); }
					}
					else if (split[1].equalsIgnoreCase("zone") || split[1].equalsIgnoreCase("z"))
					{
						int id = findZone(split[2], player.getName());
						zones.get(id).links.add(new Link(split[3], player.getName(), split[4]));
						writeZone(zones.get(id));
						if (settings.verbosity >= 1) { player.sendMessage("Added zone link: " + split[3] + " - " + split[4]); }
					}

					return true;
				}
				else if (split[0].equalsIgnoreCase("/dfill")) {
					BlockDoorSettings settings = getSettings(player);
					int id = -1;

					if (split.length > 2)
					{
						id = findDoor(split[1], player.getName());
                        if(whitelist.canUseType(split[2]) || player.isAdmin())
                            doors.get(id).fill = Integer.parseInt(split[2]);
                        else
                        {
                            player.sendMessage("Fill type " + split[2] + " is not whitelisted.");
                            return true;
                        }
					}
					else
					{
						id = findDoor(settings.name, player.getName());
                        if(whitelist.canUseType(split[1]) || player.isAdmin())
                            doors.get(id).fill = Integer.parseInt(split[1]);
                        else
                        {
                            player.sendMessage("Fill type " + split[1] + " is not whitelisted.");     
                            return true;
                        }
					}
					writeDoor(doors.get(id));
					
					if (settings.verbosity >= 1) { player.sendMessage("Setting door (" + doors.get(id).name + ") fill type to: " + doors.get(id).fill); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dempty")) {
					BlockDoorSettings settings = getSettings(player);
					int id = -1;
					if (split.length > 2)
					{
						id = findDoor(split[1], player.getName());
                        if(whitelist.canUseType(split[2]) || player.isAdmin())
                            doors.get(id).empty = Integer.parseInt(split[2]);
                        else
                        {
                            player.sendMessage("Empty type " + split[2] + " is not whitelisted.");
                            return true;
                        }
					}
					else
					{
						id = findDoor(settings.name, player.getName());
                        if(whitelist.canUseType(split[1]) || player.isAdmin())
                            doors.get(id).empty = Integer.parseInt(split[1]);
                        else
                        {
                            player.sendMessage("Empty type " + split[1] + " is not whitelisted.");     
                            return true;
                        }
					}
					writeDoor(doors.get(id));
					
					if (settings.verbosity >= 1) { player.sendMessage("Setting door (" + doors.get(id).name + ") empty type to: " + doors.get(id).fill); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dzone")) {
					BlockDoorSettings settings = getSettings(player);
					settings.command = "zone";
					if (split.length > 1)
						settings.name = split[1];
					settings.select = 1;
					if (settings.verbosity >= 1) { player.sendMessage("Creating zone: '" + settings.name + "'"); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dlist")) {
					for (Door d : doors)
					{
						if (d.creator.equalsIgnoreCase(player.getName()))
							player.sendMessage("Listing Door: '" + d.creator + " : " + d.name + "'");
					}
					for (Trigger d : triggers)
					{
						if (d.creator.equalsIgnoreCase(player.getName()))
							player.sendMessage("Listing Trigger: '" + d.creator + " : " + d.name + "'");
					}
					for (Zone z : zones)
					{
						if (z.creator.equalsIgnoreCase(player.getName()))
							player.sendMessage("Listing Zone: '" + z.creator + " : " + z.name + "'");
					}
 
					if (split.length > 1)
						player.sendMessage("Index: '" + findDoor(split[1], player.getName()) + "'");
					
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dlistall")) {
					for (Door d : doors)
					{
						player.sendMessage("Listing Door: '" + d.creator + " : " + d.name + "'");
					}
					for (Trigger d : triggers)
					{
						player.sendMessage("Listing Trigger: '" + d.creator + " : " + d.name + "'");
					}
					for (Zone z : zones)
					{
						player.sendMessage("Listing Zone: '" + z.creator + " : " + z.name + "'");
					}
 
					if (split.length > 1)
						player.sendMessage("Index: '" + findDoor(split[1], player.getName()) + "'");
					
					return true;
				}
				else if (split[0].equalsIgnoreCase("/ddel")) {
					BlockDoorSettings settings = getSettings(player);
					if (split.length == 3)
					{
						if (split[1].equalsIgnoreCase("door") || split[1].equalsIgnoreCase("d"))
						{
							if (settings.verbosity >= 1) { player.sendMessage("Removing door: " + split[2]); }
							int id = findDoor(split[2], player.getName()); //Yes, I know if it doesn't exist it just adds it to delete it. 
							deleteItem("DOOR", doors.get(id).creator, doors.get(id).name);
							doors.remove(id);
						}
						else if (split[1].equalsIgnoreCase("trigger") || split[1].equalsIgnoreCase("t"))
						{
							if (settings.verbosity >= 1) { player.sendMessage("Removing trigger: " + split[2]); }
							int id = findTrigger(split[2], player.getName()); //Yes, I know if it doesn't exist it just adds it to delete it. 
							deleteItem("TRIGGER", triggers.get(id).creator, triggers.get(id).name);
							triggers.remove(id);
						}
						else if (split[1].equalsIgnoreCase("zone") || split[1].equalsIgnoreCase("z"))
						{
							if (settings.verbosity >= 1) { player.sendMessage("Removing zone: " + split[2]); }
							int id = findZone(split[2], player.getName()); //Yes, I know if it doesn't exist it just adds it to delete it. 
							deleteItem("DOOR", zones.get(id).creator, zones.get(id).name);
							zones.remove(id);
						}
					}
					else
					{
						player.sendMessage("Usage: /ddel <door/trigger/zone> <name>");
					}
					return true;
				}
			}
			return false;
		}
		
		public void onPlayerMove(Player player, Location from, Location to)
		{
			for (Zone z : zones)
			{
				if (z.coordsSet && z.isInZone(to.x, to.y, to.z) && !z.isInZone(from.x, from.y, from.z))
				{
					BlockDoorSettings settings = getSettings(player);
					//ENTERING ZONE
					if (settings.verbosity >= 3) { player.sendMessage("Entering zone: " + z.name + " (" + z.occupants + ")"); }
					if (z.occupants < 1)
					{
						z.processLinks();
						z.occupants = 1;
					}
					else
					{						
						z.occupants += 1;
					}
					
				}
				else if (z.coordsSet && !z.isInZone(to.x, to.y, to.z) && z.isInZone(from.x, from.y, from.z))
				{
					BlockDoorSettings settings = getSettings(player);
					//LEAVING ZONE
					if (settings.verbosity >= 3) { player.sendMessage("Leaving zone: " + z.name + " (" + z.occupants + ")"); }
					z.occupants -= 1;
					
					if (z.occupants < 1)
					{
						z.processLinks();
						z.occupants = 0;
					}
						
				}
			}
		}
		
        /*
		public boolean onBlockDestroy(Player player, Block block)
		{
			//player.sendMessage("Looking for triggers at: " + blockClicked.getX() + ", " + blockClicked.getY() + ", " + blockClicked.getZ());
			for (Door d : doors)
			{
				if (d.fill != 0 && d.t_x == block.getX() && d.t_y == block.getY() && d.t_z == block.getZ())
				{
					BlockDoorSettings settings = getSettings(player);
					if (settings.verbosity >= 2) { player.sendMessage("Toggling door: '" + d.name + "'"); }
					d.toggle();
				}
			}
			return false;
		}
		*/
		public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand) 
        {
            BlockDoorSettings settings = getSettings(player);
            if (settings.command == "")
            {
                //player.sendMessage("Looking for triggers at: " + blockClicked.getX() + ", " + blockClicked.getY() + ", " + blockClicked.getZ());
                for (Trigger t : triggers)
                {
                    if (t.coordsSet && t.trigger_x == blockClicked.getX() && t.trigger_y == blockClicked.getY() && t.trigger_z == blockClicked.getZ())
                    {
                        if (settings.verbosity >= 2) { player.sendMessage("Toggling trigger: '" + t.name + "'"); }
                        t.processLinks();
                    }
                }
            }
            else if (settings.command == "door")
            {
                int id = findDoor(settings.name, player.getName());

                if (settings.select == 1)
                {
                    if (settings.verbosity >= 1) { player.sendMessage("Setting door start: '" + settings.name + "'"); }
                    doors.get(id).d_start_x = blockClicked.getX();
                    doors.get(id).d_start_y = blockClicked.getY();
                    doors.get(id).d_start_z = blockClicked.getZ();
                    settings.select = 2;
                }
                else if (settings.select == 2)
                {
                    if (settings.verbosity >= 1) { player.sendMessage("Setting door end: '" + settings.name + "'"); }
                    //Checking to make sure start > end to make for loops easier later
                    if (doors.get(id).d_start_x > blockClicked.getX())
                    {
                        doors.get(id).d_end_x = doors.get(id).d_start_x;
                        doors.get(id).d_start_x = blockClicked.getX();
                    }
                    else
                    {
                        doors.get(id).d_end_x = blockClicked.getX();
                    }
                    
                    if (doors.get(id).d_start_y > blockClicked.getY())
                    {
                        doors.get(id).d_end_y = doors.get(id).d_start_y;
                        doors.get(id).d_start_y = blockClicked.getY();
                    }
                    else
                    {
                        doors.get(id).d_end_y = blockClicked.getY();
                    }
                    
                    if (doors.get(id).d_start_z > blockClicked.getZ())
                    {
                        doors.get(id).d_end_z = doors.get(id).d_start_z;
                        doors.get(id).d_start_z = blockClicked.getZ();
                    }
                    else
                    {
                        doors.get(id).d_end_z = blockClicked.getZ();
                    }
                    
                    if (doors.get(id).fill == 0)
                        doors.get(id).fill = 1;
                    
					if (max_size != -1 && ((doors.get(id).d_end_x - doors.get(id).d_start_x > max_size) ||
						(doors.get(id).d_end_y - doors.get(id).d_start_y > max_size) ||
						(doors.get(id).d_end_z - doors.get(id).d_start_z > max_size)))
					{
						if (settings.verbosity >= 1) { player.sendMessage("DOOR DIMENSIONS REJECTED for: '" + settings.name + "'"); }
						doors.get(id).coordsSet = false;
					}
					else
					{
						doors.get(id).coordsSet = true;
                    }
					writeDoor(doors.get(id));
                    settings.select = 0;
                    settings.command = "";
                }
                
            }
            else if (settings.command == "trigger")
            {
                if (settings.verbosity >= 1) { player.sendMessage("Setting door trigger: " + settings.name); }
                int id = findTrigger(settings.name, player.getName());
                triggers.get(id).trigger_x = blockClicked.getX();
                triggers.get(id).trigger_y = blockClicked.getY();
                triggers.get(id).trigger_z = blockClicked.getZ();
                triggers.get(id).coordsSet = true;
                writeTrigger(triggers.get(id));
                
				
                settings.command = "";
                settings.select = 0;
            }
            else if (settings.command == "zone")
            {
                int id = findZone(settings.name, player.getName());

                if (settings.select == 1)
                {
                    if (settings.verbosity >= 1) { player.sendMessage("Setting zone start: " + settings.name); }
                    zones.get(id).t_start_x = blockClicked.getX();
                    zones.get(id).t_start_y = blockClicked.getY();
                    zones.get(id).t_start_z = blockClicked.getZ();
                    
                    settings.select = 2;
                }
                else if (settings.select == 2)
                {
                    if (settings.verbosity >= 1) { player.sendMessage("Setting zone end: '" + settings.name + "'"); }
                    //Checking to make sure start > end to make for loops easier later
                    if (zones.get(id).t_start_x > blockClicked.getX())
                    {
                        zones.get(id).t_end_x = zones.get(id).t_start_x;
                        zones.get(id).t_start_x = blockClicked.getX();
                    }
                    else
                    {
                        zones.get(id).t_end_x = blockClicked.getX();
                    }
                    
                    if (zones.get(id).t_start_y > blockClicked.getY())
                    {
                        zones.get(id).t_end_y = zones.get(id).t_start_y;
                        zones.get(id).t_start_y = blockClicked.getY();
                    }
                    else
                    {
                        zones.get(id).t_end_y = blockClicked.getY();
                    }
                    
                    if (zones.get(id).t_start_z > blockClicked.getZ())
                    {
                        zones.get(id).t_end_z = zones.get(id).t_start_z;
                        zones.get(id).t_start_z = blockClicked.getZ();
                    }
                    else
                    {
                        zones.get(id).t_end_z = blockClicked.getZ();
                    }
                    
					if (max_size != -1 && ((zones.get(id).t_end_x - zones.get(id).t_start_x > max_size) ||
						(zones.get(id).t_end_y - zones.get(id).t_start_y > max_size) ||
						(zones.get(id).t_end_z - zones.get(id).t_start_z > max_size)))
					{
						if (settings.verbosity >= 1) { player.sendMessage("ZONE DIMENSIONS REJECTED for: '" + settings.name + "'"); }
						zones.get(id).coordsSet = false;
					}
					else
					{
						zones.get(id).occupants = 0;
						zones.get(id).coordsSet = true;
                    }
					writeZone(zones.get(id));
                    settings.select = 0;
                    settings.command = "";
                }
                
            }

            return(false);
        }
	}
}
