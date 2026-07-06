package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.mine.MinesFile;
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
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      Messages.send(player, "add.missing-name");
      return;
    }

    String mineName = args[1];

    if (MinesFile.get("mines." + mineName) != null) {
      Messages.send(player, "add.not-unique");
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
      pos1 = region.pos1;
      pos2 = region.pos2;
    }

    MinesFile.set("mines." + mineName + ".pos1",
        new Location(player.getWorld(),
            pos1.getX(), pos1.getY(), pos1.getZ()));
    MinesFile.set("mines." + mineName + ".pos2",
        new Location(player.getWorld(),
            pos2.getX(), pos2.getY(), pos2.getZ()));
    MinesFile.save();

    Messages.send(player, "add.success", "mine", mineName);

    MineScheduler.scheduleClearCheck(mineName, 1);
  }
}
