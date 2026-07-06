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

public final class MinesFile {
  private static FileConfiguration mines;

  private MinesFile() {
  }

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

  public static void save() {
    try {
      mines.save(new File(OxMines.getInstance().getDataFolder(), "mines.yml"));
    } catch (Exception e) {
      OxMines.getInstance().getLogger().severe("Could not save mines.yml!");
    }
  }

  public static Object get(String path) {
    return mines.get(path);
  }

  public static void set(String path, Object value) {
    mines.set(path, value);
  }

  public static Location getLocation(String path) {
    return mines.getLocation(path);
  }

  public static boolean getBoolean(String path) {
    return mines.getBoolean(path);
  }

  public static int getInt(String path) {
    return mines.getInt(path);
  }

  public static ConfigurationSection getConfigurationSection(String path) {
    return mines.getConfigurationSection(path);
  }

  public static boolean hasMineData() {
    ConfigurationSection section = mines.getConfigurationSection("mines");
    return section != null && !section.getKeys(false).isEmpty();
  }
}
