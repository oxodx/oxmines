package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;

/** Subcommand to set a custom warp teleport point for a mine. */
public class SetWarpSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "setwarp";
  }

  @Override
  public String getDescription() {
    return "Set the warp point for a mine at your location";
  }

  @Override
  public String getSyntax() {
    return "/oxmines setwarp <minename>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.setwarp";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.setwarp")) {
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      Messages.send(player, "setwarp.missing-name");
      return;
    }

    String mineName = args[1];

    if (OxMines.getInstance().getConfig().get("mines." + mineName) == null) {
      Messages.send(player, "general.mine-not-found");
      return;
    }

    Location loc = player.getLocation();
    OxMines.getInstance().getConfig().set("mines." + mineName + ".warp", loc);
    OxMines.getInstance().saveConfig();

    Messages.send(player, "setwarp.success",
        "mine", mineName,
        "x", String.valueOf(loc.getBlockX()),
        "y", String.valueOf(loc.getBlockY()),
        "z", String.valueOf(loc.getBlockZ()));
  }
}
