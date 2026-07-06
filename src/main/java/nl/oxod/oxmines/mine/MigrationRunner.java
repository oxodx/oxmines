package nl.oxod.oxmines.mine;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.oxod.oxmines.OxMines;

public final class MigrationRunner {
  private static final File MIGRATIONS_DIR = new File(
      OxMines.getInstance().getDataFolder(), "migrations");
  private static final File APPLIED_DIR = new File(MIGRATIONS_DIR, ".applied");

  private MigrationRunner() {
  }

  public static void run() {
    APPLIED_DIR.mkdirs();

    // Migration 1783340391: extract mines from config.yml to mines.yml
    if (requiresMigration("1783340391")) {
      run_1783340391();
    }
  }

  private static boolean requiresMigration(String name) {
    return !new File(APPLIED_DIR, name + ".yml").exists();
  }

  private static void markApplied(String name) {
    try {
      new File(APPLIED_DIR, name + ".yml").createNewFile();
    } catch (IOException e) {
      OxMines.getInstance().getLogger().log(Level.WARNING,
          "Failed to mark migration " + name + " as applied", e);
    }
  }

  private static void run_1783340391() {
    ConfigurationSection oldMines = OxMines.getInstance().getConfig()
        .getConfigurationSection("mines");
    if (oldMines == null || oldMines.getKeys(false).isEmpty()) {
      markApplied("1783340391");
      return;
    }

    if (MinesFile.hasMineData()) {
      OxMines.getInstance().getLogger().info(
          "mines.yml already has data, skipping 1783340391 migration");
      markApplied("1783340391");
      return;
    }

    OxMines.getInstance().getLogger().info(
        "Running migration 1783340391: extracting mines from config.yml...");

    for (String key : oldMines.getKeys(false)) {
      MinesFile.set("mines." + key, oldMines.get("mines." + key));
    }

    MinesFile.save();

    OxMines.getInstance().getConfig().set("mines", null);
    OxMines.getInstance().saveConfig();

    // Write a snapshot of migrated data as the migration record
    File migrationFile = new File(MIGRATIONS_DIR, "1783340391.yml");
    if (!migrationFile.exists()) {
      try {
        YamlConfiguration record = new YamlConfiguration();
        record.set("description",
            "Extract mine definitions from config.yml into dedicated mines.yml");
        record.set("migratedMines", oldMines.getKeys(false).size());
        record.save(migrationFile);
      } catch (Exception e) {
        OxMines.getInstance().getLogger().log(Level.WARNING,
            "Failed to write migration record", e);
      }
    }

    markApplied("1783340391");

    OxMines.getInstance().getLogger().info(
        "Migration 1783340391 complete: "
            + oldMines.getKeys(false).size() + " mine(s) moved to mines.yml");
  }
}
