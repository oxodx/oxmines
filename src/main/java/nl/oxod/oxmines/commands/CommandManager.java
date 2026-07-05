package nl.oxod.oxmines.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.commands.subcommands.AddSubCommand;
import nl.oxod.oxmines.commands.subcommands.AddWeSubCommand;
import nl.oxod.oxmines.commands.subcommands.ClearSubCommand;
import nl.oxod.oxmines.commands.subcommands.ListSubCommand;
import nl.oxod.oxmines.commands.subcommands.Pos1SubCommand;
import nl.oxod.oxmines.commands.subcommands.Pos2SubCommand;
import nl.oxod.oxmines.commands.subcommands.ReloadSubCommand;
import nl.oxod.oxmines.commands.subcommands.RemoveSubCommand;
import nl.oxod.oxmines.commands.subcommands.ResetSubCommand;
import nl.oxod.oxmines.commands.subcommands.RuleSubCommand;
import nl.oxod.oxmines.commands.subcommands.SetSubCommand;
import nl.oxod.oxmines.commands.subcommands.TpSubCommand;
import nl.oxod.oxmines.commands.subcommands.UnsetSubCommand;

/**
 * Dispatches /oxmines subcommands and provides tab completion.
 */
public class CommandManager implements TabCompleter, CommandExecutor {
  private final ArrayList<SubCommand> subCommands = new ArrayList<>();

  /** Creates a new CommandManager and registers all subcommands. */
  public CommandManager() {
    subCommands.add(new ClearSubCommand());
    if (OxMines.worldeditEnabled) {
      subCommands.add(new AddWeSubCommand());
    } else {
      subCommands.add(new AddSubCommand());
    }
    subCommands.add(new RemoveSubCommand());
    subCommands.add(new ListSubCommand());
    subCommands.add(new SetSubCommand());
    subCommands.add(new UnsetSubCommand());
    subCommands.add(new ResetSubCommand());
    subCommands.add(new TpSubCommand());
    subCommands.add(new ReloadSubCommand());
    subCommands.add(new Pos1SubCommand());
    subCommands.add(new Pos2SubCommand());
    subCommands.add(new RuleSubCommand());
  }

  public ArrayList<SubCommand> getSubCommands() {
    return subCommands;
  }

  @Override
  public @Nullable List<String> onTabComplete(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String alias,
      @NotNull String[] args) {
    if (!sender.hasPermission("oxmines.any")) {
      return null;
    }

    switch (args.length) {
      case 1 -> {
        List<String> possibilities = List.of(
            "add", "clear", "remove", "list", "set", "unset",
            "tp", "reset", "rule", "reload", "pos1", "pos2");
        return possibilities.stream()
            .filter(p -> p.startsWith(args[0].toLowerCase()))
            .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
      }
      case 2 -> {
        if (List.of("remove", "rule", "set", "unset", "reset", "tp", "clear")
            .contains(args[0])) {
          try {
            Set<String> keys = Objects.requireNonNull(
                OxMines.getInstance().getConfig()
                    .getConfigurationSection("mines"))
                .getKeys(false);
            return keys.stream()
                .filter(k -> k.startsWith(args[1]))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
          } catch (Exception e) {
            return new ArrayList<>();
          }
        }
        return null;
      }
      case 3 -> {
        switch (args[0]) {
          case "set":
            List<String> blockNames = new ArrayList<>();
            for (Material m : Material.values()) {
              if (m.isBlock() && m.name().startsWith(args[2].toUpperCase())) {
                blockNames.add(m.name().toLowerCase());
              }
            }
            return blockNames;
          case "unset":
            try {
              Set<String> keys = Objects.requireNonNull(
                  OxMines.getInstance().getConfig()
                      .getConfigurationSection("mines." + args[1] + ".blocks"))
                  .getKeys(false);
              return keys.stream()
                  .filter(k -> k.startsWith(args[2]))
                  .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            } catch (Exception e) {
              return new ArrayList<>();
            }
          case "add":
            return new ArrayList<>(List.of("-i"));
          case "rule":
            return new ArrayList<>(List.of("set"));
          default:
            return null;
        }
      }
      case 4 -> {
        if (args[0].equals("rule")) {
          return new ArrayList<>(List.of("regenTime", "announceRegen", "resetWhenEmpty"));
        }
        return null;
      }
      default -> {
        return null;
      }
    }
  }

  @Override
  public boolean onCommand(
      @NotNull CommandSender sender,
      @NotNull Command command,
      @NotNull String alias,
      @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      return true;
    }

    if (!sender.hasPermission("oxmines.any")) {
      player.sendMessage(ChatColor.RED + "No permission!");
      return true;
    }

    if (args.length > 0) {
      for (SubCommand subCmd : subCommands) {
        if (args[0].equalsIgnoreCase(subCmd.getName())) {
          if (subCmd.getRequiredPermission().isEmpty()
              || player.hasPermission(subCmd.getRequiredPermission())) {
            subCmd.perform(player, args);
          } else {
            player.sendMessage(ChatColor.RED + "Command does not exist!");
          }
          return true;
        }
      }
      player.sendMessage(ChatColor.RED + "Unknown subcommand!");
    } else {
      player.sendMessage("------------------");
      for (SubCommand subCmd : subCommands) {
        player.sendMessage(subCmd.getSyntax() + " - " + subCmd.getDescription());
      }
      player.sendMessage("------------------");
    }

    return true;
  }
}
