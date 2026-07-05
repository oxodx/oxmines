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
import nl.oxod.oxmines.mine.TimerLoader;
import nl.oxod.oxmines.region.SelectionManager;

/**
 * Main plugin class for OxMines - auto-regenerating mining areas.
 *
 * <p>See <a href="https://oxod.nl">oxod.nl</a> for more info.</p>
 */
public class OxMines extends JavaPlugin implements Listener {
  private static OxMines instance;

  public static OxMines getInstance() {
    return instance;
  }

  public static boolean worldeditEnabled = true;

  @Override
  public void onEnable() {
    if (getServer().getPluginManager()
        .getPlugin("WorldEdit") == null) {
      getLogger().log(Level.WARNING, "WorldEdit not detected!");
      worldeditEnabled = false;
    }

    instance = this;

    getConfig().options().copyDefaults();
    saveDefaultConfig();

    Messages.load();

    TimerLoader.loadAll();

    getServer().getPluginManager().registerEvents(new WandListener(), this);
    getServer().getPluginManager().registerEvents(this, this);

    getCommand("oxmines").setExecutor(new CommandManager());
    getCommand("oxmines").setTabCompleter(new CommandManager());
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
