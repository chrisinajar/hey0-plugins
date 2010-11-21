import java.lang.reflect.Method;
import java.lang.Class;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HeyRunesListener
{
        private static Logger a = Logger.getLogger("Minecraft");
	Object list = null;
	public HeyRunesListener(Object l)
	{
		list = l;
	}

	public void runeCreated(Player player, HeyRune runePlaced, Block block)
	{
		if (list == null)
		{
			a.log(Level.SEVERE, "Null listener found in HeyRunes");
			return;
		}

		Class listClass = list.getClass();
		// get the method
		Method[] methods = listClass.getMethods();
		Method callback = null;
		for (int i = 0; i < methods.length; ++i)
		{
			if (methods[i].getName().equals("internalRuneCreated"))
			{
				callback = methods[i];
				break;
			}
		}
		if (callback == null)
		{
			a.log(Level.SEVERE, "Failed to find the internalRuneCreated method on the HeyRunesListener object. The plugin is probably broken.");
			return;
		}
		try {
			callback.invoke(list, player, runePlaced.name(), runePlaced.pattern(), block);
		} catch (Exception ex) {
			a.log(Level.SEVERE, "Exception while attempting call the listeners internal callback function: " + ex);
		}	
	}
}
