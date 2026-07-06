package nl.oxod.oxmines.migrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

class Migration1741267200Test {

  @Test
  void transferMinesCopiesAllMineKeys() {
    YamlConfiguration source = new YamlConfiguration();
    source.set("mines.stone_mine.pos1", "loc1");
    source.set("mines.stone_mine.pos2", "loc2");
    source.set("mines.stone_mine.regenInterval", 30);
    source.set("mines.stone_mine.announceRegen", true);
    source.set("mines.stone_mine.resetWhenEmpty", false);
    source.set("mines.deep_mine.pos1", "deep1");
    source.set("mines.deep_mine.regenInterval", 60);

    YamlConfiguration target = new YamlConfiguration();
    ConfigurationSection oldMines =
        source.getConfigurationSection("mines");

    int count = Migration1741267200.transferMines(oldMines, target);

    assertEquals(2, count);
    assertEquals("loc1", target.getString("mines.stone_mine.pos1"));
    assertEquals("loc2", target.getString("mines.stone_mine.pos2"));
    assertEquals(30, target.getInt("mines.stone_mine.regenInterval"));
    assertTrue(target.getBoolean("mines.stone_mine.announceRegen"));
    assertEquals("deep1", target.getString("mines.deep_mine.pos1"));
    assertEquals(60, target.getInt("mines.deep_mine.regenInterval"));
  }

  @Test
  void transferMinesUsesRelativeKeys() {
    YamlConfiguration source = new YamlConfiguration();
    source.set("mines.only_mine.blocks.stone", 60);
    source.set("mines.only_mine.blocks.dirt", 40);

    YamlConfiguration target = new YamlConfiguration();
    ConfigurationSection oldMines =
        source.getConfigurationSection("mines");

    Migration1741267200.transferMines(oldMines, target);

    ConfigurationSection blocks = target.getConfigurationSection(
        "mines.only_mine.blocks");
    assertEquals(60, blocks.getInt("stone"));
    assertEquals(40, blocks.getInt("dirt"));
  }

  @Test
  void transferMinesReturnsNullSectionWhenNoMinesKey() {
    YamlConfiguration source = new YamlConfiguration();
    source.set("misc.setting", "value");

    ConfigurationSection oldMines =
        source.getConfigurationSection("mines");

    assertNull(oldMines);
  }
}
