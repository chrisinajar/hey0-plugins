public class RuneCraftRegisteredListener {

	private HoobRune rune;
    private RuneCraftListener listener;
    private Plugin plugin;

    public RuneCraftRegisteredListener(HoobRune r, RuneCraftListener l, Plugin p) {
		rune = r;
        listener = l;
        plugin = p;
    }

    public HoobRune getRune() {
        return rune;
    }

    public RuneCraftListener getListener() {
        return listener;
    }

    public Plugin getPlugin() {
        return plugin;
    }

}