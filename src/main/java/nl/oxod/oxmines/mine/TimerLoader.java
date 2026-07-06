package nl.oxod.oxmines.mine;

import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;

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
    try {
      MineScheduler.cancelAll();

      Set<String> keys = Objects.requireNonNull(
          OxMines.getInstance().getConfig()
              .getConfigurationSection("mines"))
          .getKeys(false);

      for (String key : keys) {
        if (OxMines.getInstance().getConfig()
            .get("mines." + key + ".regenInterval") != null
            && OxMines.getInstance().getConfig()
                .getInt("mines." + key + ".regenInterval") > 0) {
          MineScheduler.scheduleRegeneration(key,
              OxMines.getInstance().getConfig()
                  .getInt("mines." + key + ".regenInterval"));
        }
        if (OxMines.getInstance().getConfig()
            .getBoolean("mines." + key + ".resetWhenEmpty")) {
          MineScheduler.scheduleClearCheck(key, 1);
        }
      }
      return true;
    } catch (NullPointerException e) {
      OxMines.getInstance().getLogger()
          .log(Level.INFO, "No mines created.");
      return false;
    }
  }
}
