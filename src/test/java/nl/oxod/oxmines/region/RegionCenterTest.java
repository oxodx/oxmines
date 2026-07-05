package nl.oxod.oxmines.region;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.Test;

class RegionCenterTest {

  @Test
  void calculateReturnsCenterOfPositiveCoordinates() {
    World world = mock(World.class);
    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 10, 10, 10);

    Location center = RegionCenter.calculate(pos1, pos2, world);

    assertEquals(5, center.getBlockX());
    assertEquals(5, center.getBlockY());
    assertEquals(5, center.getBlockZ());
    assertSame(world, center.getWorld());
  }

  @Test
  void calculateRoundsDownForOddDistance() {
    World world = mock(World.class);
    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 9, 9, 9);

    Location center = RegionCenter.calculate(pos1, pos2, world);

    assertEquals(4, center.getBlockX());
    assertEquals(4, center.getBlockY());
    assertEquals(4, center.getBlockZ());
  }

  @Test
  void calculateHandlesNegativeCoordinates() {
    World world = mock(World.class);
    Location pos1 = new Location(world, -10, -10, -10);
    Location pos2 = new Location(world, 0, 0, 0);

    Location center = RegionCenter.calculate(pos1, pos2, world);

    assertEquals(-5, center.getBlockX());
    assertEquals(-5, center.getBlockY());
    assertEquals(-5, center.getBlockZ());
  }

  @Test
  void calculateHandlesReversedPositions() {
    World world = mock(World.class);
    Location pos1 = new Location(world, 10, 10, 10);
    Location pos2 = new Location(world, 0, 0, 0);

    Location center = RegionCenter.calculate(pos1, pos2, world);

    assertEquals(5, center.getBlockX());
    assertEquals(5, center.getBlockY());
    assertEquals(5, center.getBlockZ());
  }

  @Test
  void calculateSingleBlockRegion() {
    World world = mock(World.class);
    Location pos1 = new Location(world, 5, 5, 5);
    Location pos2 = new Location(world, 5, 5, 5);

    Location center = RegionCenter.calculate(pos1, pos2, world);

    assertEquals(5, center.getBlockX());
    assertEquals(5, center.getBlockY());
    assertEquals(5, center.getBlockZ());
  }
}
