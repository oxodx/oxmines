package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.mine.MineScheduler;
import nl.oxod.oxmines.region.SelectionManager;
import nl.oxod.oxmines.region.SelectionRegion;

/** Subcommand to add a mine (without WorldEdit). */
public class AddSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "add";
  }

  @Override
  public String getDescription() {
    return "Add a mine";
  }

  @Override
  public String getSyntax() {
    return "/oxmines add <minename>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.add";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.add")) {
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      Messages.send(player, "add.missing-name");
      return;
    }

    String mineName = args[1];

    if (OxMines.getInstance().getConfig().get("mines." + mineName) != null) {
      Messages.send(player, "add.not-unique");
      return;
    }

    SelectionRegion region = SelectionManager.getSelection(player);
    if (region == null) {
      Messages.send(player, "add.no-selection");
      return;
    }
    if (region.pos1 == null) {
      Messages.send(player, "add.pos1-missing");
      return;
    }
    if (region.pos2 == null) {
      Messages.send(player, "add.pos2-missing");
      return;
    }

    OxMines.getInstance().getConfig().set("mines." + mineName + ".pos1",
        new Location(player.getWorld(),
            region.pos1.getX(), region.pos1.getY(), region.pos1.getZ()));
    OxMines.getInstance().getConfig().set("mines." + mineName + ".pos2",
        new Location(player.getWorld(),
            region.pos2.getX(), region.pos2.getY(), region.pos2.getZ()));
    OxMines.getInstance().saveConfig();

    Messages.send(player, "add.success", "mine", mineName);

    MineScheduler.scheduleClearCheck(mineName, 1);
  }
}
