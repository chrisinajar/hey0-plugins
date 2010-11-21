import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandAlias extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	private Listener l = new Listener(this);
	public String configLocation = "aliases.conf";
	public ArrayList<Alias> aliases = new ArrayList<Alias>();
	public static int loopChecker = 0;
	public class Alias {
		public ArrayList<String> commands = new ArrayList<String>();
		public ArrayList<String> alias = new ArrayList<String>();
		public int minArgs = 0;
		public boolean allowAnyone = true;
		public ArrayList<String> allowedGroups = new ArrayList<String>();
		public ArrayList<String> allowedUsers = new ArrayList<String>();
		public ArrayList<String> allowedCommands = new ArrayList<String>();
		public ArrayList<String> options = new ArrayList<String>();
	}

	public void enable() {
		this.reloadConfig();
		etc.getLoader().addListener(PluginLoader.Hook.COMMAND, l, this, PluginListener.Priority.MEDIUM);
	}
	public void disable() { }

	public class Listener extends PluginListener
	{
		CommandAlias plugin = null;
		Listener(CommandAlias pl)
		{
			plugin = pl;
		}

		public boolean onCommand(Player player, String[] split)
		{
			//if (!player.canUseCommand("/commandalias"))
			//	return false;
			if(plugin.loopChecker == 20)
			{
				plugin.a.log(Level.SEVERE, "Command alias recursed 20 times, aborting this command: " + split[0]);
				return false;
			}
			Alias matched = null;
			String allArgs = new String();
			for (int i = 1; i < split.length; ++i)
			{
				allArgs += split[i];
				if (i != (split.length - 1))
					allArgs += " ";
			}
			for (Alias a : plugin.aliases)
			{
				for (String s : a.commands)
				{
					if (!s.equalsIgnoreCase(split[0]))
					{
						// plugin.a.log(Level.SEVERE, s + " isn't " + split[0]);
						continue;
					}
					if (matched != null && a.minArgs < matched.minArgs)
					{
						// plugin.a.log(Level.SEVERE, "We already matched an alias with " + matched.minArgs);
						continue;
					}
					if (a.minArgs >= split.length)
					{
						// plugin.a.log(Level.SEVERE, a.minArgs + " is greater than or equal to " + split.length);
						continue;
					}

					boolean hasPermission = a.allowAnyone;
					while (!hasPermission) // so that I can break out of it, i could use a function but I don't want to :3
					{
						if (a.allowedCommands.size() > 0)
						{
							boolean matchedCommands = true;
							for (String cmd : a.allowedCommands)
							{
								if (!player.canUseCommand(cmd))
								{
									matchedCommands = false;
									break;
								}
							}
							if (matchedCommands)
							{
								hasPermission = true;
								break;
							}
						}
						for (String name : a.allowedUsers)
						{
							if (player.getName().equalsIgnoreCase(name))
							{
								hasPermission = true;
								break;
							}
						}
						if (hasPermission)
							break;
						for (String group : a.allowedGroups)
						{
							if(player.isInGroup(group))
							{
								hasPermission = true;
								break;
							}
						}
						if (hasPermission)
							break;
						break;
					}
					if (!hasPermission) // This user cannot run this command
						continue;
					matched = a;
				}
			}
			if (matched == null)
			{
				return false;
			}
			plugin.loopChecker++;
			String runAs = null;
			for (String option : matched.options)
			{
				if(option.startsWith("runas="))
				{
					runAs = option.substring(6);
				}
			}
			String[] oldGroups = player.getGroups();
			if (runAs != null)
			{
				// plugin.a.log(Level.SEVERE, "Running alias as " + runAs);
				player.addGroup(runAs);
			}
			for (String c : matched.alias)
			{
				String[] cmdToRun = c.split(" +", 0);
				for (int i = 0; i < cmdToRun.length; ++i)
				{
					if(cmdToRun[i].equals("[@]"))
					{
						cmdToRun[i] = allArgs;
						continue;
					}
					if (cmdToRun[i].matches("^\\[[0-9]+\\]$"))
					{
						int argNum = Integer.parseInt(cmdToRun[i].substring(1, cmdToRun[i].length() - 1));
						
						while (argNum >= split.length)
							argNum--;
						cmdToRun[i] = split[argNum];
					}
				}
				String strCmd = new String();
				for (int i = 0; i < cmdToRun.length; ++i)
				{
					strCmd += cmdToRun[i];
					if (i != (cmdToRun.length - 1))
						strCmd += " ";
				}
				player.getUser().a.a(new bg(strCmd)); // D: oh noez
			}
			if (runAs != null)
				player.setGroups(oldGroups);
			plugin.loopChecker--;
			return true;
		}
	}

	public void reloadConfig() {
		if (new File(configLocation).exists()) {
			try {
				Scanner scanner = new Scanner(new File(configLocation));
				Alias alias = new Alias();
				boolean inAlias = false;
				boolean inOptions = false;
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					if (line.startsWith("#") || line.length() == 0)
						continue;
					String[] parts = line.split(" ");
					if (inAlias) {
						int minArgs = 0;
						String aliasStr = new String();
						for (String p : parts) {
							if (p.length() == 0)
								continue;
							if (p.equalsIgnoreCase("}")) {
								if(aliasStr.length() > 0) {
									alias.alias.add(aliasStr);
									alias.minArgs = (alias.minArgs < alias.minArgs ? minArgs : alias.minArgs);
								}
								aliases.add(alias);
								alias = new Alias();
								inAlias = false;
								break;
							}
							if (p.equalsIgnoreCase("{"))
								throw new Exception("Misplaced '{' character");
							if (p.matches("^\\[[0-9]+\\]$")) {
								int argNum = Integer.parseInt(p.substring(1, p.length() - 1));
								minArgs = (argNum > minArgs ? argNum : minArgs);
							}
							if (aliasStr.length() > 0)
								aliasStr = aliasStr + " " + p;
							else
								aliasStr = p;
						}
						if(aliasStr.length() > 0) {
							alias.alias.add(aliasStr);
							alias.minArgs = (alias.minArgs < minArgs ? minArgs : alias.minArgs);
						}
					}
					else {
						for (String p : parts) {
							if (p.length() == 0)
								continue;
							if (p.equalsIgnoreCase("{")) {
								inAlias = true;
								break;
							}
							if (p.equalsIgnoreCase("}"))
								throw new Exception("Misplaced '}' character");
							if (p.charAt(0) == '[' || p.charAt(p.length() - 1) == ']')
							{
								if (p.charAt(0) != '[' || p.charAt(p.length() - 1) != ']')
									throw new Exception("Malformed options list:" + p);
								String[] options = p.substring(1, p.length() - 1).split(",");
								for (String o : options)
								{
									if (o.charAt(1) != ':')
										throw new Exception("Malformed option: " + o + "... '" + o.charAt(1) + "' is not ':'");
									if (o.charAt(0) == 'g') // group allowed list
									{
										alias.allowedGroups.add(o.substring(2));
										alias.allowAnyone = false;
									}
									else if (o.charAt(0) == 'u') // users allowed list
									{
										alias.allowedUsers.add(o.substring(2));
										alias.allowAnyone = false;
									}
									else if (o.charAt(0) == 'c') // command allowed list
									{
										alias.allowedCommands.add(o.substring(2));
										alias.allowAnyone = false;
									}
									else if (o.charAt(0) == 'o') // generic option
									{
										alias.options.add(o.substring(2));
									}
									else
									{
										a.log(Level.SEVERE, "Unknown option type found while reading aliases: " + o);
									}
								}
							}
							else
							{
								alias.commands.add(p);
							}
						}
					}
				}
			} catch (Exception e) {
				a.log(Level.SEVERE, "Exception while reading " + configLocation + ", please paste this to chrisinajar", e);
			}
		}
		else
		{
			a.log(Level.SEVERE, "no aliases found");
		}
	}
}

