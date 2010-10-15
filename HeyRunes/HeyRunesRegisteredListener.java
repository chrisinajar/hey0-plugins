public class HeyRunesRegisteredListener {
	private HeyRune rune;
    private HeyRunes.Listener listener;
    private Plugin plugin;

    public HeyRunesRegisteredListener(HeyRune r, HeyRunes.Listener l, Plugin p) {
		rune = r;
        listener = l;
        plugin = p;
    }

    public HeyRune getRune() {
        return rune;
    }

    public HeyRunes.Listener getListener() {
        return listener;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
