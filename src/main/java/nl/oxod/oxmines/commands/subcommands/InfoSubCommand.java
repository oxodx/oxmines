package nl.oxod.oxmines.commands.subcommands;

import java.util.Objects;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;

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

    if (OxMines.getInstance().getConfig().get("mines." + mineName) == null) {
      Messages.send(player, "general.mine-not-found");
      return;
    }

    Location pos1 = OxMines.getInstance().getConfig()
        .getLocation("mines." + mineName + ".pos1");
    Location pos2 = OxMines.getInstance().getConfig()
        .getLocation("mines." + mineName + ".pos2");

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

    int regenInterval = OxMines.getInstance().getConfig()
        .getInt("mines." + mineName + ".regenInterval");
    if (regenInterval > 0) {
      Messages.send(player, "info.regen-interval", "time", String.valueOf(regenInterval));
    } else {
      Messages.send(player, "info.regen-disabled");
    }

    boolean announce = OxMines.getInstance().getConfig()
        .getBoolean("mines." + mineName + ".announceRegen");
    Messages.send(player, announce ? "info.announce-yes" : "info.announce-no");

    boolean resetEmpty = OxMines.getInstance().getConfig()
        .getBoolean("mines." + mineName + ".resetWhenEmpty");
    Messages.send(player, resetEmpty ? "info.reset-yes" : "info.reset-no");

    Location warp = OxMines.getInstance().getConfig()
        .getLocation("mines." + mineName + ".warp");
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

    try {
      Set<String> blockKeys = Objects.requireNonNull(
          OxMines.getInstance().getConfig()
              .getConfigurationSection("mines." + mineName + ".blocks"))
          .getKeys(false);

      if (!blockKeys.isEmpty()) {
        Messages.send(player, "info.blocks-header");
        for (String key : blockKeys) {
          int pct = OxMines.getInstance().getConfig()
              .getInt("mines." + mineName + ".blocks." + key);
          Messages.send(player, "info.block-entry",
              "block", key, "pct", String.valueOf(pct));
        }
      } else {
        Messages.send(player, "info.blocks-none");
      }
    } catch (NullPointerException e) {
      Messages.send(player, "info.blocks-none");
    }
  }
}
