package nl.oxod.oxmines.region;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class Vector3Test {

  @Test
  void constructorStoresCoordinates() {
    Vector3 v = new Vector3(1.5, 2.5, 3.5);
    assertEquals(1.5, v.getX());
    assertEquals(2.5, v.getY());
    assertEquals(3.5, v.getZ());
  }

  @Test
  void toStringReturnsFormattedCoordinates() {
    Vector3 v = new Vector3(1.0, 2.0, 3.0);
    assertEquals("1.0, 2.0, 3.0", v.toString());
  }

  @Test
  void getX_returnsCorrectPosition() {
    Vector3 v = new Vector3(10.0, 20.0, 30.0);
    assertEquals(10.0, v.getX());
  }

  @Test
  void negativeCoordinates() {
    Vector3 v = new Vector3(-5.0, -10.0, -15.0);
    assertEquals(-5.0, v.getX());
    assertEquals(-10.0, v.getY());
    assertEquals(-15.0, v.getZ());
    assertEquals("-5.0, -10.0, -15.0", v.toString());
  }

  @Test
  void zeroCoordinates() {
    Vector3 v = new Vector3(0.0, 0.0, 0.0);
    assertEquals(0.0, v.getX());
    assertEquals(0.0, v.getY());
    assertEquals(0.0, v.getZ());
  }
}
