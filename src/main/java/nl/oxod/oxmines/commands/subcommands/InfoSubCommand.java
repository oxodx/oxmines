package nl.oxod.oxmines.commands.subcommands;

import java.util.Objects;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;

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
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      player.sendMessage(ChatColor.RED + "You must provide a mine name!");
      return;
    }

    String mineName = args[1];

    if (OxMines.getInstance().getConfig().get("mines." + mineName) == null) {
      player.sendMessage(ChatColor.RED + "That mine does not exist!");
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

    player.sendMessage(ChatColor.AQUA + "=== " + ChatColor.GOLD + mineName
        + ChatColor.AQUA + " ===");
    player.sendMessage(ChatColor.GRAY + "World: " + ChatColor.WHITE
        + pos1.getWorld().getName());
    player.sendMessage(ChatColor.GRAY + "Size: " + ChatColor.WHITE
        + width + "x" + height + "x" + depth
        + ChatColor.GRAY + " (" + volume + " blocks)");

    int regenInterval = OxMines.getInstance().getConfig()
        .getInt("mines." + mineName + ".regenInterval");
    if (regenInterval > 0) {
      player.sendMessage(ChatColor.GRAY + "Regen interval: " + ChatColor.WHITE
          + regenInterval + "s");
    } else {
      player.sendMessage(ChatColor.GRAY + "Regen interval: " + ChatColor.RED + "disabled");
    }

    boolean announce = OxMines.getInstance().getConfig()
        .getBoolean("mines." + mineName + ".announceRegen");
    player.sendMessage(ChatColor.GRAY + "Announce regen: "
        + (announce ? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));

    boolean resetEmpty = OxMines.getInstance().getConfig()
        .getBoolean("mines." + mineName + ".resetWhenEmpty");
    player.sendMessage(ChatColor.GRAY + "Reset when empty: "
        + (resetEmpty ? ChatColor.GREEN + "yes" : ChatColor.RED + "no"));

    Location warp = OxMines.getInstance().getConfig()
        .getLocation("mines." + mineName + ".warp");
    if (warp != null) {
      player.sendMessage(ChatColor.GRAY + "Warp: " + ChatColor.WHITE
          + warp.getBlockX() + ", " + warp.getBlockY() + ", " + warp.getBlockZ()
          + ChatColor.GRAY + " ("
          + String.format("%.1f", warp.getYaw()) + "° / "
          + String.format("%.1f", warp.getPitch()) + "°)");
    } else {
      player.sendMessage(ChatColor.GRAY + "Warp: " + ChatColor.RED + "not set");
    }

    try {
      Set<String> blockKeys = Objects.requireNonNull(
          OxMines.getInstance().getConfig()
              .getConfigurationSection("mines." + mineName + ".blocks"))
          .getKeys(false);

      if (!blockKeys.isEmpty()) {
        player.sendMessage(ChatColor.GRAY + "Blocks:");
        for (String key : blockKeys) {
          int pct = OxMines.getInstance().getConfig()
              .getInt("mines." + mineName + ".blocks." + key);
          player.sendMessage(ChatColor.GRAY + "  - " + ChatColor.WHITE
              + key + ChatColor.GRAY + ": " + ChatColor.WHITE + pct + "%");
        }
      } else {
        player.sendMessage(ChatColor.GRAY + "Blocks: " + ChatColor.RED + "none configured");
      }
    } catch (NullPointerException e) {
      player.sendMessage(ChatColor.GRAY + "Blocks: " + ChatColor.RED + "none configured");
    }
  }
}
