package nl.oxod.oxmines.migrations;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import nl.oxod.oxmines.OxMines;

public final class MigrationRunner {
  private static final File APPLIED_DIR = new File(
      OxMines.getInstance().getDataFolder(), "migrations" + File.separator + ".applied");

  private MigrationRunner() {
  }

  public static void run() {
    APPLIED_DIR.mkdirs();

    runIfNeeded("1783340391", new Migration_1783340391());
  }

  private static void runIfNeeded(String name, Migration migration) {
    if (new File(APPLIED_DIR, name).exists()) {
      return;
    }

    migration.up();

    try {
      new File(APPLIED_DIR, name).createNewFile();
    } catch (IOException e) {
      OxMines.getInstance().getLogger().log(Level.WARNING,
          "Failed to mark migration " + name + " as applied", e);
    }
  }
}
