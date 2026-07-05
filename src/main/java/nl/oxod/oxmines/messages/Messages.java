package nl.oxod.oxmines.messages;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import nl.oxod.oxmines.OxMines;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Centralized message manager for OxMines.
 *
 * <p>Loads messages from {@code messages.yml} and provides convenient methods
 * to send color-translated, placeholder-replaced messages to players.
 *
 * <p>Placeholders use the format {@code {key}} and are replaced via
 * alternating key-value string pairs.
 * Color codes use {@code &} syntax (e.g., {@code &a} for green).
 */
public final class Messages {
  private static FileConfiguration messages;

  private Messages() {
  }

  /**
   * Loads (or reloads) messages from the plugin data folder, merging with
   * the bundled defaults from the jar.
   */
  public static void load() {
    File file = new File(OxMines.getInstance().getDataFolder(), "messages.yml");

    if (!file.exists()) {
      OxMines.getInstance().saveResource("messages.yml", false);
    }

    messages = YamlConfiguration.loadConfiguration(file);

    InputStream defaultStream = OxMines.getInstance()
        .getResource("messages.yml");
    if (defaultStream != null) {
      YamlConfiguration defaults =
          YamlConfiguration.loadConfiguration(
              new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
      messages.setDefaults(defaults);
    }
  }

  /**
   * Retrieves a formatted message string for the given key.
   *
   * @param key          the message key in messages.yml
   * @param replacements alternating placeholder-value pairs
   *                     (e.g., {@code "mine", "foo", "time", "30"})
   * @return the formatted message with colors translated and placeholders
   *         replaced
   */
  public static String get(String key, String... replacements) {
    String msg;

    if (messages != null) {
      msg = messages.getString(key);
    } else {
      msg = null;
    }

    if (msg == null) {
      return ChatColor.RED + "Missing message: " + key;
    }

    for (int i = 0; i < replacements.length - 1; i += 2) {
      msg = msg.replace("{" + replacements[i] + "}",
          replacements[i + 1]);
    }

    return ChatColor.translateAlternateColorCodes('&', msg);
  }

  /**
   * Sends a formatted message to a player.
   *
   * @param player       the recipient
   * @param key          the message key in messages.yml
   * @param replacements alternating placeholder-value pairs
   */
  public static void send(Player player, String key, String... replacements) {
    player.sendMessage(get(key, replacements));
  }
}
