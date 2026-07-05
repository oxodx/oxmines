package nl.oxod.oxmines.region;

import java.util.HashMap;
import org.bukkit.entity.Player;

/**
 * Manages per-player cuboid region selections for mine creation.
 */
public class SelectionManager {
  private static HashMap<Player, SelectionRegion> selections = new HashMap<>();

  /**
   * Gets the current selection for a player.
   *
   * @param player the player
   * @return the selection region, or null if none set
   */
  public static SelectionRegion getSelection(Player player) {
    return selections.get(player);
  }

  /**
   * Sets the selection region for a player.
   *
   * @param player the player
   * @param region the selection region
   */
  public static void setSelection(Player player, SelectionRegion region) {
    selections.put(player, region);
  }

  /**
   * Removes the selection region for a player.
   *
   * @param player the player
   */
  public static void removeSelection(Player player) {
    selections.remove(player);
  }
}
