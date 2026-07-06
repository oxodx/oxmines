package nl.oxod.oxmines.mine;

import java.util.Set;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;

import nl.oxod.oxmines.OxMines;

/**
 * Loads mine schedules from config on startup or reload.
 */
public class TimerLoader {

  /**
   * Reads all mines from config and starts their scheduled tasks.
   *
   * @return true if at least one mine was found
   */
  public static boolean loadAll() {
    MineScheduler.cancelAll();

    ConfigurationSection minesSection = MinesFile.getConfigurationSection("mines");
    if (minesSection == null) {
      OxMines.getInstance().getLogger()
          .log(Level.INFO, "No mines created.");
      return false;
    }

    Set<String> keys = minesSection.getKeys(false);

    for (String key : keys) {
      if (MinesFile.get("mines." + key + ".regenInterval") != null
          && MinesFile.getInt("mines." + key + ".regenInterval") > 0) {
        MineScheduler.scheduleRegeneration(key,
            MinesFile.getInt("mines." + key + ".regenInterval"));
      }
      if (MinesFile.getBoolean("mines." + key + ".resetWhenEmpty")) {
        MineScheduler.scheduleClearCheck(key, 1);
      }
    }
    return true;
  }
}
