package nl.oxod.oxmines.mine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

class RegionCheckerTest {

  @Test
  void isPlayerInRegion_playerInside_returnsTrue() {
    Player player = mock(Player.class);
    World world = mock(World.class);
    when(player.getLocation()).thenReturn(new Location(world, 5, 5, 5));

    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 10, 10, 10);

    assertTrue(RegionChecker.isPlayerInRegion(player, pos1, pos2));
  }

  @Test
  void isPlayerInRegion_playerOutsideX_returnsFalse() {
    Player player = mock(Player.class);
    World world = mock(World.class);
    when(player.getLocation()).thenReturn(new Location(world, 15, 5, 5));

    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 10, 10, 10);

    assertFalse(RegionChecker.isPlayerInRegion(player, pos1, pos2));
  }

  @Test
  void isPlayerInRegion_playerOutsideY_returnsFalse() {
    Player player = mock(Player.class);
    World world = mock(World.class);
    when(player.getLocation()).thenReturn(new Location(world, 5, 15, 5));

    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 10, 10, 10);

    assertFalse(RegionChecker.isPlayerInRegion(player, pos1, pos2));
  }

  @Test
  void isPlayerInRegion_playerOutsideZ_returnsFalse() {
    Player player = mock(Player.class);
    World world = mock(World.class);
    when(player.getLocation()).thenReturn(new Location(world, 5, 5, 15));

    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 10, 10, 10);

    assertFalse(RegionChecker.isPlayerInRegion(player, pos1, pos2));
  }

  @Test
  void isPlayerInRegion_playerOnBoundary_returnsTrue() {
    Player player = mock(Player.class);
    World world = mock(World.class);
    when(player.getLocation()).thenReturn(new Location(world, 0, 0, 0));

    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 10, 10, 10);

    assertTrue(RegionChecker.isPlayerInRegion(player, pos1, pos2));
  }

  @Test
  void isPlayerInRegion_playerAtMaxBoundary_returnsTrue() {
    Player player = mock(Player.class);
    World world = mock(World.class);
    when(player.getLocation()).thenReturn(new Location(world, 10, 10, 10));

    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 10, 10, 10);

    assertTrue(RegionChecker.isPlayerInRegion(player, pos1, pos2));
  }

  @Test
  void isPlayerInRegion_reversedCorners_returnsTrue() {
    Player player = mock(Player.class);
    World world = mock(World.class);
    when(player.getLocation()).thenReturn(new Location(world, 5, 5, 5));

    Location pos1 = new Location(world, 10, 10, 10);
    Location pos2 = new Location(world, 0, 0, 0);

    assertTrue(RegionChecker.isPlayerInRegion(player, pos1, pos2));
  }

  @Test
  void isPlayerInRegion_negativeRegion_returnsTrue() {
    Player player = mock(Player.class);
    World world = mock(World.class);
    when(player.getLocation()).thenReturn(new Location(world, -5, -5, -5));

    Location pos1 = new Location(world, -10, -10, -10);
    Location pos2 = new Location(world, -1, -1, -1);

    assertTrue(RegionChecker.isPlayerInRegion(player, pos1, pos2));
  }
}
