package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;

/** Subcommand to remove a mine's custom warp point. */
public class DelWarpSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "delwarp";
  }

  @Override
  public String getDescription() {
    return "Remove the warp point for a mine";
  }

  @Override
  public String getSyntax() {
    return "/oxmines delwarp <minename>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.delwarp";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.delwarp")) {
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

    if (OxMines.getInstance().getConfig().get("mines." + mineName + ".warp") == null) {
      player.sendMessage(ChatColor.RED + "That mine does not have a warp set!");
      return;
    }

    OxMines.getInstance().getConfig().set("mines." + mineName + ".warp", null);
    OxMines.getInstance().saveConfig();

    player.sendMessage(ChatColor.GREEN + "Warp point removed for mine "
        + ChatColor.GOLD + mineName);
  }
}
