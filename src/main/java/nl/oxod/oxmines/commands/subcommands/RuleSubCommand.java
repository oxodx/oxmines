package nl.oxod.oxmines.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.SubCommand;
import nl.oxod.oxmines.mine.MineScheduler;

/** Subcommand to configure mine rules (regen time, announcements, reset-on-empty). */
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
      player.sendMessage(ChatColor.RED + "No permission!");
      return;
    }

    if (args.length == 1) {
      player.sendMessage("List of rules: announceRegen, regenTime, resetWhenEmpty");
      return;
    }

    if (args.length < 5) {
      player.sendMessage(ChatColor.RED + "Missing arguments!");
      return;
    }

    String mineName = args[1];
    String rule = args[3];
    String value = args[4];

    if (value.isEmpty()) {
      player.sendMessage(ChatColor.RED + "Empty value!");
      return;
    }

    switch (rule.toLowerCase()) {
      case "announceregen" -> {
        if (!value.equalsIgnoreCase("true")
            && !value.equalsIgnoreCase("false")) {
          player.sendMessage(ChatColor.RED + "Value options are 'true' or 'false'.");
          return;
        }

        boolean curVal = OxMines.getInstance().getConfig()
            .getBoolean("mines." + mineName + ".announceRegen");
        boolean newVal = value.equalsIgnoreCase("true");

        if (curVal == newVal) {
          String state = newVal ? "already announcing" : "already not announcing";
          player.sendMessage(
              ChatColor.GOLD + mineName + ChatColor.GREEN
                  + " is " + state + " regeneration!");
          return;
        }

        OxMines.getInstance().getConfig()
            .set("mines." + mineName + ".announceRegen", newVal);

        String change = newVal ? "now announcing" : "no longer announcing";
        player.sendMessage(
            ChatColor.GOLD + mineName + ChatColor.GREEN
                + " is " + change + " regeneration!");
      }
      case "regentime" -> {
        if (OxMines.getInstance().getConfig()
            .get("mines." + mineName) == null) {
          player.sendMessage(ChatColor.RED + "That mine does not exist!");
          return;
        }

        int timeInSeconds;
        try {
          timeInSeconds = Integer.parseInt(value);
        } catch (NumberFormatException e) {
          player.sendMessage(ChatColor.RED + "That is not a valid amount of time!"
              + " The time must be a number.");
          return;
        }

        if (timeInSeconds <= 0) {
          MineScheduler.cancelRegeneration(mineName);
          player.sendMessage(
              ChatColor.AQUA + mineName + ChatColor.GREEN
                  + " will no longer automatically regenerate.");
        } else {
          MineScheduler.scheduleRegeneration(mineName, timeInSeconds);
          player.sendMessage(
              ChatColor.AQUA + mineName + ChatColor.GREEN
                  + " will now automatically regenerate every "
                  + ChatColor.GOLD + timeInSeconds + ChatColor.GREEN + " seconds.");
        }

        OxMines.getInstance().getConfig()
            .set("mines." + mineName + ".regenInterval", timeInSeconds);
      }
      case "resetwhenempty" -> {
        if (!value.equalsIgnoreCase("true")
            && !value.equalsIgnoreCase("false")) {
          player.sendMessage(ChatColor.RED + "Value options are 'true' or 'false'.");
          return;
        }

        boolean curVal = OxMines.getInstance().getConfig()
            .getBoolean("mines." + mineName + ".resetWhenEmpty");
        boolean newVal = value.equalsIgnoreCase("true");

        if (curVal == newVal) {
          String state = newVal ? "already resetting" : "already not resetting";
          player.sendMessage(
              ChatColor.GOLD + mineName + ChatColor.GREEN
                  + " is " + state + " when empty!");
          return;
        }

        OxMines.getInstance().getConfig()
            .set("mines." + mineName + ".resetWhenEmpty", newVal);

        if (newVal) {
          MineScheduler.scheduleClearCheck(mineName, 1);
        } else {
          MineScheduler.cancelClearCheck(mineName);
        }
      }
      default -> {
        player.sendMessage(ChatColor.RED + "Rule does not exist.");
        return;
      }
    }

    OxMines.getInstance().saveConfig();
    player.sendMessage(ChatColor.GREEN + "Successfully set rule "
        + ChatColor.GOLD + rule + ChatColor.GREEN + " on "
        + ChatColor.GOLD + mineName + ChatColor.GREEN + " to "
        + ChatColor.GOLD + value + ChatColor.GREEN + ".");
  }
}
