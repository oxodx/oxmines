package nl.oxod.plugintemplate;

import org.bukkit.plugin.java.JavaPlugin;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/** Bukkit plugin main class. */
public class PluginTemplate extends JavaPlugin {
  private static PluginTemplate instance;

  @SuppressFBWarnings(value = "MS_EXPOSE_REP", justification = "Bukkit plugin singleton pattern")
  public static PluginTemplate getInstance() {
    return instance;
  }

  @Override
  @SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", justification = "Bukkit plugin singleton pattern")
  public void onEnable() {
    instance = this;

    getConfig().options().copyDefaults();
    saveDefaultConfig();
  }
}
