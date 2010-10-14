import java.util.Hashtable;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SampleRune extends Plugin
{
	private static Logger a = Logger.getLogger("Minecraft");
	//private Listener l = new SampleRuneListener();
	private SampleRuneListener listener = new SampleRuneListener();

	public void enable() { }
	
	public void initialize()
	{
		HoobRune rune = new HoobRune("Tower", new int[][] { 	{-55, -55, -55, -55, -55},
															{-55,  55,  55,  55, -55},
															{-55,  55, -55,  55, -55},
															{-55,  55,  55,  55, -55},
															{-55, -55, -55, -55, -55}	});
		//etc.getLoader().addListener(PluginLoader.Hook.CHAT, l, this, PluginListener.Priority.MEDIUM);
		HoobRunes hr = (HoobRunes)etc.getLoader().getPlugin("HoobRunes");
		hr.addListener(rune, listener, this);
	}
	public void disable() { }
	
	public class SampleRuneListener extends RuneCraftListener
	{
		public void runeCreated(Player player)
		{
			player.sendMessage("GOT SAMPLE RUNE!");
		}
	}

}