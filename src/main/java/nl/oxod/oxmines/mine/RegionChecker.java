package nl.oxod.oxmines.mine;

import java.time.temporal.ValueRange;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Checks whether a player is inside a given cuboid region.
 */
public class RegionChecker {

  /**
   * Returns true if the player is standing within the specified region.
   *
   * @param player the player to check
   * @param loc1   the first corner of the region
   * @param loc2   the second corner of the region
   * @return true if the player is inside
   */
  public static boolean isPlayerInRegion(Player player, Location loc1, Location loc2) {
    int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
    int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
    int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

    int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
    int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
    int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

    return ValueRange.of(minX, maxX)
        .isValidIntValue(player.getLocation().getBlockX())
        && ValueRange.of(minY, maxY)
            .isValidIntValue(player.getLocation().getBlockY())
        && ValueRange.of(minZ, maxZ)
            .isValidIntValue(player.getLocation().getBlockZ());
  }
}
