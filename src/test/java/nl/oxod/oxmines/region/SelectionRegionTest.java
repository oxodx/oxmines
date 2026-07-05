package nl.oxod.oxmines.region;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SelectionRegionTest {

  @Test
  void defaultRegion_hasNullPositions() {
    SelectionRegion region = new SelectionRegion();
    assertNull(region.pos1);
    assertNull(region.pos2);
  }

  @Test
  void setPositions_storesVectors() {
    SelectionRegion region = new SelectionRegion();
    region.pos1 = new Vector3(1, 2, 3);
    region.pos2 = new Vector3(4, 5, 6);

    assertEquals(1, region.pos1.getX());
    assertEquals(2, region.pos1.getY());
    assertEquals(3, region.pos1.getZ());
    assertEquals(4, region.pos2.getX());
    assertEquals(5, region.pos2.getY());
    assertEquals(6, region.pos2.getZ());
  }

  @Test
  void partialPosition_allowsNullPos2() {
    SelectionRegion region = new SelectionRegion();
    region.pos1 = new Vector3(1, 2, 3);
    assertNotNull(region.pos1);
    assertNull(region.pos2);
  }
}
