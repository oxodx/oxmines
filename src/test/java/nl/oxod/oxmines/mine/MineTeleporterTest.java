package nl.oxod.oxmines.mine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

class MineTeleporterTest {

  @Test
  void teleportToTop_teleportsToHighestYPlusOne() {
    Player player = mock(Player.class);
    World world = mock(World.class);

    when(player.getWorld()).thenReturn(world);
    when(player.getLocation()).thenReturn(new Location(world, 5, 0, 5));
    when(player.teleport(ArgumentMatchers.<Location>any())).thenReturn(true);

    Location pos1 = new Location(world, 0, 10, 0);
    Location pos2 = new Location(world, 10, 20, 10);

    boolean result = MineTeleporter.teleportToTop(player, pos1, pos2);

    assertTrue(result);
    verify(player).teleport(argThat((Location loc) ->
        loc.getX() == 5.0
        && loc.getY() == 21.0
        && loc.getZ() == 5.0
        && loc.getWorld() == world));
  }

  @Test
  void teleportToTop_preservesXZPosition() {
    Player player = mock(Player.class);
    World world = mock(World.class);

    when(player.getWorld()).thenReturn(world);
    when(player.getLocation()).thenReturn(new Location(world, 42.5, 0, 78.3));
    when(player.teleport(ArgumentMatchers.<Location>any())).thenReturn(true);

    Location pos1 = new Location(world, 0, 5, 0);
    Location pos2 = new Location(world, 10, 15, 10);

    MineTeleporter.teleportToTop(player, pos1, pos2);

    verify(player).teleport(argThat((Location loc) ->
        loc.getX() == 42.5
        && loc.getZ() == 78.3));
  }

  @Test
  void teleportToTop_handlesReversedCorners() {
    Player player = mock(Player.class);
    World world = mock(World.class);

    when(player.getWorld()).thenReturn(world);
    when(player.getLocation()).thenReturn(new Location(world, 5, 0, 5));
    when(player.teleport(ArgumentMatchers.<Location>any())).thenReturn(true);

    Location pos1 = new Location(world, 10, 30, 10);
    Location pos2 = new Location(world, 0, 5, 0);

    MineTeleporter.teleportToTop(player, pos1, pos2);

    verify(player).teleport(argThat((Location loc) -> loc.getY() == 31.0));
  }

  @Test
  void teleportToTop_singleBlockMine() {
    Player player = mock(Player.class);
    World world = mock(World.class);

    when(player.getWorld()).thenReturn(world);
    when(player.getLocation()).thenReturn(new Location(world, 5, 0, 5));
    when(player.teleport(ArgumentMatchers.<Location>any())).thenReturn(true);

    Location pos1 = new Location(world, 10, 10, 10);
    Location pos2 = new Location(world, 10, 10, 10);

    MineTeleporter.teleportToTop(player, pos1, pos2);

    verify(player).teleport(argThat((Location loc) -> loc.getY() == 11.0));
  }
}
