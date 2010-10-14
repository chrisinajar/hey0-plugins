
public class HoobRune
{
	public static enum Direction { NORTH, SOUTH, EAST, WEST, UP, DOWN, NONE }
	private int [][] pattern;
	public String name;
	
	public HoobRune(String in_name, int[][] in_pattern)
	{
		name = in_name;
		pattern = in_pattern;
	}
	
	public Direction matches(int in_x, int in_y, int in_z)
	{
		boolean north = true; //actually east
		boolean south = true; //actually west
		boolean east = true;
		boolean west = true; //north
		boolean done = false;
		
		int nx, sx, ex, wx;
		int nz, sz, ez, wz;
		
		for (int x = -2; x <= 2  && !done; x++)
		{
			ex = (-1 * x) + 2;
			wx = x + 2;
			sx = (-1 * x) + 2;
			nx = x + 2;
			
			for (int z = -2; z <= 2 && !done; z++)
			{
				ez = (-1 * z) + 2;
				wz = z + 2;
				sz = z + 2;
				nz = (-1 * z) + 2;
				
				int id = etc.getServer().getBlockIdAt(in_x + x, in_y + 1, in_z + z);
				if((pattern[nx][nz] >= 0 && pattern[nx][nz] != id) || (pattern[nx][nz] < 0 && Math.abs(pattern[nx][nz]) == id))
					north = false;
					
				if((pattern[sx][sz] >= 0 && pattern[sx][sz] != id) || (pattern[sx][sz] < 0 && Math.abs(pattern[sx][sz]) == id))
					south = false;
					
				if((pattern[ex][ez] >= 0 && pattern[ex][ez] != id) || (pattern[ex][ez] < 0 && Math.abs(pattern[ex][ez]) == id))
					east = false;
					
				if((pattern[wx][wz] >= 0 && pattern[wx][wz] != id) || (pattern[wx][wz] < 0 && Math.abs(pattern[wx][wz]) == id))
					west = false;
					
				if (!north && !south && !east && !west)
					done = true;
			}
		}
		
		if (north)
			return Direction.NORTH;
		if (south)
			return Direction.SOUTH;
		if (east)
			return Direction.EAST;
		if (west)
			return Direction.WEST;
		
	
			
		return Direction.NONE;
		
	}
}