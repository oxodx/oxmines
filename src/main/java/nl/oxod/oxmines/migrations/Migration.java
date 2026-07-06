package nl.oxod.oxmines.migrations;

/**
 * A single migration step identified by a Unix timestamp.
 *
 * <p>Each implementation performs a one-time data transformation when the
 * plugin is upgraded and runs {@link MigrationRunner#run()}.
 */
public interface Migration {

  /**
   * Executes this migration step.
   */
  void up();
}
