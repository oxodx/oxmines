package nl.oxod.oxmines.commands.subcommands;

import java.util.Objects;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;

/** Subcommand to list all configured mines. */
public class ListSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "list";
  }

  @Override
  public String getDescription() {
    return "Return a list of added mines";
  }

  @Override
  public String getSyntax() {
    return "/oxmines list";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.list";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.list")) {
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    player.sendMessage(ChatColor.AQUA + "Mines:");
    try {
      Set<String> keys = Objects.requireNonNull(
          OxMines.getInstance().getConfig()
              .getConfigurationSection("mines"))
          .getKeys(false);

      if (keys.isEmpty()) {
        player.sendMessage(ChatColor.GOLD + "None!");
      } else {
        for (String key : keys) {
          player.sendMessage(ChatColor.GOLD + key);
        }
      }
    } catch (NullPointerException e) {
      player.sendMessage(ChatColor.GOLD + "None!");
    }
  }
}
