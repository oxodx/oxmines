package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.entity.Player;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
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
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      Messages.send(player, "clear.missing-name");
      return;
    }

    boolean success = MineRegenerator.regenerate(args[1]);
    if (success) {
      Messages.send(player, "clear.success", "mine", args[1]);
    } else {
      Messages.send(player, "general.something-wrong");
    }
  }
}
