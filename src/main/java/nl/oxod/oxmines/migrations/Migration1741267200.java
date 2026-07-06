package nl.oxod.oxmines.migrations;

import org.bukkit.configuration.ConfigurationSection;

import nl.oxod.oxmines.OxMines;
import nl.oxod.oxmines.mine.MinesFile;

/**
 * Extracts mine definitions from config.yml into the dedicated mines.yml.
 *
 * <p>Before this migration the plugin stored all mine data under the
 * {@code mines} key in config.yml. This step copies that data into
 * mines.yml and removes it from config.yml so that config.yml can be
 * reserved for actual plugin settings.
 */
public class Migration1741267200 implements Migration {

  @Override
  public void up() {
    ConfigurationSection oldMines = OxMines.getInstance().getConfig()
        .getConfigurationSection("mines");
    if (oldMines == null || oldMines.getKeys(false).isEmpty()) {
      return;
    }

    if (MinesFile.hasMineData()) {
      OxMines.getInstance().getLogger().info(
          "mines.yml already has data, skipping migration");
      return;
    }

    OxMines.getInstance().getLogger().info(
        "Running migration: extracting mines from config.yml...");

    for (String key : oldMines.getKeys(false)) {
      MinesFile.set("mines." + key, oldMines.get("mines." + key));
    }

    MinesFile.save();

    OxMines.getInstance().getConfig().set("mines", null);
    OxMines.getInstance().saveConfig();

    OxMines.getInstance().getLogger().info(
        "Migration complete: "
            + oldMines.getKeys(false).size() + " mine(s) moved to mines.yml");
  }
}
