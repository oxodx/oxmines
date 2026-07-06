package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.region.SelectionManager;
import nl.oxod.oxmines.region.SelectionRegion;
import nl.oxod.oxmines.region.Vector3;

/** Subcommand to set position 1 for manual mine selection. */
public class Pos1SubCommand extends SubCommand {
  @Override
  public String getName() {
    return "pos1";
  }

  @Override
  public String getDescription() {
    return "Set the first position for your mine placement";
  }

  @Override
  public String getSyntax() {
    return "/oxmines pos1";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.pos1";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.pos1")) {
      Messages.send(player, "general.no-permission");
      return;
    }

    SelectionRegion region = SelectionManager.getSelection(player);
    if (region == null) {
      region = new SelectionRegion();
    }

    Block block = player.getLocation().getBlock();
    region.pos1 = new Vector3(block.getX(), block.getY() - 1, block.getZ());
    SelectionManager.setSelection(player, region);

    Messages.send(player, "pos.set-pos1", "pos", region.pos1.toString());
  }
}
