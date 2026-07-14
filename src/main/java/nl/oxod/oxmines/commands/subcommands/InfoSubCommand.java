package nl.oxod.oxmines.commands.subcommands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.mine.MinesFile;

/** Subcommand to show detailed information about a mine. */
public class InfoSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "info";
  }

  @Override
  public String getDescription() {
    return "Show detailed info about a mine";
  }

  @Override
  public String getSyntax() {
    return "/oxmines info <minename>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.info";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.info")) {
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      Messages.send(player, "general.missing-name");
      return;
    }

    String mineName = args[1];

    if (MinesFile.get("mines." + mineName) == null) {
      Messages.send(player, "general.mine-not-found");
      return;
    }

    Location pos1 = MinesFile.getLocation("mines." + mineName + ".pos1");
    Location pos2 = MinesFile.getLocation("mines." + mineName + ".pos2");

    int width = Math.abs(pos1.getBlockX() - pos2.getBlockX()) + 1;
    int height = Math.abs(pos1.getBlockY() - pos2.getBlockY()) + 1;
    int depth = Math.abs(pos1.getBlockZ() - pos2.getBlockZ()) + 1;
    int volume = width * height * depth;

    Messages.send(player, "info.header", "mine", mineName);
    Messages.send(player, "info.world", "world", pos1.getWorld().getName());
    Messages.send(player, "info.size",
        "width", String.valueOf(width),
        "height", String.valueOf(height),
        "depth", String.valueOf(depth),
        "volume", String.valueOf(volume));

    int regenInterval = MinesFile.getInt("mines." + mineName + ".regenInterval");
    if (regenInterval > 0) {
      Messages.send(player, "info.regen-interval", "time", String.valueOf(regenInterval));
    } else {
      Messages.send(player, "info.regen-disabled");
    }

    boolean announce = MinesFile.getBoolean("mines." + mineName + ".announceRegen");
    Messages.send(player, announce ? "info.announce-yes" : "info.announce-no");

    boolean resetEmpty = MinesFile.getBoolean("mines." + mineName + ".resetWhenEmpty");
    Messages.send(player, resetEmpty ? "info.reset-yes" : "info.reset-no");

    Location warp = MinesFile.getLocation("mines." + mineName + ".warp");
    if (warp != null) {
      Messages.send(player, "info.warp",
          "x", String.valueOf(warp.getBlockX()),
          "y", String.valueOf(warp.getBlockY()),
          "z", String.valueOf(warp.getBlockZ()),
          "yaw", String.format("%.1f", warp.getYaw()),
          "pitch", String.format("%.1f", warp.getPitch()));
    } else {
      Messages.send(player, "info.warp-not-set");
    }

    ConfigurationSection blocksSection = MinesFile.getConfigurationSection(
        "mines." + mineName + ".blocks");
    if (blocksSection != null) {
      Set<String> blockKeys = blocksSection.getKeys(false);
      if (!blockKeys.isEmpty()) {
        double totalWeight = 0.0;
        for (String key : blockKeys) {
          double weight = MinesFile.getDouble("mines." + mineName + ".blocks." + key);
          if (weight > 0.0) {
            totalWeight += weight;
          }
        }

        Messages.send(player, "info.blocks-header");
        for (String key : blockKeys) {
          double weight = MinesFile.getDouble("mines." + mineName + ".blocks." + key);
          Messages.send(player, "info.block-entry",
              "block", key,
              "pct", formatPercentage(weight, totalWeight),
              "weight", formatWeight(weight));
        }
      } else {
        Messages.send(player, "info.blocks-none");
      }
    } else {
      Messages.send(player, "info.blocks-none");
    }
  }

  /**
   * Formats a block weight as a normalized percentage of the total weight.
   *
   * @param weight the block weight
   * @param totalWeight the total weight of all configured blocks
   * @return the percentage formatted for display
   */
  public static String formatPercentage(double weight, double totalWeight) {
    if (totalWeight <= 0.0) {
      return "0%";
    }

    BigDecimal percentage;
    if (weight < totalWeight / 100.0) {
      percentage = BigDecimal.valueOf(weight)
          .multiply(BigDecimal.valueOf(100.0))
          .divide(BigDecimal.valueOf(totalWeight), 2, RoundingMode.HALF_UP);
    } else {
      percentage = BigDecimal.valueOf(weight)
          .multiply(BigDecimal.valueOf(100.0))
          .divide(BigDecimal.valueOf(totalWeight), 2, RoundingMode.DOWN);
    }

    if (percentage.compareTo(BigDecimal.ZERO) == 0) {
      return "0%";
    }

    BigDecimal trimmed = percentage.stripTrailingZeros();
    return trimmed.toPlainString() + "%";
  }

  static String formatWeight(double weight) {
    return BigDecimal.valueOf(weight).stripTrailingZeros().toPlainString();
  }
}
