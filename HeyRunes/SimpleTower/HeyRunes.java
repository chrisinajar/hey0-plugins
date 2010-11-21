import java.lang.reflect.Method;
import java.lang.Class;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HeyRunes
{
	private static Logger a = Logger.getLogger("Minecraft");
	public static void addListener(HeyRune rune, HeyRunesListener list, Plugin pl)
	{
		Plugin hrpl = etc.getLoader().getPlugin("HeyRunes");
		if (hrpl == null)
		{
			a.log(Level.SEVERE, "Cannot register a rune when HeyRunes plugin is not loaded");
			return;
		}
		
		Class runeClass = hrpl.getClass();
		// get the method
		Method[] methods = runeClass.getDeclaredMethods();
		Method addlist = null;
		for (int i = 0; i < methods.length; ++i)
		{
			if (methods[i].getName().equals("addListener"))
			{
				addlist = methods[i];
				break;
			}
		}
		if (addlist == null)
		{
			a.log(Level.SEVERE, "Failed to find the addListener method on the HeyRunes plugin object. Are we certain it's loaded properly?");
			return;
		}
		try {
			addlist.invoke(hrpl, rune.name(), rune.pattern(), pl, (Object)list);
		} catch (Exception ex) {
			a.log(Level.SEVERE, "Exception while attempting to register HeyRunes listener: " + ex);
		}
	}
}



