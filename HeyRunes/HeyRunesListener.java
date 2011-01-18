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

	public void runeCreated(Player player, HeyRune runePlaced, Block block, HeyRune.MatchType mt)
	{
		int paramCount = 7;

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
				if (callback != null && callback.getParameterTypes().length > methods[i].getParameterTypes().length)
					continue; // Don't get older versions
				callback = methods[i];

				if (callback.getParameterTypes().length == paramCount) // break on "newest" function
					break;
			}
		}
		if (callback == null)
		{
			a.log(Level.SEVERE, "Failed to find the internalRuneCreated method on the HeyRunesListener object. The plugin is probably broken.");
			return;
		}
		try {
			// Reverse compatibility between new direction passing
			switch (callback.getParameterTypes().length)
			{
				case 7:
					callback.invoke(list, player,
						runePlaced.name(), runePlaced.pattern(), runePlaced.signatureBlocks(), runePlaced.direction.ordinal(),
						mt.ordinal(), block);
				case 6:
					callback.invoke(list, player,
						runePlaced.name(), runePlaced.pattern(), runePlaced.direction.ordinal(),
						mt.ordinal(), block);
					break;
				case 5:
					callback.invoke(list, player, runePlaced.name(), runePlaced.pattern(), runePlaced.direction.ordinal(), block);
					break;
				case 4:
					callback.invoke(list, player, runePlaced.name(), runePlaced.pattern(), block);
					break;
				default:
					throw new Exception("Invalid number of arguments in internalRuneCreated");
			}
		} catch (Exception ex) {
			a.log(Level.SEVERE, "Exception while attempting call the listeners internal callback function: " + ex);
		}
	}
}
