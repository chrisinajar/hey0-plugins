/**
 * SampleHitBloxListener.java - Listens for events
 * @author Ho0ber
 */
public class SampleHitBloxListener extends PluginListener
{
	public void onArmSwing(Player player) {
		//If holding a block, left click to place anywhere!
		if ((player.canUseCommand("/SampleHitBlox")) && (player.getItemInHand() < 100))
		{
			HitBlox blox = new HitBlox(player);
			blox.setFaceBlock(player.getItemInHand());
		}
		//If holding a wooden stick, left click to delete blocks!
		else if ((player.canUseCommand("/SampleHitBlox")) && (player.getItemInHand() == 280))
		{
			HitBlox blox = new HitBlox(player);
			blox.setTargetBlock(0);
		}
    }
}
