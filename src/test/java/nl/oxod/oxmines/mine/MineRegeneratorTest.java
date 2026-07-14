package nl.oxod.oxmines.mine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.SecureRandom;
import java.util.List;

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

    int stoneCount = 0;
    int iterations = 10000;

    for (int i = 0; i < iterations; i++) {
      Material result = MineRegenerator.selectBlock(types, weights, 100,
          random);
      assertNotNull(result);
      if (result == Material.STONE) {
        stoneCount++;
      }
    }

    double stonePct = stoneCount * 100.0 / iterations;
    assertEquals(50.0, stonePct, 3.0);
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

  @Test
  void selectBlock_dominantWeight_alwaysSelectsDominant() {
    List<Material> types = List.of(Material.STONE, Material.DIRT);
    List<Integer> weights = List.of(999, 1);
    SecureRandom random = new SecureRandom();
    int totalWeight = 1000;

    int dirtCount = 0;
    int iterations = 100000;

    for (int i = 0; i < iterations; i++) {
      Material result = MineRegenerator.selectBlock(types, weights,
          totalWeight, random);
      if (result == Material.DIRT) {
        dirtCount++;
      }
    }

    double dirtPct = dirtCount * 100.0 / iterations;
    // Dirt: 1/1000 = 0.1%
    assertEquals(0.1, dirtPct, 0.1);
  }

  @Test
  void selectBlock_uniformWeights_allBlocksSelected() {
    List<Material> types = List.of(Material.STONE, Material.DIRT,
        Material.COBBLESTONE, Material.OAK_PLANKS, Material.SAND);
    List<Integer> weights = List.of(1, 1, 1, 1, 1);
    SecureRandom random = new SecureRandom();

    java.util.Set<Material> seen = new java.util.HashSet<>();
    for (int i = 0; i < 1000; i++) {
      Material result = MineRegenerator.selectBlock(types, weights, 5,
          random);
      assertNotNull(result);
      seen.add(result);
    }

    assertEquals(5, seen.size(),
        "All five block types should be selected at least once");
  }

  @Test
  void selectBlock_largeWeights_normalizesCorrectly() {
    List<Material> types = List.of(Material.STONE, Material.DIRT);
    List<Integer> weights = List.of(750, 250);
    SecureRandom random = new SecureRandom();
    int totalWeight = 1000;

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
    assertEquals(75.0, stonePct, 2.0);
  }

  @Test
  void selectBlock_neverReturnsNull() {
    List<Material> types = List.of(Material.STONE, Material.DIRT,
        Material.COBBLESTONE);
    List<Integer> weights = List.of(1, 1, 1);
    SecureRandom random = new SecureRandom();

    for (int i = 0; i < 1000; i++) {
      Material result = MineRegenerator.selectBlock(types, weights, 3,
          random);
      assertNotNull(result,
          "selectBlock should never return null");
    }
  }

  @Test
  void selectBlock_alwaysReturnsValidType() {
    List<Material> types = List.of(Material.STONE, Material.DIRT);
    List<Integer> weights = List.of(60, 40);
    SecureRandom random = new SecureRandom();
    java.util.Set<Material> valid = java.util.Set.of(
        Material.STONE, Material.DIRT);

    for (int i = 0; i < 1000; i++) {
      Material result = MineRegenerator.selectBlock(types, weights, 100,
          random);
      assertTrue(valid.contains(result),
          "Result should always be one of the configured types");
    }
  }

  @Test
  void selectBlock_minimalWeights_distributesCorrectly() {
    List<Material> types = List.of(Material.STONE, Material.DIRT);
    List<Integer> weights = List.of(1, 2);
    SecureRandom random = new SecureRandom();

    int stoneCount = 0;
    int iterations = 100000;

    for (int i = 0; i < iterations; i++) {
      Material result = MineRegenerator.selectBlock(types, weights, 3,
          random);
      if (result == Material.STONE) {
        stoneCount++;
      }
    }

    double stonePct = stoneCount * 100.0 / iterations;
    // Stone: 1/3 = ~33.3%
    assertEquals(33.3, stonePct, 2.0);
  }
}
