package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.entity.Player;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.mine.TimerLoader;

/** Subcommand to reload plugin configuration and schedules. */
public class ReloadSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "reload";
  }

  @Override
  public String getDescription() {
    return "Reload the plugin";
  }

  @Override
  public String getSyntax() {
    return "/oxmines reload";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.reload";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.reload")) {
      Messages.send(player, "general.no-permission");
      return;
    }

    Messages.send(player, "reload.start");

    OxMines.getInstance().reloadConfig();
    OxMines.getInstance().saveDefaultConfig();
    OxMines.getInstance().getConfig().options().copyDefaults();
    OxMines.getInstance().saveConfig();

    Messages.load();

    TimerLoader.loadAll();

    Messages.send(player, "reload.done");
  }
}
