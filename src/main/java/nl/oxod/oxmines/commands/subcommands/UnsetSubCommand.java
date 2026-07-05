package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;

/** Subcommand to remove a block type from a mine's composition. */
public class UnsetSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "unset";
  }

  @Override
  public String getDescription() {
    return "Unset a block for a mine";
  }

  @Override
  public String getSyntax() {
    return "/oxmines unset <minename> <block>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.unset";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.unset")) {
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      Messages.send(player, "unset.missing-mine");
      return;
    }

    if (args.length < 3 || args[2].isEmpty()) {
      Messages.send(player, "unset.missing-block");
      return;
    }

    String mineName = args[1];
    String blockName = args[2];

    boolean validBlock = false;
    for (Material m : Material.values()) {
      if (m.isBlock() && m.name().equals(blockName.toUpperCase())) {
        validBlock = true;
        break;
      }
    }
    if (!validBlock) {
      Messages.send(player, "unset.invalid-block");
      return;
    }

    if (OxMines.getInstance().getConfig().get("mines." + mineName) == null) {
      Messages.send(player, "general.mine-not-found");
      return;
    }

    OxMines.getInstance().getConfig()
        .set("mines." + mineName + ".blocks." + blockName.toLowerCase(), null);
    OxMines.getInstance().saveConfig();

    Messages.send(player, "unset.success",
        "block", blockName.toLowerCase(), "mine", mineName);
  }
}
