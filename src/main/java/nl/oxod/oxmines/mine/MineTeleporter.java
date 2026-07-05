package nl.oxod.oxmines.mine;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Teleports players to the top of a mine region.
 */
public class MineTeleporter {

  /**
   * Teleports a player to the block above the highest Y in the region,
   * keeping their current X/Z position.
   *
   * @param player the player to teleport
   * @param pos1   the first corner of the mine
   * @param pos2   the second corner of the mine
   * @return true if teleport succeeded
   */
  public static boolean teleportToTop(Player player, Location pos1, Location pos2) {
    try {
      int highestY = Math.max(pos1.getBlockY(), pos2.getBlockY());
      player.teleport(new Location(
          player.getWorld(),
          player.getLocation().getX(),
          highestY + 1,
          player.getLocation().getZ()));
    } catch (Exception e) {
      return false;
    }

    return true;
  }
}
