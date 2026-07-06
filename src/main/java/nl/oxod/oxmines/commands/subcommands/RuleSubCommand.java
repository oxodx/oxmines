package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.entity.Player;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.mine.MinesFile;
import nl.oxod.oxmines.mine.MineScheduler;

/**
 * Subcommand to configure mine rules (regen time, announcements,
 * reset-on-empty).
 */
public class RuleSubCommand extends SubCommand {
  @Override
  public String getName() {
    return "rule";
  }

  @Override
  public String getDescription() {
    return "Set a rule for a mine";
  }

  @Override
  public String getSyntax() {
    return "/oxmines rule <mineName> set <ruleName> <value>";
  }

  @Override
  public String getRequiredPermission() {
    return "oxmines.rule";
  }

  @Override
  public void perform(Player player, String[] args) {
    if (!player.hasPermission("oxmines.rule")) {
      Messages.send(player, "general.no-permission");
      return;
    }

    if (args.length == 1) {
      Messages.send(player, "rule.list");
      return;
    }

    if (args.length < 5) {
      Messages.send(player, "rule.missing-args");
      return;
    }

    String mineName = args[1];
    String rule = args[3];
    String value = args[4];

    if (value.isEmpty()) {
      Messages.send(player, "rule.empty-value");
      return;
    }

    switch (rule.toLowerCase()) {
      case "announceregen" -> {
        if (!value.equalsIgnoreCase("true")
            && !value.equalsIgnoreCase("false")) {
          Messages.send(player, "rule.invalid-boolean");
          return;
        }

        boolean curVal = MinesFile.getBoolean("mines." + mineName + ".announceRegen");
        boolean newVal = value.equalsIgnoreCase("true");

        if (curVal == newVal) {
          String stateKey = newVal
              ? "rule.state-already-announcing"
              : "rule.state-no-longer-announcing";
          Messages.send(player, "rule.announce-already",
              "mine", mineName, "state", Messages.raw(stateKey));
          return;
        }

        MinesFile.set("mines." + mineName + ".announceRegen", newVal);

        String changeKey = newVal
            ? "rule.state-now-announcing"
            : "rule.state-no-longer-announce";
        Messages.send(player, "rule.announce-change",
            "mine", mineName, "state", Messages.raw(changeKey));
      }
      case "regentime" -> {
        if (MinesFile.get("mines." + mineName) == null) {
          Messages.send(player, "general.mine-not-found");
          return;
        }

        int timeInSeconds;
        try {
          timeInSeconds = Integer.parseInt(value);
        } catch (NumberFormatException e) {
          Messages.send(player, "rule.invalid-time");
          return;
        }

        if (timeInSeconds <= 0) {
          MineScheduler.cancelRegeneration(mineName);
          Messages.send(player, "rule.regen-disabled", "mine", mineName);
        } else {
          MineScheduler.scheduleRegeneration(mineName, timeInSeconds);
          Messages.send(player, "rule.regen-set",
              "mine", mineName, "time", String.valueOf(timeInSeconds));
        }

        MinesFile.set("mines." + mineName + ".regenInterval", timeInSeconds);
      }
      case "resetwhenempty" -> {
        if (!value.equalsIgnoreCase("true")
            && !value.equalsIgnoreCase("false")) {
          Messages.send(player, "rule.invalid-boolean");
          return;
        }

        boolean curVal = MinesFile.getBoolean("mines." + mineName + ".resetWhenEmpty");
        boolean newVal = value.equalsIgnoreCase("true");

        if (curVal == newVal) {
          String stateKey = newVal
              ? "rule.reset-change-already"
              : "rule.reset-change-no-longer";
          Messages.send(player, "rule.reset-already",
              "mine", mineName, "state", Messages.raw(stateKey));
          return;
        }

        MinesFile.set("mines." + mineName + ".resetWhenEmpty", newVal);

        if (newVal) {
          MineScheduler.scheduleClearCheck(mineName, 1);
        } else {
          MineScheduler.cancelClearCheck(mineName);
        }
      }
      default -> {
        Messages.send(player, "rule.rule-not-found");
        return;
      }
    }

    MinesFile.save();
    Messages.send(player, "rule.success",
        "rule", rule, "mine", mineName, "value", value);
  }
}
