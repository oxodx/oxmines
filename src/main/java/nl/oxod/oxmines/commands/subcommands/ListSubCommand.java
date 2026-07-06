package nl.oxod.oxmines.commands.subcommands;

import java.util.Objects;
import java.util.Set;

import org.bukkit.entity.Player;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;

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
      Messages.send(player, "general.no-permission");
      return;
    }

    Messages.send(player, "list.header");
    try {
      Set<String> keys = Objects.requireNonNull(
          OxMines.getInstance().getConfig()
              .getConfigurationSection("mines"))
          .getKeys(false);

      if (keys.isEmpty()) {
        Messages.send(player, "list.empty");
      } else {
        for (String key : keys) {
          Messages.send(player, "list.entry", "mine", key);
        }
      }
    } catch (NullPointerException e) {
      Messages.send(player, "list.empty");
    }
  }
}
