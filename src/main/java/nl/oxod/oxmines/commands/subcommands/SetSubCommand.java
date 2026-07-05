package nl.oxod.oxmines.commands.subcommands;

import java.util.Objects;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;

/** Subcommand to set a block type with a percentage for a mine. */
public class SetSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "set";
  }

  @Override
  public String getDescription() {
    return "Set a block for a mine";
  }

  @Override
  public String getSyntax() {
    return "/oxmines set <minename> <block> <percentage>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.set";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.set")) {
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      player.sendMessage(ChatColor.RED + "You must provide a mine to set the blocks for!");
      return;
    }
    String mineName = args[1];

    if (OxMines.getInstance().getConfig().get("mines." + mineName) == null) {
      player.sendMessage(ChatColor.RED + "That mine does not exist!");
      return;
    }

    if (args.length < 3 || args[2].isEmpty()) {
      player.sendMessage(ChatColor.RED + "You must provide a block to set!");
      return;
    }
    String blockName = args[2];

    boolean validBlock = false;
    for (Material m : Material.values()) {
      if (m.isBlock() && m.name().equals(blockName.toUpperCase())) {
        validBlock = true;
        break;
      }
    }
    if (!validBlock) {
      player.sendMessage(ChatColor.RED + "That is not a valid block!");
      return;
    }

    if (args.length < 4 || args[3].isEmpty()) {
      player.sendMessage(ChatColor.RED + "You must provide a percentage!");
      return;
    }

    int percentage;
    try {
      percentage = Integer.parseInt(args[3]);
    } catch (NumberFormatException e) {
      player.sendMessage(ChatColor.RED + "The percentage must be a number!");
      return;
    }

    try {
      Set<String> keys = Objects.requireNonNull(
          OxMines.getInstance().getConfig()
              .getConfigurationSection("mines." + mineName + ".blocks"))
          .getKeys(false);
      int currentTotal = 0;
      for (String key : keys) {
        Integer val = OxMines.getInstance().getConfig()
            .getInt("mines." + mineName + ".blocks." + key);
        if (val != null) {
          currentTotal += val;
        }
      }
      if (currentTotal + percentage > 100) {
        player.sendMessage(ChatColor.RED + "That percentage would exceed 100%!"
            + " Current percentage is: " + ChatColor.AQUA + currentTotal + "%");
        return;
      }
    } catch (Exception ignored) { // first block set, no existing section
    }

    OxMines.getInstance().getConfig()
        .set("mines." + mineName + ".blocks." + blockName.toLowerCase(), percentage);
    OxMines.getInstance().saveConfig();

    player.sendMessage(ChatColor.GREEN + "Successfully set "
        + ChatColor.YELLOW + blockName.toLowerCase() + ChatColor.GREEN + " to "
        + ChatColor.AQUA + percentage + ChatColor.GREEN + " on mine "
        + ChatColor.GOLD + mineName);
  }
}
