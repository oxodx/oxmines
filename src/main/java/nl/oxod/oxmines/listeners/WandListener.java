package nl.oxod.oxmines.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import nl.oxod.oxmines.commands.subcommands.WandSubCommand;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.region.SelectionManager;
import nl.oxod.oxmines.region.SelectionRegion;
import nl.oxod.oxmines.region.Vector3;

/** Listens for wand interactions to set mine selection positions. */
public class WandListener implements Listener {

  /**
   * Handles block clicks with the Mine Wand to set pos1/pos2.
   *
   * @param event the interact event
   */
  @EventHandler
  public void onWandInteract(PlayerInteractEvent event) {
    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }

    Player player = event.getPlayer();
    ItemStack item = player.getInventory().getItemInMainHand();

    if (!isWand(item)) {
      return;
    }

    event.setCancelled(true);

    Block clickedBlock = event.getClickedBlock();
    if (clickedBlock == null) {
      return;
    }

    SelectionRegion region = SelectionManager.getSelection(player);
    if (region == null) {
      region = new SelectionRegion();
    }

    Vector3 pos = new Vector3(
        clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ());

    if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
      region.pos1 = pos;
      SelectionManager.setSelection(player, region);
      Messages.send(player, "wand.pos1-set", "pos", pos.toString());
    } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
      region.pos2 = pos;
      SelectionManager.setSelection(player, region);
      Messages.send(player, "wand.pos2-set", "pos", pos.toString());
    }
  }

  private boolean isWand(ItemStack item) {
    if (item == null || item.getType() != Material.STICK) {
      return false;
    }
    ItemMeta meta = item.getItemMeta();
    return meta != null && WandSubCommand.WAND_NAME.equals(meta.displayName());
  }
}
