/**
* HeyRune.java - Extend this and register it to listen to specific hooks.
* @author chrisinajar
*/

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HeyRune
{
	private static Logger a = Logger.getLogger("Minecraft");

	public static enum Direction { NORTH, SOUTH, EAST, WEST, NONE }
	public static enum MatchType { NONE, onPlayerMove, onArmSwing } // On any kind of error or version mismatch, "NONE" is used.

	private ArrayList<Integer> pattern = new ArrayList<Integer>();
	public String name;
	public Direction direction;
	public boolean north = true;
	public boolean south = true;
	public boolean east = true;
	public boolean west = true;
	
	public HeyRune(String in_name, int[][] in_pattern) {
		name = in_name;
		windPattern(in_pattern);
		direction = Direction.NONE;
	}

	// old constructor for reverse compatibility
	public HeyRune(String in_name, ArrayList<Integer> in_pattern)
	{
		name = in_name;
		pattern = in_pattern;
		direction = Direction.NONE;
	}

	// new direciton handling
	public HeyRune(String in_name, ArrayList<Integer> in_pattern, int in_direction)
	{
		name = in_name;
		pattern = in_pattern;
		direction = Direction.values()[in_direction];
	}

	public String name()
	{
		return name;
	}

	public ArrayList<Integer> pattern()
	{
		return pattern;
	}
	
	public int size() {
		return pattern.size();
	}

	public int height() {
		return (int)Math.ceil(Math.sqrt(pattern.size()));
	}
	
	public int idAt(int index) {
		try {
			return pattern.get(index);
		} catch (IndexOutOfBoundsException ex) {
			return -1;
		}
	}

/*
	// I overload, what of it.
	public void removeRune(int x, int y, int z)
	{
		removeRune(x, y, z, true, true);
	}

	public void removeRune(int x, int y, int z, boolean removeSignatures)
	{
		removeRune(x, y, z, removeSignatures, true);
	}

	public void removeRune(int x, int y, int z, boolean removeSignatures, boolean removeExceptions)
	{
		if (direction == NONE)
		{
			a.log(Level.SEVERE, "Cannot remove rune, " + name() + ", until it is matched.");
		}
		int index = 0;
		int xoffset = 0;
		int yoffset = 0;
		int zoffset = 0;
		int curX = x;
		int cuyY = y;
		int curZ = z;
		for (int i = 0; index < size; ++i) {
			for (int j = 0; j < ((i/2) + 1); ++j) {
				switch(direction)
				{
					case NORTH:
						curX = zoffset + x;
						curZ = -xoffset + z;
						break;
					case SOUTH:
						curX = -zoffset + x;
						curZ = xoffset + z;
						break;
					case EAST:
						curX = xoffset + x;
						curZ = zoffset + z;
						break;
					case WEST:
						curX = -xoffset + x;
						curZ = -zoffset + z;
						break;
				}
				int myId = rune.at(index);
				if (myId > 0)
				{
					if (etc.getServer().getBlockIdAt(curX, curY, curZ) == myId)
						etc.getServer().setBlockAt(0, curX, curY, curZ);
				}
				else if (removeSignatures && myId <= -100 && myId > -200)
				{
//					if (etc.getServer().getBlockIdAt(curX, curY, curZ) == getSignatureBlockId(myId))
//						etc.getServer().setBlockAt(0, curX, curY, curZ);
				}
				else if (removeExceptions && myId <= -1000)
					etc.getServer().setBlockAt(0, curX, curY, curZ);

				switch ((i)%4) {
					case 1:
						zoffset++;
						break;
					case 0:
						xoffset++;
						break;
					case 3:
						zoffset--;
						break;
					case 2:
						xoffset--;
						break;
				}
				index++;
			}
		}
	}
*/
	
	private void windPattern(int[][] p) {
		double height = p.length;
		double width = 0;
		double size = height;
		for(int i = 0; i < p.length; ++i) {
			if (p[i].length > width)
				width = p[i].length;
		}
		int offset = -1;

		int curx = 0;
		int cury = 0;
		if (width > size) {
			size = width;
			offset = -2;
			if ((size%2) == 1) {
				offset = -3;
			}
			else if ((size%2) == 0) {
				offset = -4;
			}
		}
		else
		{
			if ((size%2) == 0) {
				offset = -4;
			}
		}
		
		cury += (height)/2;
		curx += (width)/2;
	
		int counter = 1;
		for (int i = 0; i < ((size*2) + offset); ++i) {
			for (int j = 0; j < ((i/2) + 1); ++j) {
				try {
					pattern.add(p[cury][curx]);
				} catch (Throwable t) {
					pattern.add(-1);
				}
				switch((i)%4) {
					case 1:
						cury++;
						break;
					case 0:
						curx++;
						break;
					case 3:
						cury--;
						break;
					case 2:
						curx--;
						break;
				}
			}
		}
	}
	
	public void reset() {
		north = true;
		south = true;
		east = true;
		west = true;
		direction = Direction.NONE;
	}
}
