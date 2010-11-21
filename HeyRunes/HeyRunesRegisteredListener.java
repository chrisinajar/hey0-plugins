public class HeyRunesRegisteredListener
{
	private HeyRune rune;
	private HeyRunesListener listener;
	private Plugin plugin;

	public HeyRunesRegisteredListener(HeyRune r, Object l, Plugin p)
	{
		rune = r;
		listener = new HeyRunesListener(l);
		plugin = p;
	}

	public HeyRune getRune()
	{
		return rune;
	}

	public HeyRunesListener getListener()
	{
		return listener;
	}

	public Plugin getPlugin()
	{
		return plugin;
	}

}

