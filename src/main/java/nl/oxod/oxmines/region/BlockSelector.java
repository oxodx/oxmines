package nl.oxod.oxmines.region;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Utility for selecting all blocks within a cuboid region.
 */
public class BlockSelector {

  /**
   * Returns every block within the region defined by two corners.
   *
   * @param loc1  the first corner
   * @param loc2  the second corner
   * @param world the world
   * @return list of all blocks in the region
   */
  public static List<Block> getBlocks(Location loc1, Location loc2, World world) {
    int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
    int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
    int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
    int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
    int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
    int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

    List<Block> blocks = new ArrayList<>();
    for (int x = minX; x <= maxX; x++) {
      for (int y = minY; y <= maxY; y++) {
        for (int z = minZ; z <= maxZ; z++) {
          blocks.add(new Location(world, x, y, z).getBlock());
        }
      }
    }

    return blocks;
  }
}
