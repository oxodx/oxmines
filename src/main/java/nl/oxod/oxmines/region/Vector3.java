package nl.oxod.oxmines.region;

/**
 * Represents a 3D vector with coordinates.
 */
public class Vector3 {
  public double posX;
  public double posY;
  public double posZ;

  /**
   * Creates a new Vector3.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   */
  public Vector3(double x, double y, double z) {
    this.posX = x;
    this.posY = y;
    this.posZ = z;
  }

  public double getX() {
    return posX;
  }

  public double getY() {
    return posY;
  }

  public double getZ() {
    return posZ;
  }

  @Override
  public String toString() {
    return posX + ", " + posY + ", " + posZ;
  }
}
