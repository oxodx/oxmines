package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;

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
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      Messages.send(player, "general.missing-name");
      return;
    }

    String mineName = args[1];

    if (OxMines.getInstance().getConfig().get("mines." + mineName) == null) {
      Messages.send(player, "general.mine-not-found");
      return;
    }

    if (OxMines.getInstance().getConfig().get("mines." + mineName + ".warp") == null) {
      Messages.send(player, "delwarp.no-warp");
      return;
    }

    OxMines.getInstance().getConfig().set("mines." + mineName + ".warp", null);
    OxMines.getInstance().saveConfig();

    Messages.send(player, "delwarp.success", "mine", mineName);
  }
}
