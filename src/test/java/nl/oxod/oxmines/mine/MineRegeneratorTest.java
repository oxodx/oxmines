package nl.oxod.oxmines.mine;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;

import nl.oxod.oxmines.commands.subcommands.InfoSubCommand;

class MineRegeneratorTest {
  @Test
  void selectsBlockByWeightedRandomValue() {
    Map<Material, Double> blocks = new LinkedHashMap<>();
    blocks.put(Material.STONE, 0.25);
    blocks.put(Material.DIRT, 0.75);

    assertEquals(Material.STONE, MineRegenerator.selectMaterial(blocks, 0.2));
    assertEquals(Material.DIRT, MineRegenerator.selectMaterial(blocks, 0.9));
  }

  @Test
  void fallsBackToHighestWeightWhenNoPositiveWeightsExist() {
    Map<Material, Double> blocks = new LinkedHashMap<>();
    blocks.put(Material.STONE, -1.0);
    blocks.put(Material.DIRT, -2.0);

    assertEquals(Material.STONE, MineRegenerator.selectMaterial(blocks, 0.0));
  }

  @Test
  void formatsPercentagesWithoutTrailingZeros() {
    assertEquals("66.53%", InfoSubCommand.formatPercentage(1000.0, 1503.0));
    assertEquals("33.26%", InfoSubCommand.formatPercentage(500.0, 1503.0));
    assertEquals("0.2%", InfoSubCommand.formatPercentage(3.0, 1503.0));
    assertEquals("50%", InfoSubCommand.formatPercentage(1.0, 2.0));
    assertEquals("33.33%", InfoSubCommand.formatPercentage(1.0, 3.0));
  }
}
