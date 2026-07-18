package nl.oxod.oxmines;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import nl.oxod.oxmines.commands.CommandManager;
import nl.oxod.oxmines.listeners.WandListener;
import nl.oxod.oxmines.messages.Messages;
import nl.oxod.oxmines.migrations.MigrationRunner;
import nl.oxod.oxmines.mine.MinesFile;
import nl.oxod.oxmines.mine.TimerLoader;
import nl.oxod.oxmines.region.SelectionManager;
import org.bstats.bukkit.Metrics;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Main plugin class for OxMines - auto-regenerating mining areas.
 *
 * <p>
 * See <a href="https://oxod.nl">oxod.nl</a> for more info.
 * </p>
 */
public class OxMines extends JavaPlugin implements Listener {
  private static final int BSTATS_PLUGIN_ID = 32718;
  private static OxMines instance;

  @SuppressFBWarnings(
      value = "MS_EXPOSE_REP",
      justification = "Bukkit plugin singleton pattern")
  public static OxMines getInstance() {
    return instance;
  }

  private static boolean worldeditEnabled = true;

  public static boolean isWorldeditEnabled() {
    return worldeditEnabled;
  }

  @Override
  @SuppressFBWarnings(
      value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
      justification = "Bukkit plugin singleton pattern")
  public void onEnable() {
    if (getServer().getPluginManager()
        .getPlugin("WorldEdit") == null) {
      getLogger().log(Level.WARNING, "WorldEdit not detected!");
      worldeditEnabled = false;
    }

    instance = this;

    // Initialize bStats metrics
    new Metrics(this, BSTATS_PLUGIN_ID);

    getConfig().options().copyDefaults();
    saveDefaultConfig();

    Messages.load();
    MinesFile.load();
    MigrationRunner.run();

    TimerLoader.loadAll();

    getServer().getPluginManager().registerEvents(new WandListener(), this);
    getServer().getPluginManager().registerEvents(this, this);

    var cmd = getCommand("oxmines");
    if (cmd != null) {
      cmd.setExecutor(new CommandManager());
      cmd.setTabCompleter(new CommandManager());
    }
  }

  /**
   * Cleans up selection data when a player leaves.
   *
   * @param event the player quit event
   */
  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    if (SelectionManager.getSelection(player) != null) {
      SelectionManager.removeSelection(player);
    }
  }
}