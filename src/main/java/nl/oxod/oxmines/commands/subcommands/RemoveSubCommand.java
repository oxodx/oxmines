package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.entity.Player;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.mine.MinesFile;
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
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      Messages.send(player, "remove.missing-name");
      return;
    }

    String mineName = args[1];

    if (MinesFile.get("mines." + mineName) == null) {
      Messages.send(player, "general.mine-not-found");
      return;
    }

    MinesFile.set("mines." + mineName, null);
    MinesFile.save();

    MineScheduler.cancelRegeneration(mineName);
    MineScheduler.cancelClearCheck(mineName);

    Messages.send(player, "remove.success", "mine", mineName);
  }
}
