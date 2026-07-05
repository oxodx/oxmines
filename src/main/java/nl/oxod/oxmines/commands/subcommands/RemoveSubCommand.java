package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.mine.MineScheduler;

/** Subcommand to remove a mine. */
public class RemoveSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "remove";
  }

  @Override
  public String getDescription() {
    return "Remove a mine";
  }

  @Override
  public String getSyntax() {
    return "/oxmines remove <minename>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.remove";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.remove")) {
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      player.sendMessage(
          ChatColor.RED + "You must provide the name of the mine you'd like to remove!");
      return;
    }

    String mineName = args[1];

    if (OxMines.getInstance().getConfig().get("mines." + mineName) == null) {
      player.sendMessage(ChatColor.RED + "That mine does not exist!");
      return;
    }

    OxMines.getInstance().getConfig().set("mines." + mineName, null);
    OxMines.getInstance().saveConfig();

    MineScheduler.cancelRegeneration(mineName);
    MineScheduler.cancelClearCheck(mineName);

    player.sendMessage(ChatColor.GREEN + "Successfully deleted mine "
        + ChatColor.GOLD + mineName + "!");
  }
}
