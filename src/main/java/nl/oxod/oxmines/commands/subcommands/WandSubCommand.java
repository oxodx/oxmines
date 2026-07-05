package nl.oxod.oxmines.commands.subcommands;

import java.util.Arrays;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;

/** Subcommand to give the player a wand for selecting mine regions. */
public class WandSubCommand extends SubCommand {
  public static final Component WAND_NAME = MiniMessage.miniMessage()
      .deserialize("<gold>Mine Wand</gold>");
  public static final Component WAND_LORE = MiniMessage.miniMessage()
      .deserialize("<gray>Left-click: pos1, Right-click: pos2</gray>");

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
      Messages.send(player, "general.no-permission");
      return;
    }

    ItemStack wand = new ItemStack(Material.STICK);
    ItemMeta meta = wand.getItemMeta();
    meta.displayName(WAND_NAME);
    meta.lore(Arrays.asList(WAND_LORE));
    wand.setItemMeta(meta);

    player.getInventory().addItem(wand);
    Messages.send(player, "wand.received");
    Messages.send(player, "wand.instructions");
  }
}
