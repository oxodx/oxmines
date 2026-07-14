package nl.oxod.oxmines.mine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.junit.jupiter.api.Test;

class MineRegeneratorTest {

  @Test
  void selectBlock_singleWeight_returnsOnlyBlock() {
    List<Material> types = List.of(Material.STONE);
    List<Integer> weights = List.of(100);
    SecureRandom random = new SecureRandom();

    Material result = MineRegenerator.selectBlock(types, weights, 100,
        random);

    assertEquals(Material.STONE, result);
  }

  @Test
  void selectBlock_equalWeights_returnsEitherBlock() {
    List<Material> types = List.of(Material.STONE, Material.DIRT);
    List<Integer> weights = List.of(50, 50);
    SecureRandom random = new SecureRandom();

    IntStream.range(0, 200).forEach(i -> {
      Material result = MineRegenerator.selectBlock(types, weights, 100,
          random);
      assertNotNull(result);
    });
  }

  @Test
  void selectBlock_weightsAbove100_normalizesCorrectly() {
    List<Material> types = List.of(Material.STONE, Material.DIRT,
        Material.COBBLESTONE);
    List<Integer> weights = List.of(60, 30, 20);
    SecureRandom random = new SecureRandom();
    int totalWeight = 110;

    int stoneCount = 0;
    int dirtCount = 0;
    int cobbleCount = 0;
    int iterations = 100000;

    for (int i = 0; i < iterations; i++) {
      Material result = MineRegenerator.selectBlock(types, weights,
          totalWeight, random);
      if (result == Material.STONE) {
        stoneCount++;
      } else if (result == Material.DIRT) {
        dirtCount++;
      } else {
        cobbleCount++;
      }
    }

    double stonePct = stoneCount * 100.0 / iterations;
    double dirtPct = dirtCount * 100.0 / iterations;
    double cobblePct = cobbleCount * 100.0 / iterations;

    // Stone: 60/110 = ~54.5%
    assertEquals(54.5, stonePct, 2.0);
    // Dirt: 30/110 = ~27.3%
    assertEquals(27.3, dirtPct, 2.0);
    // Cobblestone: 20/110 = ~18.2%
    assertEquals(18.2, cobblePct, 2.0);
  }

  @Test
  void selectBlock_unevenWeights_distributesProportionally() {
    List<Material> types = List.of(Material.STONE, Material.DIRT);
    List<Integer> weights = List.of(90, 10);
    SecureRandom random = new SecureRandom();
    int totalWeight = 100;

    int stoneCount = 0;
    int iterations = 100000;

    for (int i = 0; i < iterations; i++) {
      Material result = MineRegenerator.selectBlock(types, weights,
          totalWeight, random);
      if (result == Material.STONE) {
        stoneCount++;
      }
    }

    double stonePct = stoneCount * 100.0 / iterations;
    // Stone: 90/100 = 90%
    assertEquals(90.0, stonePct, 2.0);
  }
}
