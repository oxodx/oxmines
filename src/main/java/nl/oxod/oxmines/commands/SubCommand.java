package nl.oxod.oxmines.commands;

import org.bukkit.entity.Player;

/** Abstract base class for all subcommands. */
public abstract class SubCommand {

  /**
   * Gets the name of the subcommand.
   *
   * @return the command name
   */
  public abstract String getName();

  /**
   * Gets the description of the subcommand.
   *
   * @return the description
   */
  public abstract String getDescription();

  /**
   * Gets the syntax of the subcommand.
   *
   * @return the syntax string
   */
  public abstract String getSyntax();

  /**
   * Gets the required permission for the subcommand.
   *
   * @return the permission string
   */
  public abstract String getRequiredPermission();

  /**
   * Performs the subcommand.
   *
   * @param player the player executing the command
   * @param args   the command arguments
   */
  public abstract void perform(Player player, String[] args);
}
