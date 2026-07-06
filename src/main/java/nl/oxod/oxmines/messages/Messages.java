package nl.oxod.oxmines.messages;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import nl.oxod.oxmines.OxMines;

/**
 * Centralized message manager for OxMines.
 *
 * <p>
 * Loads messages from {@code messages.yml} and provides convenient methods
 * to send MiniMessage-formatted messages to players with placeholder
 * replacement.
 *
 * <p>
 * Placeholders use the format {@code {key}} and are replaced via
 * alternating key-value string pairs.
 * Formatting uses <a href="https://docs.advntr.dev/minimessage/format.html">
 * MiniMessage</a> syntax (e.g., {@code <red>}, {@code <gold>}, {@code <gray>}).
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
      YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
          new InputStreamReader(defaultStream, StandardCharsets.UTF_8));
      messages.setDefaults(defaults);
    }
  }

  /**
   * Returns the raw (MiniMessage) string for a key, without parsing or
   * placeholder replacement. Useful when a message value is used as a
   * placeholder inside another message.
   *
   * @param key the message key
   * @return the raw string, or the key itself if not found
   */
  public static String raw(String key) {
    if (messages != null) {
      String val = messages.getString(key);
      if (val != null) {
        return val;
      }
    }
    return key;
  }

  /**
   * Retrieves a formatted {@link Component} for the given message key.
   *
   * @param key          the message key in messages.yml
   * @param replacements alternating placeholder-value pairs
   *                     (e.g., {@code "mine", "foo", "time", "30"})
   * @return the formatted component with placeholders replaced
   */
  public static Component get(String key, String... replacements) {
    String msg;

    if (messages != null) {
      msg = messages.getString(key);
    } else {
      msg = null;
    }

    if (msg == null) {
      return MiniMessage.miniMessage().deserialize(
          "<red>Missing message: " + key + "</red>");
    }

    for (int i = 0; i < replacements.length - 1; i += 2) {
      msg = msg.replace("{" + replacements[i] + "}",
          replacements[i + 1]);
    }

    return MiniMessage.miniMessage().deserialize(msg);
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
