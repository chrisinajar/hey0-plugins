/**
 * SampleHitBlox.java - Sample plugin showing the basic use of HitBlox
 * @author Ho0ber
 */
public class SampleHitBlox extends Plugin
{
	public void enable()
	{
	}

	public void disable()
	{
	}
	
	//Hooking on arm animation (beware, this triggers on both left and right click for placeable blocks)
	public void onArmSwing(Player player)
	{
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