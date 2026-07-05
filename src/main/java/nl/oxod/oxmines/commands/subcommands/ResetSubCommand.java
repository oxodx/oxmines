package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.mine.MineRegenerator;

/** Subcommand to immediately regenerate a mine. */
public class ResetSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "reset";
  }

  @Override
  public String getDescription() {
    return "Reset a mine";
  }

  @Override
  public String getSyntax() {
    return "/oxmines reset <minename>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.reset";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.reset")) {
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      player.sendMessage(ChatColor.RED + "You need to provide a mine to reset!");
      return;
    }

    boolean success = MineRegenerator.regenerate(args[1]);
    if (success) {
      player.sendMessage(ChatColor.GREEN + "Reset " + ChatColor.GOLD + args[1]);
    } else {
      player.sendMessage(ChatColor.RED + "Something went wrong! Make sure you"
          + " have blocks set for the mine and that the mine exists.");
    }
  }
}
