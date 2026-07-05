package nl.oxod.oxmines.commands.subcommands;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import nl.oxod.oxmines.commands.SubCommand;

/** Subcommand to give the player a wand for selecting mine regions. */
public class WandSubCommand extends SubCommand {
  public static final String WAND_NAME = ChatColor.GOLD + "Mine Wand";
  public static final String WAND_LORE = ChatColor.GRAY + "Left-click: pos1, Right-click: pos2";

  @Override
  public String getName() {
    return "wand";
  }

  @Override
  public String getDescription() {
    return "Get the selection wand for defining mine areas";
  }

  @Override
  public String getSyntax() {
    return "/oxmines wand";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.wand";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.wand")) {
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    ItemStack wand = new ItemStack(Material.STICK);
    ItemMeta meta = wand.getItemMeta();
    meta.setDisplayName(WAND_NAME);
    meta.setLore(Arrays.asList(WAND_LORE));
    wand.setItemMeta(meta);

    player.getInventory().addItem(wand);
    player.sendMessage(ChatColor.GREEN + "You received the "
        + ChatColor.GOLD + "Mine Wand" + ChatColor.GREEN + "!");
    player.sendMessage(ChatColor.GRAY + "Left-click a block to set pos1,"
        + " right-click to set pos2.");
  }
}
