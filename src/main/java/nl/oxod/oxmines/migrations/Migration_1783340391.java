package nl.oxod.oxmines.migrations;

import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.mine.MinesFile;

/**
 * Extracts mine definitions from config.yml into the dedicated mines.yml.
 */
public class Migration_1783340391 implements Migration {

  @Override
  public void up() {
    ConfigurationSection oldMines = OxMines.getInstance().getConfig()
        .getConfigurationSection("mines");
    if (oldMines == null || oldMines.getKeys(false).isEmpty()) {
      return;
    }

    if (MinesFile.hasMineData()) {
      OxMines.getInstance().getLogger().info(
          "mines.yml already has data, skipping 1783340391 migration");
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

    OxMines.getInstance().getLogger().log(Level.INFO, "Migration 1783340391 complete: {0} mine(s) moved to mines.yml",
        oldMines.getKeys(false).size());
  }
}
