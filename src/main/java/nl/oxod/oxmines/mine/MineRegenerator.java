package nl.oxod.oxmines.mine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.region.BlockSelector;

/**
 * Handles regenerating mine blocks from configured compositions and clearing them.
 */
public class MineRegenerator {

  /**
   * Regenerates a mine with its configured block types and percentages.
   *
   * @param mineName the name of the mine
   * @return true if regeneration succeeded
   */
  public static boolean regenerate(String mineName) {
    try {
      Location pos1 = OxMines.getInstance().getConfig()
          .getLocation("mines." + mineName + ".pos1");
      Location pos2 = OxMines.getInstance().getConfig()
          .getLocation("mines." + mineName + ".pos2");

      HashMap<Material, Integer> blocks = new HashMap<>();

      for (String blockName : OxMines.getInstance().getConfig()
          .getConfigurationSection("mines." + mineName + ".blocks")
          .getKeys(false)) {
        for (Material m : Material.values()) {
          if (m.isBlock() && m.name().equals(blockName.toUpperCase())) {
            blocks.put(m, OxMines.getInstance().getConfig()
                .getInt("mines." + mineName + ".blocks." + blockName));
            break;
          }
        }
      }

      if (blocks.isEmpty()) {
        OxMines.getInstance().getLogger()
            .log(Level.SEVERE, "Couldn't get block types for mine: " + mineName);
        return false;
      }

      List<Block> blocksInArea =
          BlockSelector.getBlocks(pos1, pos2, pos1.getWorld());

      for (Block block : blocksInArea) {
        boolean choseOne = false;
        Material highest = null;
        int highestPercentage = 0;

        for (Map.Entry<Material, Integer> entry : blocks.entrySet()) {
          if (entry.getValue() <= 0) {
            continue;
          }

          Random r = new Random();
          boolean chosen = (r.nextInt(99) + 1) <= entry.getValue();
          if (chosen) {
            choseOne = true;
            block.setType(entry.getKey());
            break;
          }
          if (highestPercentage < entry.getValue()) {
            highestPercentage = entry.getValue();
            highest = entry.getKey();
          }
        }
        if (!choseOne && highest != null) {
          block.setType(highest);
        }
      }

      for (Player p : OxMines.getInstance().getServer().getOnlinePlayers()) {
        if (RegionChecker.isPlayerInRegion(p, pos1, pos2)) {
          MineTeleporter.teleportToTop(p, pos1, pos2);
        }
      }
    } catch (Exception e) {
      return false;
    }

    return true;
  }

  /**
   * Sets every block in the mine to air.
   *
   * @param mineName the name of the mine
   * @return true if clearing succeeded
   */
  public static boolean clear(String mineName) {
    try {
      Location pos1 = OxMines.getInstance().getConfig()
          .getLocation("mines." + mineName + ".pos1");
      Location pos2 = OxMines.getInstance().getConfig()
          .getLocation("mines." + mineName + ".pos2");

      List<Block> blocksInArea =
          BlockSelector.getBlocks(pos1, pos2, pos1.getWorld());

      for (Block block : blocksInArea) {
        block.setType(Material.AIR);
      }
    } catch (Exception e) {
      return false;
    }

    return true;
  }
}
