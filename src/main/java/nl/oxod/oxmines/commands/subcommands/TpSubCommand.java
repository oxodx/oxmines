package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.region.RegionCenter;

/** Subcommand to teleport a player to the top center of a mine. */
public class TpSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "tp";
  }

  @Override
  public String getDescription() {
    return "Teleports you to the top of the provided mine";
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
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    if (args.length < 2) {
      player.sendMessage(ChatColor.RED + "You must provide a mine name!");
      return;
    }

    Location pos1 = OxMines.getInstance().getConfig()
        .getLocation("mines." + args[1] + ".pos1");
    Location pos2 = OxMines.getInstance().getConfig()
        .getLocation("mines." + args[1] + ".pos2");

    if (pos1 == null || pos2 == null) {
      player.sendMessage(ChatColor.RED + "That mine does not exist!");
      return;
    }

    int highestY = Math.max(pos1.getBlockY(), pos2.getBlockY());

    Location tpPoint = RegionCenter.calculate(pos1, pos2, pos1.getWorld());
    tpPoint.setY(highestY + 1);

    player.teleport(tpPoint);
  }
}
