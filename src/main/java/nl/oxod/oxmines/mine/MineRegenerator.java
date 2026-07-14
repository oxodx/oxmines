package nl.oxod.oxmines.mine;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.region.BlockSelector;

/**
 * Handles regenerating mine blocks from configured compositions and clearing
 * them.
 */
public class MineRegenerator {
  private static final SecureRandom RANDOM = new SecureRandom();

  /**
   * Selects a material based on weighted random distribution.
   *
   * @param blockTypes the list of block materials
   * @param weights the corresponding weights
   * @param totalWeight the sum of all weights
   * @param random the random instance to use
   * @return the selected material, or null if selection fails
   */
  static Material selectBlock(List<Material> blockTypes,
      List<Double> weights, double totalWeight, SecureRandom random) {
    double roll = random.nextDouble() * totalWeight;
    double cumulative = 0.0;

    for (int i = 0; i < blockTypes.size(); i++) {
      cumulative += weights.get(i);
      if (roll <= cumulative) {
        return blockTypes.get(i);
      }
    }
    return blockTypes.get(blockTypes.size() - 1);
  }

  /**
   * Regenerates a mine with its configured block types and weights.
   * Teleports players inside to the mine's warp if one is set.
   *
   * @param mineName the name of the mine
   * @return true if regeneration succeeded
   */
  public static boolean regenerate(String mineName) {
    try {
      List<Material> blockTypes = new ArrayList<>();
      List<Double> weights = new ArrayList<>();

      for (String blockName : MinesFile.getConfigurationSection(
          "mines." + mineName + ".blocks")
          .getKeys(false)) {
        double weight = MinesFile.getDouble(
            "mines." + mineName + ".blocks." + blockName);
        if (weight <= 0.0) {
          continue;
        }
        for (Material m : Material.values()) {
          if (m.isBlock() && m.name().equals(blockName.toUpperCase())) {
            blockTypes.add(m);
            weights.add(weight);
            break;
          }
        }
      }

      if (blockTypes.isEmpty()) {
        OxMines.getInstance().getLogger()
            .log(Level.SEVERE, "Couldn't get block types for mine: " + mineName);
        return false;
      }

      double totalWeight = 0.0;
      for (double w : weights) {
        totalWeight += w;
      }

      Location pos1 = MinesFile.getLocation("mines." + mineName + ".pos1");
      Location pos2 = MinesFile.getLocation("mines." + mineName + ".pos2");
      List<Block> blocksInArea = BlockSelector.getBlocks(pos1, pos2, pos1.getWorld());

      for (Block block : blocksInArea) {
        Material chosen = selectBlock(blockTypes, weights,
            totalWeight, RANDOM);
        if (chosen != null) {
          block.setType(chosen);
        }
      }

      Location warp = MinesFile.getLocation("mines." + mineName + ".warp");

      for (Player p : OxMines.getInstance().getServer().getOnlinePlayers()) {
        if (RegionChecker.isPlayerInRegion(p, pos1, pos2)) {
          if (warp != null) {
            p.teleport(warp);
          }
          // No warp set: leave player where they are (warp is optional)
        }
      }
    } catch (RuntimeException e) {
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
      Location pos1 = MinesFile.getLocation("mines." + mineName + ".pos1");
      Location pos2 = MinesFile.getLocation("mines." + mineName + ".pos2");

      List<Block> blocksInArea = BlockSelector.getBlocks(pos1, pos2, pos1.getWorld());

      for (Block block : blocksInArea) {
        block.setType(Material.AIR);
      }
    } catch (RuntimeException e) {
      return false;
    }

    return true;
  }
}
