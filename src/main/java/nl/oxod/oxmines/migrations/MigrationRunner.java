package nl.oxod.oxmines.migrations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import nl.oxod.oxmines.OxMines;

public final class MigrationRunner {
  private static final File FILE = new File(
      OxMines.getInstance().getDataFolder(), "migrations.yml");

  private MigrationRunner() {
  }

  public static void run() {
    runIfNeeded("1783340391", new Migration_1783340391());
  }

  private static void runIfNeeded(String name, Migration migration) {
    YamlConfiguration data = load();

    List<String> applied = data.getStringList("applied");
    if (applied.contains(name)) {
      return;
    }

    migration.up();

    applied.add(name);
    data.set("applied", applied);
    save(data);
  }

  private static YamlConfiguration load() {
    YamlConfiguration data = new YamlConfiguration();
    if (FILE.exists()) {
      try {
        data.load(FILE);
      } catch (Exception ignored) {
      }
    }
    if (!data.contains("applied")) {
      data.set("applied", new ArrayList<String>());
    }
    return data;
  }

  private static void save(YamlConfiguration data) {
    try {
      data.save(FILE);
    } catch (Exception e) {
      OxMines.getInstance().getLogger().severe(
          "Could not save migrations.yml!");
    }
  }
}
