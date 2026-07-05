package nl.oxod.oxmines.region;

import org.bukkit.Location;
import org.bukkit.World;

/**
 * Utility for calculating the center point of a cuboid region.
 */
public class RegionCenter {

  /**
   * Calculates the center location between two corner positions.
   *
   * @param pos1  the first corner
   * @param pos2  the second corner
   * @param world the world
   * @return the center location
   */
  public static Location calculate(Location pos1, Location pos2, World world) {
    int x1 = pos1.getBlockX();
    int y1 = pos1.getBlockY();
    int z1 = pos1.getBlockZ();

    int x2 = pos2.getBlockX();
    int y2 = pos2.getBlockY();
    int z2 = pos2.getBlockZ();

    int x = (x1 + x2) / 2;
    int y = (y1 + y2) / 2;
    int z = (z1 + z2) / 2;

    return new Location(world, x, y, z);
  }
}
