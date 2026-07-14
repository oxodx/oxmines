package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.mine.MinesFile;

/** Subcommand to set a block type with a percentage for a mine. */
public class SetSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "set";
  }

  @Override
  public String getDescription() {
    return "Set a block for a mine";
  }

  @Override
  public String getSyntax() {
    return "/oxmines set <minename> <block> <percentage>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.set";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.set")) {
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length < 2 || args[1].isEmpty()) {
      Messages.send(player, "set.missing-mine");
      return;
    }
    String mineName = args[1];

    if (MinesFile.get("mines." + mineName) == null) {
      Messages.send(player, "general.mine-not-found");
      return;
    }

    if (args.length < 3 || args[2].isEmpty()) {
      Messages.send(player, "set.missing-block");
      return;
    }
    String blockName = args[2];

    boolean validBlock = false;
    for (Material m : Material.values()) {
      if (m.isBlock() && m.name().equals(blockName.toUpperCase())) {
        validBlock = true;
        break;
      }
    }
    if (!validBlock) {
      Messages.send(player, "set.invalid-block");
      return;
    }

    if (args.length < 4 || args[3].isEmpty()) {
      Messages.send(player, "set.missing-percentage");
      return;
    }

    double weight;
    try {
      weight = Double.parseDouble(args[3]);
    } catch (NumberFormatException e) {
      Messages.send(player, "set.invalid-percentage");
      return;
    }

    if (weight <= 0.0) {
      Messages.send(player, "set.invalid-percentage");
      return;
    }

    MinesFile.set("mines." + mineName + ".blocks." + blockName.toLowerCase(), weight);
    MinesFile.save();

    Messages.send(player, "set.success",
        "block", blockName.toLowerCase(),
        "pct", String.valueOf(weight),
        "mine", mineName);
  }
}
