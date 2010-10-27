import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;

import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockDoor extends Plugin
{
    private static Logger a = Logger.getLogger("Minecraft");
	private Listener l = new Listener(this);
	private List<Door> doors = new ArrayList<Door>();
	private Hashtable playerSettings = new Hashtable();
    public String doorsLoc = "blockdoors.txt";
    
    private boolean initialized = false;
	
	private class Door
	{
		public String name;
		public String creator;
	
		public int d_start_x, d_start_y, d_start_z;
		public int d_end_x, d_end_y, d_end_z;
		
		public int t_start_x, t_start_y, t_start_z;
		public int t_end_x, t_end_y, t_end_z;

		public int t_x, t_y, t_z;
		
		public int type;
		public int occupants;
		
		public boolean zoneTrigger;
		
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
  
			t_start_x   = 0;
			t_start_y   = 0;
			t_start_z   = 0;
			t_end_x     = 0;
			t_end_y     = 0;
			t_end_z     = 0;

			t_x         = 0;
			t_y         = 0;
			t_z         = 0;

			type		= 0;
			
			occupants   = 0;
			
			isOpen		= false;
			zoneTrigger	= false;
		}
        
		public Door(String in_string)
		{
            String[] split = in_string.split(":");
            if (split.length == 21)
            {
                
                creator = split[0];
				name = split[1];
                
                d_start_x   = Integer.parseInt(split[2]);
                d_start_y   = Integer.parseInt(split[3]);
                d_start_z   = Integer.parseInt(split[4]);

                d_end_x     = Integer.parseInt(split[5]);
                d_end_y     = Integer.parseInt(split[6]);
                d_end_z     = Integer.parseInt(split[7]);
      
                t_start_x   = Integer.parseInt(split[8]);
                t_start_y   = Integer.parseInt(split[9]);
                t_start_z   = Integer.parseInt(split[10]);
                t_end_x     = Integer.parseInt(split[11]);
                t_end_y     = Integer.parseInt(split[12]);
                t_end_z     = Integer.parseInt(split[13]);

                t_x         = Integer.parseInt(split[14]);
                t_y         = Integer.parseInt(split[15]);
                t_z         = Integer.parseInt(split[16]);

                type		= Integer.parseInt(split[17]);
                
                occupants   = 0; //No reason to store, presently.
                
                isOpen		= Boolean.parseBoolean(split[19]);
                zoneTrigger	= Boolean.parseBoolean(split[20]);
            }
            else
            {
                type = -99;
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
					etc.getServer().setBlockAt(type, i, j, k);
				  }
			}
			else
			{
				isOpen = true;
				for (int i = d_start_x; i <= d_end_x; i++)
				 for (int j = d_start_y; j <= d_end_y; j++)
				  for (int k = d_start_z; k <= d_end_z; k++)
				  {
					etc.getServer().setBlockAt(0, i, j, k);
				  }
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

            builder.append(Integer.toString(t_x));
            builder.append(":");
            builder.append(Integer.toString(t_y));
            builder.append(":");
            builder.append(Integer.toString(t_z));
            builder.append(":");

            builder.append(Integer.toString(type));
            builder.append(":");

            builder.append(Integer.toString(occupants));
            builder.append(":");

            builder.append(Boolean.toString(isOpen));	
            builder.append(":");
            builder.append(Boolean.toString(zoneTrigger));

			return builder.toString();
        }
	}
	
	//Returns a Door for the player, making a new one if it has to
	public int findDoor(String in_name, String in_creator)
	{
		int index = -1;
		for (int i = 0; i < doors.size(); i++)
		{
			if (doors.get(i).name.equals(in_name) && doors.get(i).creator.equals(in_creator))
			{
				index = i;
			}
		}
		
		if (index == -1)
		{
			doors.add(new Door(in_name, in_creator));
            
			for (int i = 0; i < doors.size(); i++)
			{
				if (doors.get(i).name.equals(in_name) && doors.get(i).creator.equals(in_creator))
				{
					index = i;
				}
			}
		}
		
		return index;
	}
	
	private class BlockDoorSettings
	{
		public String command = "";
		public String door = "";
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
		this.reloadDoors();
	}
	
	private void deleteDoor(Door door) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(doorsLoc)));
			String line = "", text = "", newline = "";
			boolean foundIt = false;
			
            newline = door.toString();
            
			while ((line = reader.readLine()) != null) {
				if (!line.contains(door.creator + ":" + door.name))
					text += line + "\r\n";
				else {
					if(!foundIt)
					{
						foundIt = true;
						//text += "\r\n";
					}
				}
			}
            
			//if(!foundIt)
			//	text += newline + "\r\n";
			reader.close();
            
			FileWriter writer = new FileWriter(doorsLoc);
			writer.write(text);
			writer.close();
		} catch (Exception e1) {
			a.log(Level.SEVERE, "Exception while deleting doors in " + doorsLoc, e1);
		}
	}
	
	private void writeDoor(Door door) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(doorsLoc)));
			String line = "", text = "", newline = "";
			boolean foundIt = false;
			
            newline = door.toString();
            
			while ((line = reader.readLine()) != null) {
				if (!line.contains(door.creator + ":" + door.name))
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
	
	public void reloadDoors() {
		if (new File(doorsLoc).exists()) {
			try {
                doors = new ArrayList<Door>();
				Scanner scanner = new Scanner(new File(doorsLoc));
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					if (line.startsWith("#") || line.length() == 0)
						continue;
					Door d = new Door(line);
                    if (d != null && d.type != -99)
                    {
						a.log(Level.INFO, "Loading door: " + line);
                        doors.add(d);
                    }
				}
			} catch (Exception e) {
				a.log(Level.SEVERE, "Exception while reading " + doorsLoc, e);
			}
		}
	}
    
    
    

	public class Listener extends PluginListener {
		BlockDoor plugin;
		Listener(BlockDoor p) {
			plugin = p;
		}
		
		public boolean onCommand(Player player, String[] split)
		{
			if ((player.canUseCommand("/blockdoor")))
			{
				if (split[0].equalsIgnoreCase("/ddoor") || split[0].equalsIgnoreCase("/blockdoor")) {
					BlockDoorSettings settings = getSettings(player);
					settings.command = "door";
					settings.door = split[1];
					settings.select = 1;
					if (settings.verbosity >= 1) { player.sendMessage("Creating door: '" + settings.door+ "'"); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dtrig") || split[0].equalsIgnoreCase("/dtrigger")) {
					BlockDoorSettings settings = getSettings(player);
					settings.command = "trigger";
					if (split.length > 1)
						settings.door = split[1];
					settings.select = 1;
					if (settings.verbosity >= 1) { player.sendMessage("Creating trigger: '" + settings.door+ "'"); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dzero")) {
					BlockDoorSettings settings = getSettings(player);
					if (split.length > 1)
						settings.door = split[1];
					int id = findDoor(settings.door, player.getName());
					doors.get(id).occupants = 0;
					
					writeDoor(doors.get(id));
					
					if (settings.verbosity >= 1) { player.sendMessage("Zeroing: '" + settings.door + "'"); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dtoggle")) {
					BlockDoorSettings settings = getSettings(player);
					if (split.length > 1)
						settings.door = split[1];
					int id = findDoor(settings.door, player.getName());
					doors.get(id).toggle();
					
					writeDoor(doors.get(id));
					
					if (settings.verbosity >= 1) { player.sendMessage("Toggling: '" + settings.door + "'"); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dtype") || split[0].equalsIgnoreCase("/dfill")) {
					BlockDoorSettings settings = getSettings(player);
					int id = -1;
					if (split.length > 2)
					{
						id = findDoor(split[1], player.getName());
						doors.get(id).type = Integer.parseInt(split[2]);
					}
					else
					{
						id = findDoor(settings.door, player.getName());
						doors.get(id).type = Integer.parseInt(split[1]);
					}
					writeDoor(doors.get(id));
					
					if (settings.verbosity >= 1) { player.sendMessage("Setting door (" + doors.get(id).name + ") type to: " + doors.get(id).type); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dzone")) {
					BlockDoorSettings settings = getSettings(player);
					settings.command = "zone";
					if (split.length > 1)
						settings.door = split[1];
					settings.select = 1;
					if (settings.verbosity >= 1) { player.sendMessage("Creating zone: '" + settings.door + "'"); }
					return true;
				}
				else if (split[0].equalsIgnoreCase("/dlist")) {
					for (Door d : doors)
					{
						player.sendMessage("Listing: '" + d.name + "'");
					}
					player.sendMessage("Size: '" + doors.size() + "'");
					if (split.length > 1)
						player.sendMessage("Index: '" + findDoor(split[1], player.getName()) + "'");
					
					return true;
				}
				else if (split[0].equalsIgnoreCase("/ddel")) {
					BlockDoorSettings settings = getSettings(player);
					if (split.length > 1)
					{
						if (settings.verbosity >= 1) { player.sendMessage("Removing door: " + split[1]); }
						int id = findDoor(split[1], player.getName()); //Yes, I know if it doesn't exist it just adds it to delete it. 
						deleteDoor(doors.get(id));
						doors.remove(id);
					}
					else
					{
						player.sendMessage("Please speicify a door.");
					}
					return true;
				}
			}
			return false;
		}
		
		public void onPlayerMove(Player player, Location from, Location to)
		{
			for (Door d : doors)
			{
				if (d.zoneTrigger && d.type != 0 && d.isInZone(to.x, to.y, to.z) && !d.isInZone(from.x, from.y, from.z))
				{
					BlockDoorSettings settings = getSettings(player);
					//ENTERING ZONE
					if (settings.verbosity >= 3) { player.sendMessage("Entering zone: " + d.name + " (" + d.occupants + ")"); }
					if (d.occupants < 1)
					{
						d.toggle();
						d.occupants = 1;
					}
					else
					{						
						d.occupants += 1;
					}
					
				}
				else if (d.zoneTrigger && d.type != 0 && !d.isInZone(to.x, to.y, to.z) && d.isInZone(from.x, from.y, from.z))
				{
					BlockDoorSettings settings = getSettings(player);
					//LEAVING ZONE
					if (settings.verbosity >= 3) { player.sendMessage("Leaving zone: " + d.name + " (" + d.occupants + ")"); }
					d.occupants -= 1;
					
					if (d.occupants < 1)
					{
						d.toggle();
						d.occupants = 0;
					}
						
				}
			}
		}
		
		public boolean onBlockDestroy(Player player, Block block)
		{
			//player.sendMessage("Looking for triggers at: " + blockClicked.getX() + ", " + blockClicked.getY() + ", " + blockClicked.getZ());
			for (Door d : doors)
			{
				if (d.type != 0 && d.t_x == block.getX() && d.t_y == block.getY() && d.t_z == block.getZ())
				{
					BlockDoorSettings settings = getSettings(player);
					if (settings.verbosity >= 2) { player.sendMessage("Toggling door: '" + d.name + "'"); }
					d.toggle();
				}
			}
			return false;
		}
		
		public boolean onBlockCreate(Player player, Block blockPlaced, Block blockClicked, int itemInHand)
        {
           // if ((player.canUseCommand("/door")))
           // {
                BlockDoorSettings settings = getSettings(player);
				if (settings.command == "")
				{
					//player.sendMessage("Looking for triggers at: " + blockClicked.getX() + ", " + blockClicked.getY() + ", " + blockClicked.getZ());
					for (Door d : doors)
					{
						if (d.type != 0 && d.t_x == blockClicked.getX() && d.t_y == blockClicked.getY() && d.t_z == blockClicked.getZ())
						{
							if (settings.verbosity >= 2) { player.sendMessage("Toggling door: '" + d.name + "'"); }
							d.toggle();
						}
					}
				}
				else if (settings.command == "door")
				{
					int id = findDoor(settings.door, player.getName());

					if (settings.select == 1)
					{
						if (settings.verbosity >= 1) { player.sendMessage("Setting door start: '" + settings.door + "'"); }
						doors.get(id).d_start_x = blockClicked.getX();
						doors.get(id).d_start_y = blockClicked.getY();
						doors.get(id).d_start_z = blockClicked.getZ();
						settings.select = 2;
					}
					else if (settings.select == 2)
					{
						if (settings.verbosity >= 1) { player.sendMessage("Setting door end: '" + settings.door + "'"); }
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
						
						if (doors.get(id).type == 0)
							doors.get(id).type = 1;
						
                        writeDoor(doors.get(id));
                        
						settings.select = 0;
						settings.command = "";
					}
					
				}
				else if (settings.command == "trigger")
				{
					if (settings.verbosity >= 1) { player.sendMessage("Setting door trigger: '" + settings.door + "'"); }
					int id = findDoor(settings.door, player.getName());
					doors.get(id).t_x = blockClicked.getX();
					doors.get(id).t_y = blockClicked.getY();
					doors.get(id).t_z = blockClicked.getZ();
					
                    writeDoor(doors.get(id));
                    
					settings.command = "";
					settings.select = 0;
				}
				else if (settings.command == "zone")
				{
					int id = findDoor(settings.door, player.getName());

					if (settings.select == 1)
					{
						if (settings.verbosity >= 1) { player.sendMessage("Setting zone start: '" + settings.door + "'"); }
						doors.get(id).t_start_x = blockClicked.getX();
						doors.get(id).t_start_y = blockClicked.getY();
						doors.get(id).t_start_z = blockClicked.getZ();
                        
						settings.select = 2;
					}
					else if (settings.select == 2)
					{
						if (settings.verbosity >= 1) { player.sendMessage("Setting zone end: '" + settings.door + "'"); }
						//Checking to make sure start > end to make for loops easier later
						if (doors.get(id).t_start_x > blockClicked.getX())
						{
							doors.get(id).t_end_x = doors.get(id).t_start_x;
							doors.get(id).t_start_x = blockClicked.getX();
						}
						else
						{
							doors.get(id).t_end_x = blockClicked.getX();
						}
						
						if (doors.get(id).t_start_y > blockClicked.getY())
						{
							doors.get(id).t_end_y = doors.get(id).t_start_y;
							doors.get(id).t_start_y = blockClicked.getY();
						}
						else
						{
							doors.get(id).t_end_y = blockClicked.getY();
						}
						
						if (doors.get(id).t_start_z > blockClicked.getZ())
						{
							doors.get(id).t_end_z = doors.get(id).t_start_z;
							doors.get(id).t_start_z = blockClicked.getZ();
						}
						else
						{
							doors.get(id).t_end_z = blockClicked.getZ();
						}
						
						doors.get(id).zoneTrigger = true;
						doors.get(id).occupants = 0;
                        
                        writeDoor(doors.get(id));
                        
						settings.select = 0;
						settings.command = "";
					}
					
				}

			//}
            return(false);
        }
	}
}
