package nl.oxod.oxmines.region;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.Test;

class RegionCenterTest {

  @Test
  void calculate_returnsCenterOfPositiveCoordinates() {
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
  void calculate_roundsDownForOddDistance() {
    World world = mock(World.class);
    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 9, 9, 9);

    Location center = RegionCenter.calculate(pos1, pos2, world);

    assertEquals(4, center.getBlockX());
    assertEquals(4, center.getBlockY());
    assertEquals(4, center.getBlockZ());
  }

  @Test
  void calculate_handlesNegativeCoordinates() {
    World world = mock(World.class);
    Location pos1 = new Location(world, -10, -10, -10);
    Location pos2 = new Location(world, 0, 0, 0);

    Location center = RegionCenter.calculate(pos1, pos2, world);

    assertEquals(-5, center.getBlockX());
    assertEquals(-5, center.getBlockY());
    assertEquals(-5, center.getBlockZ());
  }

  @Test
  void calculate_handlesReversedPositions() {
    World world = mock(World.class);
    Location pos1 = new Location(world, 10, 10, 10);
    Location pos2 = new Location(world, 0, 0, 0);

    Location center = RegionCenter.calculate(pos1, pos2, world);

    assertEquals(5, center.getBlockX());
    assertEquals(5, center.getBlockY());
    assertEquals(5, center.getBlockZ());
  }

  @Test
  void calculate_singleBlockRegion() {
    World world = mock(World.class);
    Location pos1 = new Location(world, 5, 5, 5);
    Location pos2 = new Location(world, 5, 5, 5);

    Location center = RegionCenter.calculate(pos1, pos2, world);

    assertEquals(5, center.getBlockX());
    assertEquals(5, center.getBlockY());
    assertEquals(5, center.getBlockZ());
  }
}
