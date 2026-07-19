package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.mine.MinesFile;
import nl.oxod.oxmines.mine.MineScheduler;
import nl.oxod.oxmines.region.SelectionManager;
import nl.oxod.oxmines.region.SelectionRegion;

/** Subcommand to redefine an existing mine's boundary points. */
public class RedefineSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "redefine";
  }

  @Override
  public String getDescription() {
    return "Redefine a mine's boundary with your current selection";
  }

  @Override
  public String getSyntax() {
    return "/oxmines redefine <minename>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.redefine";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.redefine")) {
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      Messages.send(player, "redefine.missing-name");
      return;
    }

    String mineName = args[1];

    if (MinesFile.get("mines." + mineName) == null) {
      Messages.send(player, "general.mine-not-found");
      return;
    }

    SelectionRegion region = SelectionManager.getSelection(player);
    if (region == null) {
      Messages.send(player, "redefine.no-selection");
      return;
    }
    if (region.pos1 == null) {
      Messages.send(player, "redefine.pos1-missing");
      return;
    }
    if (region.pos2 == null) {
      Messages.send(player, "redefine.pos2-missing");
      return;
    }

    MinesFile.set("mines." + mineName + ".pos1",
        new Location(player.getWorld(),
            region.pos1.getX(), region.pos1.getY(), region.pos1.getZ()));
    MinesFile.set("mines." + mineName + ".pos2",
        new Location(player.getWorld(),
            region.pos2.getX(), region.pos2.getY(), region.pos2.getZ()));
    MinesFile.save();

    Messages.send(player, "redefine.success", "mine", mineName);

    MineScheduler.scheduleClearCheck(mineName, 1);
  }
}