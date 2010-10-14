public class RuneCraftRegisteredListener {

    //private int[][] pattern;
	private HeyRune rune;
    private RuneCraftListener listener;
    private RunePlugin plugin;

//    public RuneCraftRegisteredListener(int[][] pat, RuneCraftListener l, Plugin p) {
    public RuneCraftRegisteredListener(HeyRune r, RuneCraftListener l, RunePlugin p) {
//        pattern = pat;
		rune = r;
        listener = l;
        plugin = p;
    }

//    public int[][] getPattern() {
//        return pattern;
//    }
    public HeyRune getRune() {
        return rune;
    }

    public RuneCraftListener getListener() {
        return listener;
    }

    public RunePlugin getRunePlugin() {
        return plugin;
    }

}
