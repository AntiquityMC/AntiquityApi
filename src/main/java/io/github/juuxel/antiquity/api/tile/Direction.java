package io.github.juuxel.antiquity.api.tile;

/**
 * The direction enum represents all sides of a tile.
 *
 * <p>The ordinal of a value matches the int code used in tile rendering.
 */
public enum Direction {
    DOWN(0, -1, 0),
    UP(0, 1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    EAST(1, 0, 0);

    private final int offsetX;
    private final int offsetY;
    private final int offsetZ;

    private Direction(int offsetX, int offsetY, int offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    public int offsetX(int x) {
        return x + offsetX;
    }

    public int offsetY(int y) {
        return y + offsetY;
    }

    public int offsetZ(int z) {
        return z + offsetZ;
    }
}
