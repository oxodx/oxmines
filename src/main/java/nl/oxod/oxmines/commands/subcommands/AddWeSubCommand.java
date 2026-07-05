package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.mine.MineScheduler;
import nl.oxod.oxmines.region.SelectionManager;
import nl.oxod.oxmines.region.SelectionRegion;
import nl.oxod.oxmines.region.Vector3;

/** Subcommand to add a mine using WorldEdit selection (or manual pos1/pos2). */
public class AddWeSubCommand extends SubCommand {
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

    boolean useWorldEdit = true;
    if (args.length == 3 && args[2].equalsIgnoreCase("-i")) {
      useWorldEdit = false;
    }

    Vector3 pos1 = null;
    Vector3 pos2 = null;

    if (useWorldEdit) {
      try {
        Region selectedRegion = WorldEdit.getInstance()
            .getSessionManager()
            .get(BukkitAdapter.adapt(player))
            .getSelection(BukkitAdapter.adapt(player.getWorld()));

        pos1 = new Vector3(
            selectedRegion.getMinimumPoint().x(),
            selectedRegion.getMinimumPoint().y(),
            selectedRegion.getMinimumPoint().z());
        pos2 = new Vector3(
            selectedRegion.getMaximumPoint().x(),
            selectedRegion.getMaximumPoint().y(),
            selectedRegion.getMaximumPoint().z());
      } catch (Exception e) {
        // fall through to manual selection
      }
    }

    if (pos1 == null || pos2 == null) {
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
      pos1 = region.pos1;
      pos2 = region.pos2;
    }

    OxMines.getInstance().getConfig().set("mines." + mineName + ".pos1",
        new Location(player.getWorld(),
            pos1.getX(), pos1.getY(), pos1.getZ()));
    OxMines.getInstance().getConfig().set("mines." + mineName + ".pos2",
        new Location(player.getWorld(),
            pos2.getX(), pos2.getY(), pos2.getZ()));
    OxMines.getInstance().saveConfig();

    player.sendMessage(ChatColor.GREEN + "Successfully added mine "
        + ChatColor.GOLD + mineName + ChatColor.GREEN + "!");

    MineScheduler.scheduleClearCheck(mineName, 1);
  }
}
