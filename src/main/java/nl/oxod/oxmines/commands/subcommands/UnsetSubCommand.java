package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;

/** Subcommand to remove a block type from a mine's composition. */
public class UnsetSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "unset";
  }

  @Override
  public String getDescription() {
    return "Unset a block for a mine";
  }

  @Override
  public String getSyntax() {
    return "/oxmines unset <minename> <block>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.unset";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.unset")) {
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      player.sendMessage(ChatColor.RED + "You must provide a mine to unset the block for!");
      return;
    }

    if (args.length < 3 || args[2].isEmpty()) {
      player.sendMessage(ChatColor.RED + "You must provide a block to unset!");
      return;
    }

    String mineName = args[1];
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

    if (OxMines.getInstance().getConfig().get("mines." + mineName) == null) {
      player.sendMessage(ChatColor.RED + "That mine does not exist!");
      return;
    }

    OxMines.getInstance().getConfig()
        .set("mines." + mineName + ".blocks." + blockName.toLowerCase(), null);
    OxMines.getInstance().saveConfig();

    player.sendMessage(ChatColor.GREEN + "Successfully unset "
        + ChatColor.YELLOW + blockName.toLowerCase() + ChatColor.GREEN + " on mine "
        + ChatColor.GOLD + mineName);
  }
}
