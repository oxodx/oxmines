package nl.oxod.oxmines.region;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.junit.jupiter.api.Test;

class BlockSelectorTest {

  @Test
  void getBlocksSingleBlock() {
    World world = mock(World.class);
    Block block = mock(Block.class);
    when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);

    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 0, 0, 0);

    List<Block> blocks = BlockSelector.getBlocks(pos1, pos2, world);

    assertEquals(1, blocks.size());
  }

  @Test
  void getBlocksThreeByThreeByThree() {
    World world = mock(World.class);
    Block block = mock(Block.class);
    when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);

    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 2, 2, 2);

    List<Block> blocks = BlockSelector.getBlocks(pos1, pos2, world);

    assertEquals(27, blocks.size());
  }

  @Test
  void getBlocksHandlesReversedCoordinates() {
    World world = mock(World.class);
    Block block = mock(Block.class);
    when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);

    Location pos1 = new Location(world, 5, 5, 5);
    Location pos2 = new Location(world, 3, 3, 3);

    List<Block> blocks = BlockSelector.getBlocks(pos1, pos2, world);

    assertEquals(27, blocks.size());
  }

  @Test
  void getBlocksNegativeCoordinates() {
    World world = mock(World.class);
    Block block = mock(Block.class);
    when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);

    Location pos1 = new Location(world, -5, -5, -5);
    Location pos2 = new Location(world, -3, -3, -3);

    List<Block> blocks = BlockSelector.getBlocks(pos1, pos2, world);

    assertEquals(27, blocks.size());
  }

  @Test
  void getBlocksTenByTenByTen() {
    World world = mock(World.class);
    Block block = mock(Block.class);
    when(world.getBlockAt(anyInt(), anyInt(), anyInt())).thenReturn(block);

    Location pos1 = new Location(world, 0, 0, 0);
    Location pos2 = new Location(world, 9, 9, 9);

    List<Block> blocks = BlockSelector.getBlocks(pos1, pos2, world);

    assertEquals(1000, blocks.size());
  }
}
