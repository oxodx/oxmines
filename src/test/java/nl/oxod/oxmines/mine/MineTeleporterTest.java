package nl.oxod.oxmines.mine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

class MineTeleporterTest {

  @Test
  void teleportToTopToHighestPlusOne() {
    Player player = mock(Player.class);
    World world = mock(World.class);

    when(player.getWorld()).thenReturn(world);
    when(player.getLocation()).thenReturn(new Location(world, 5, 0, 5));
    when(player.teleport(ArgumentMatchers.<Location>any())).thenReturn(true);

    Location pos1 = new Location(world, 0, 10, 0);
    Location pos2 = new Location(world, 10, 20, 10);

    boolean result = MineTeleporter.teleportToTop(player, pos1, pos2);

    assertTrue(result);
    verify(player).teleport(argThat((Location loc) -> {
      assertEquals(21.0, loc.getY(), 0.001);
      return loc.getWorld() == world;
    }));
  }

  @Test
  void teleportToTopPreservesXzPosition() {
    Player player = mock(Player.class);
    World world = mock(World.class);

    when(player.getWorld()).thenReturn(world);
    when(player.getLocation()).thenReturn(new Location(world, 42.5, 0, 78.3));
    when(player.teleport(ArgumentMatchers.<Location>any())).thenReturn(true);

    Location pos1 = new Location(world, 0, 5, 0);
    Location pos2 = new Location(world, 10, 15, 10);

    MineTeleporter.teleportToTop(player, pos1, pos2);

    verify(player).teleport(argThat((Location loc) -> {
      assertEquals(42.5, loc.getX(), 0.001);
      assertEquals(78.3, loc.getZ(), 0.001);
      return true;
    }));
  }

  @Test
  void teleportToTopHandlesReversedCorners() {
    Player player = mock(Player.class);
    World world = mock(World.class);

    when(player.getWorld()).thenReturn(world);
    when(player.getLocation()).thenReturn(new Location(world, 5, 0, 5));
    when(player.teleport(ArgumentMatchers.<Location>any())).thenReturn(true);

    Location pos1 = new Location(world, 10, 30, 10);
    Location pos2 = new Location(world, 0, 5, 0);

    MineTeleporter.teleportToTop(player, pos1, pos2);

    verify(player).teleport(argThat((Location loc) -> {
      assertEquals(31.0, loc.getY(), 0.001);
      return true;
    }));
  }

  @Test
  void teleportToTopSingleBlockMine() {
    Player player = mock(Player.class);
    World world = mock(World.class);

    when(player.getWorld()).thenReturn(world);
    when(player.getLocation()).thenReturn(new Location(world, 5, 0, 5));
    when(player.teleport(ArgumentMatchers.<Location>any())).thenReturn(true);

    Location pos1 = new Location(world, 10, 10, 10);
    Location pos2 = new Location(world, 10, 10, 10);

    MineTeleporter.teleportToTop(player, pos1, pos2);

    verify(player).teleport(argThat((Location loc) -> {
      assertEquals(11.0, loc.getY(), 0.001);
      return true;
    }));
  }
}
