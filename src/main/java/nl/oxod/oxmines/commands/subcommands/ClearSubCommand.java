package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.mine.MineRegenerator;

/** Subcommand to clear all blocks in a mine. */
public class ClearSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "clear";
  }

  @Override
  public String getDescription() {
    return "Clear a mine";
  }

  @Override
  public String getSyntax() {
    return "/oxmines clear <minename>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.clear";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.clear")) {
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      player.sendMessage(ChatColor.RED + "You need to provide a mine to clear!");
      return;
    }

    boolean success = MineRegenerator.clear(args[1]);
    if (success) {
      player.sendMessage(ChatColor.GREEN + "Clear " + ChatColor.GOLD + args[1]);
    } else {
      player.sendMessage(ChatColor.RED + "Something went wrong!");
    }
  }
}
