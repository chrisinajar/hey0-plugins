import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

public class HoobRunes extends Plugin
{
	static final HoobRunesListener listener = new HoobRunesListener();
	public static ArrayList<HoobRune> runes = new ArrayList<HoobRune>();
	
	
	public void enable()
	{
			runes.add(new HoobRune("Tower", new int[][] { 	{-55, -55, -55, -55, -55},
														{-55,  55,  55,  55, -55},
														{-55,  55, -55,  55, -55},
														{-55,  55,  55,  55, -55},
														{-55, -55, -55, -55, -55}	}));
														
			runes.add(new HoobRune("Pit", new int[][] { 	{-55, -55, -55, -55, -55},
														{-55,  55, -55,  55, -55},
														{-55, -55, -55, -55, -55},
														{-55,  55, -55,  55, -55},
														{-55, -55, -55, -55, -55}	}));														

			runes.add(new HoobRune("StairTower", new int[][] { 	{ 55,  55,  55,  55,  55},
															{ 55, -55, -55, -55,  55},
															{ 55, -55, -55, -55,  55},
															{ 55, -55, -55, -55,  55},
															{ 55,  55,  55,  55,  55}	}));														

	}

	public void disable()
	{
	}
	
	public void initialize()
	{
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_CREATED, listener, this, PluginListener.Priority.MEDIUM);
    }
	
}
