package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.region.RegionCenter;

/** Subcommand to teleport to a mine (uses custom warp if set). */
public class TpSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "tp";
  }

  @Override
  public String getDescription() {
    return "Teleport to a mine's warp or its top center";
  }

  @Override
  public String getSyntax() {
    return "/oxmines tp <minename>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.tp";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.tp")) {
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2) {
      Messages.send(player, "general.missing-name");
      return;
    }

    String mineName = args[1];

    Location pos1 = OxMines.getInstance().getConfig()
        .getLocation("mines." + mineName + ".pos1");
    Location pos2 = OxMines.getInstance().getConfig()
        .getLocation("mines." + mineName + ".pos2");

    if (pos1 == null || pos2 == null) {
      Messages.send(player, "general.mine-not-found");
      return;
    }

    Location warp = OxMines.getInstance().getConfig()
        .getLocation("mines." + mineName + ".warp");
    if (warp != null) {
      player.teleport(warp);
      Messages.send(player, "tp.warped", "mine", mineName);
    } else {
      int highestY = Math.max(pos1.getBlockY(), pos2.getBlockY());
      Location tpPoint = RegionCenter.calculate(pos1, pos2, pos1.getWorld());
      tpPoint.setY(highestY + 1);
      player.teleport(tpPoint);
      Messages.send(player, "tp.top", "mine", mineName);
    }
  }
}
