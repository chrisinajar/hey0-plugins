
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.EnumMap;
//import net.minecraft.server.MinecraftServer;

/**
 * RunePluginLoader.java - Used to load RunePlugins, toggle them, etc.
 * @author James
 */
public class RunePluginLoader {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static final Object lock = new Object();
	private HoobRunes plugin;
    private List<RunePlugin> RunePlugins = new ArrayList<RunePlugin>();
	
	private List<RuneCraftRegisteredListener> regListeners = new ArrayList<RuneCraftRegisteredListener>();
    //private List< List<RuneCraftRegisteredListener> > listeners = new ArrayList< List<RuneCraftRegisteredListener> >();
	
    private PropertiesFile properties;

    /**
     * Creates a RunePlugin loader
     * @param server
     */
    public RunePluginLoader(HoobRunes plugin) {
        properties = new PropertiesFile("server.properties");
		this.plugin = plugin;
    }

    /**
     * Loads all RunePlugins.
     */
    public void loadRunePlugins() {
        String[] classes = properties.getString("runeplugins", "").split(",");
        for (String sclass : classes) {
            if (sclass.equals(""))
                continue;
            loadRunePlugin(sclass);
        }
    }

    /**
     * Loads the specified RunePlugin
     * @param fileName
     */
    public void loadRunePlugin(String fileName) {
        if (getRunePlugin(fileName) != null)
            return; //Already exists.
		log.log(Level.INFO, "Loading RunePlugin:" + fileName);
        load(fileName);
    }

    /**
     * Reloads the specified RunePlugin
     * @param fileName
     */
    public void reloadRunePlugin(String fileName) {
        /* Not sure exactly how much of this is necessary */
        RunePlugin toNull = getRunePlugin(fileName);
        if (toNull != null)
            if (toNull.isEnabled())
                toNull.disable();
        synchronized (lock) {
	        RunePlugins.remove(toNull);
			for ( RuneCraftRegisteredListener reg : regListeners )
				if ( reg.getRunePlugin() == toNull )
					regListeners.remove(reg);
        }
        toNull = null;

        load(fileName);
    }

    private void load(String fileName) {
        try {
            File file = new File("runeplugins/" + fileName + ".jar");
            URLClassLoader child = null;
            try {
                child = new MyClassLoader(new URL[]{file.toURL()}, this.getClass().getClassLoader());
            } catch (MalformedURLException ex) {
                log.log(Level.SEVERE, "Exception while loading class", ex);
            }
            Class c = Class.forName(fileName, true, child);

            try {
                RunePlugin RunePlugin = (RunePlugin) c.newInstance();
                RunePlugin.setName(fileName);
                RunePlugin.enable();
                synchronized (lock) {
                    RunePlugins.add(RunePlugin);
                    RunePlugin.initialize();
                }
            } catch (InstantiationException ex) {
                log.log(Level.SEVERE, "Exception while loading RunePlugin", ex);
            } catch (IllegalAccessException ex) {
                log.log(Level.SEVERE, "Exception while loading RunePlugin", ex);
            }
        } catch (ClassNotFoundException ex) {
            log.log(Level.SEVERE, "Exception while loading RunePlugin", ex);
        }
    }

    /**
     * Returns the specified RunePlugin
     * @param name
     * @return
     */
    public RunePlugin getRunePlugin(String name) {
        synchronized (lock) {
            for (RunePlugin RunePlugin : RunePlugins) {
                if (RunePlugin.getName().equalsIgnoreCase(name)) {
                    return RunePlugin;
                }
            }
        }
        return null;
    }

    /**
     * Returns a string list of RunePlugins
     * @return
     */
    public String getRunePluginList() {
        StringBuilder sb = new StringBuilder();
        synchronized (lock) {
            for (RunePlugin RunePlugin : RunePlugins) {
                sb.append(RunePlugin.getName());
                sb.append(" ");
                sb.append(RunePlugin.isEnabled() ? "(E)" : "(D)");
                sb.append(",");
            }
        }
        String str = sb.toString();
        if (str.length() > 1)
            return str.substring(0, str.length() - 1);
        else
            return "Empty";
    }

    /**
     * Enables the specified RunePlugin (Or adds and enables it)
     * @param name
     * @return
     */
    public boolean enableRunePlugin(String name) {
        RunePlugin RunePlugin = getRunePlugin(name);
        if (RunePlugin != null) {
            if (!RunePlugin.isEnabled()) {
                RunePlugin.toggleEnabled();
                RunePlugin.enable();
            }
        } else { //New RunePlugin, perhaps?
            File file = new File("runeplugins/" + name + ".jar");
            if (file.exists())
                loadRunePlugin(name);
            else
                return false;
        }
        return true;
    }

    /**
     * Disables specified RunePlugin
     * @param name
     */
    public void disableRunePlugin(String name) {
        RunePlugin RunePlugin = getRunePlugin(name);
        if (RunePlugin != null) {
            if (RunePlugin.isEnabled()) {
                RunePlugin.toggleEnabled();
                RunePlugin.disable();
            }
        }
    }

    /**
     * Returns the server
     * @return
     */
    public HoobRunes getPlugin() {
        return plugin;
    }

    /**
     * Calls a RunePlugin hook.
     * @param h
     * @param parameters
     * @return
     */
    public void callHook(Player player, Block blockPlaced, Block blockClicked, int itemInHand) {
		for (RuneCraftRegisteredListener rl : regListeners)
		{
			HoobRune.Direction rune_dir = rl.getRune().matches(blockClicked.getX(), blockClicked.getY(), blockClicked.getZ());
			if (rune_dir != HoobRune.Direction.NONE)
			{
				RuneCraftListener listener = rl.getListener();
				listener.runeCreated(player, blockPlaced, blockClicked, itemInHand);
			}
		}
	}
	

    
    /**
     * Calls a RunePlugin hook.
     * @param h
     * @param parameters
     * @return
     */
	public RuneCraftRegisteredListener addListener(HoobRune rune, RuneCraftListener listener, RunePlugin plugin)
	{
    	RuneCraftRegisteredListener reg = new RuneCraftRegisteredListener(rune, listener, plugin);
	    regListeners.add(reg);
    	return reg;
    }
    
    public void removeListener(RuneCraftRegisteredListener reg)
    {
        synchronized (lock) {
        	regListeners.remove(reg);
        }
    }
}
