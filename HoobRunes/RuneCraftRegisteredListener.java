public class RuneCraftRegisteredListener {

    //private int[][] pattern;
	private HoobRune rune;
    private RuneCraftListener listener;
    private Plugin plugin;

//    public RuneCraftRegisteredListener(int[][] pat, RuneCraftListener l, Plugin p) {
    public RuneCraftRegisteredListener(HoobRune r, RuneCraftListener l, Plugin p) {
//        pattern = pat;
		rune = r;
        listener = l;
        plugin = p;
    }

//    public int[][] getPattern() {
//        return pattern;
//    }
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
