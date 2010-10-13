import java.util.Hashtable;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BangCommandHider extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	private Listener l = new Listener();

	public void enable() { }
	
	public void initialize()
	{
		etc.getLoader().addListener(PluginLoader.Hook.CHAT, l, this, PluginListener.Priority.MEDIUM);
	}
	public void disable() { }

	public class Listener extends PluginListener {
		public boolean onChat(Player player, String message) {
			if (message.startsWith("!")) {
			a.log(Level.INFO, "<" + player.getName() + "> " + message);
				return true;
			}
			return false;
		}
	}

}
