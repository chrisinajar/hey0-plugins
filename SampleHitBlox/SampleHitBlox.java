/**
 * SampleHitBlox.java - Sample plugin showing the basic use of HitBlox
 * @author Ho0ber
 */
public class SampleHitBlox extends Plugin
{
	static final SampleHitBloxListener listener = new SampleHitBloxListener();
	
	public void enable()
	{
	}

	public void disable()
	{
	}
	
	public void initialize()
	{
        etc.getLoader().addListener(PluginLoader.Hook.ARM_SWING, listener, this, PluginListener.Priority.MEDIUM);
    }
}