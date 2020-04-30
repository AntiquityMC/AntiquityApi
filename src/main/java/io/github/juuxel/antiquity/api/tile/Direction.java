package io.github.juuxel.antiquity.api.tile;

/**
 * The direction enum represents all sides of a tile.
 *
 * <p>The ordinal of a value matches the int code used in tile rendering.
 */
public enum Direction {
    DOWN(0, -1, 0, Axis.Y),
    UP(0, 1, 0, Axis.Y),
    NORTH(0, 0, -1, Axis.Z),
    SOUTH(0, 0, 1, Axis.Z),
    WEST(-1, 0, 0, Axis.X),
    EAST(1, 0, 0, Axis.X);

    private final int offsetX;
    private final int offsetY;
    private final int offsetZ;
    private final Axis axis;

    Direction(int offsetX, int offsetY, int offsetZ, Axis axis) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.axis = axis;
    }

    public int transformX(int x) {
        return x + offsetX;
    }

    public int transformY(int y) {
        return y + offsetY;
    }

    public int transformZ(int z) {
        return z + offsetZ;
    }

    public Axis getAxis() {
        return axis;
    }

    public enum Axis {
        X, Y, Z
    }
}
