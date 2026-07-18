package nl.oxod.oxmines.mine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.region.BlockSelector;

/**
 * Manages scheduled tasks for mine regeneration and clear-on-empty checking.
 */
public class MineScheduler {
  private static HashMap<String, Integer> regenTaskIds = new HashMap<>();
  private static HashMap<String, Integer> clearTaskIds = new HashMap<>();

  /**
   * Schedules a repeating regeneration task for a mine.
   *
   * @param mineName        the mine name
   * @param intervalSeconds interval between regenerations
   * @return true if scheduling succeeded
   */
  public static boolean scheduleRegeneration(String mineName, int intervalSeconds) {
    try {
      if (regenTaskIds.containsKey(mineName)) {
        cancelRegeneration(mineName);
      }
      int id = OxMines.getInstance().getServer().getScheduler()
          .scheduleSyncRepeatingTask(
              OxMines.getInstance(),
              () -> {
                MineRegenerator.regenerate(mineName);
                try {
                  if (MinesFile.getBoolean("mines." + mineName + ".announceRegen")) {
                    Location pos1 = MinesFile.getLocation("mines." + mineName + ".pos1");
                    Location pos2 = MinesFile.getLocation("mines." + mineName + ".pos2");
                    for (Player p : OxMines.getInstance().getServer()
                        .getOnlinePlayers()) {
                      if (RegionChecker.isPlayerInRegion(p, pos1, pos2)) {
                        Messages.send(p, "scheduler.regenerated", "mine", mineName);
                      }
                    }
                  }
                } catch (Exception ignored) { // skip announce errors
                }
              },
              intervalSeconds * 20L,
              intervalSeconds * 20L);

      regenTaskIds.put(mineName, id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Cancels the regeneration task for a mine.
   *
   * @param mineName the mine name
   * @return true if cancellation succeeded
   */
  public static boolean cancelRegeneration(String mineName) {
    try {
      OxMines.getInstance().getServer().getScheduler()
          .cancelTask(regenTaskIds.get(mineName));
      regenTaskIds.remove(mineName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Cancels all scheduled regeneration tasks.
   *
   * @return true if all tasks were cancelled
   */
  public static boolean cancelAll() {
    try {
      Iterator<Map.Entry<String, Integer>> it = regenTaskIds.entrySet().iterator();
      while (it.hasNext()) {
        OxMines.getInstance().getServer().getScheduler()
            .cancelTask(it.next().getValue());
        it.remove();
      }
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Schedules a periodic check to regenerate a mine when it is fully emptied.
   *
   * @param mineName        the mine name
   * @param intervalSeconds interval between checks
   * @return true if scheduling succeeded
   */
  public static boolean scheduleClearCheck(String mineName, int intervalSeconds) {
    try {
      if (clearTaskIds.containsKey(mineName)) {
        cancelClearCheck(mineName);
      }
      int id = OxMines.getInstance().getServer().getScheduler()
          .scheduleSyncRepeatingTask(
              OxMines.getInstance(),
              () -> {
                try {
                  if (MinesFile.get("mines." + mineName + ".resetWhenEmpty") != null
                      && MinesFile.getBoolean("mines." + mineName + ".resetWhenEmpty")) {
                    Location pos1 = MinesFile.getLocation("mines." + mineName + ".pos1");
                    Location pos2 = MinesFile.getLocation("mines." + mineName + ".pos2");

                    List<Block> blocksInArea = BlockSelector.getBlocks(pos1, pos2, pos1.getWorld());

                    boolean foundAnything = false;
                    for (Block block : blocksInArea) {
                      if (block.getType() != Material.AIR) {
                        foundAnything = true;
                        break;
                      }
                    }

                      if (!foundAnything) {
                        MineRegenerator.regenerate(mineName);
                        try {
                        if (MinesFile.getBoolean("mines." + mineName + ".announceRegen")) {
                            for (Player p : OxMines.getInstance()
                                .getServer().getOnlinePlayers()) {
                              if (RegionChecker.isPlayerInRegion(p, pos1, pos2)) {
                                Messages.send(p, "scheduler.regenerated",
                                    "mine", mineName);
                              }
                            }
                          }
                        } catch (Exception ignored) { // skip announce errors
                        }
                      }
                  }
                } catch (Exception ignored) { // skip check errors
                }
              },
              intervalSeconds * 20L,
              intervalSeconds * 20L);

      clearTaskIds.put(mineName, id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Cancels the clear-on-empty check task for a mine.
   *
   * @param mineName the mine name
   * @return true if cancellation succeeded
   */
  public static boolean cancelClearCheck(String mineName) {
    try {
      OxMines.getInstance().getServer().getScheduler()
          .cancelTask(clearTaskIds.get(mineName));
      clearTaskIds.remove(mineName);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
