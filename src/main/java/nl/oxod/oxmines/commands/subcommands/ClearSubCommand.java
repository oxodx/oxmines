package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.entity.Player;

import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
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
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      Messages.send(player, "clear.missing-name");
      return;
    }

    boolean success = MineRegenerator.clear(args[1]);
    if (success) {
      Messages.send(player, "clear.success", "mine", args[1]);
    } else {
      Messages.send(player, "clear.failure");
    }
  }
}
