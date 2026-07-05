package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.mine.MineScheduler;
import nl.oxod.oxmines.region.SelectionManager;
import nl.oxod.oxmines.region.SelectionRegion;
import nl.oxod.oxmines.region.Vector3;

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
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      player.sendMessage(ChatColor.RED + "You must provide a unique name for this mine!");
      return;
    }

    String mineName = args[1];

    if (OxMines.getInstance().getConfig().get("mines." + mineName) != null) {
      player.sendMessage(ChatColor.RED + "Mine name must be unique!"
          + " You can find a list of mines by doing /oxmines list");
      return;
    }

    SelectionRegion region = SelectionManager.getSelection(player);
    if (region == null) {
      player.sendMessage(ChatColor.RED + "You do not have any area selected!");
      return;
    }
    if (region.pos1 == null) {
      player.sendMessage(ChatColor.RED + "Position 1 not set!");
      return;
    }
    if (region.pos2 == null) {
      player.sendMessage(ChatColor.RED + "Position 2 not set!");
      return;
    }

    OxMines.getInstance().getConfig().set("mines." + mineName + ".pos1",
        new Location(player.getWorld(),
            region.pos1.getX(), region.pos1.getY(), region.pos1.getZ()));
    OxMines.getInstance().getConfig().set("mines." + mineName + ".pos2",
        new Location(player.getWorld(),
            region.pos2.getX(), region.pos2.getY(), region.pos2.getZ()));
    OxMines.getInstance().saveConfig();

    player.sendMessage(ChatColor.GREEN + "Successfully added mine "
        + ChatColor.GOLD + mineName + ChatColor.GREEN + "!");

    MineScheduler.scheduleClearCheck(mineName, 1);
  }
}
