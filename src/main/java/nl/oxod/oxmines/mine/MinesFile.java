package nl.oxod.oxmines.mine;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import nl.oxod.oxmines.OxMines;

/**
 * Static config manager for the {@code mines.yml} file.
 *
 * <p>Follows the same pattern as {@link nl.oxod.oxmines.messages.Messages}
 * &mdash; bundled defaults are merged with the user's on-disk copy.
 */
public final class MinesFile {
  private static FileConfiguration mines;

  private MinesFile() {
  }

  /**
   * Loads (or reloads) mines from the plugin data folder, merging with
   * the bundled defaults from the jar.
   */
  public static void load() {
    File file = new File(OxMines.getInstance().getDataFolder(), "mines.yml");

    if (!file.exists()) {
      OxMines.getInstance().saveResource("mines.yml", false);
    }

    mines = YamlConfiguration.loadConfiguration(file);

    InputStream defaultStream = OxMines.getInstance()
        .getResource("mines.yml");
    if (defaultStream != null) {
      YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
          new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
      mines.setDefaults(defaults);
    }
  }

  /**
   * Persists the in-memory mine data to disk.
   */
  public static void save() {
    try {
      mines.save(new File(OxMines.getInstance().getDataFolder(), "mines.yml"));
    } catch (Exception e) {
      OxMines.getInstance().getLogger().severe("Could not save mines.yml!");
    }
  }

  /**
   * Returns the value at the given path, or null.
   */
  public static Object get(String path) {
    return mines.get(path);
  }

  /**
   * Sets the value at the given path.
   */
  public static void set(String path, Object value) {
    mines.set(path, value);
  }

  /**
   * Returns a {@link Location} at the given path, or null.
   */
  public static Location getLocation(String path) {
    return mines.getLocation(path);
  }

  /**
   * Returns a boolean at the given path, or false.
   */
  public static boolean getBoolean(String path) {
    return mines.getBoolean(path);
  }

  /**
   * Returns an int at the given path, or 0.
   */
  public static int getInt(String path) {
    return mines.getInt(path);
  }

  /**
   * Returns a {@link ConfigurationSection} at the given path, or null.
   */
  public static ConfigurationSection getConfigurationSection(String path) {
    return mines.getConfigurationSection(path);
  }

  /**
   * Returns the underlying {@link FileConfiguration} for direct access.
   */
  public static FileConfiguration getHandle() {
    return mines;
  }

  /**
   * Returns true when the loaded config contains at least one mine entry.
   */
  public static boolean hasMineData() {
    ConfigurationSection section = mines.getConfigurationSection("mines");
    return section != null && !section.getKeys(false).isEmpty();
  }
}
