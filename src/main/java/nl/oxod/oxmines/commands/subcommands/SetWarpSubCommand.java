package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;

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
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      player.sendMessage(ChatColor.RED + "You must provide a mine name!");
      return;
    }

    String mineName = args[1];

    if (OxMines.getInstance().getConfig().get("mines." + mineName) == null) {
      player.sendMessage(ChatColor.RED + "That mine does not exist!");
      return;
    }

    Location loc = player.getLocation();
    OxMines.getInstance().getConfig().set("mines." + mineName + ".warp", loc);
    OxMines.getInstance().saveConfig();

    player.sendMessage(ChatColor.GREEN + "Warp point set for mine "
        + ChatColor.GOLD + mineName + ChatColor.GREEN + " at "
        + ChatColor.WHITE + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
  }
}
