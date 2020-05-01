package io.github.juuxel.antiquity.api.tile;

import com.mojang.minecraft.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

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

    /**
     * Gets the horizontal direction where the player is looking.
     *
     * @param player the player
     * @return the direction
     */
    public static Direction of(Player player) {
        float rotation = player.yRot % 360;
        if (rotation < 0) rotation += 360;
        int rotationIndex = Math.round(rotation / 90) % 4;

        Direction result;
        switch (rotationIndex) {
            case 0:
                result = NORTH;
                break;
            case 1:
                result = EAST;
                break;
            case 2:
                result = SOUTH;
                break;
            case 3:
            default:
                result = WEST;
                break;
        }

        return result;
    }

    /**
     * Converts a string into a direction.
     *
     * @param string the string form of a direction
     * @return the direction value or null if not found
     */
    public static @Nullable Direction of(String string) {
        switch (string.toLowerCase(Locale.ROOT)) {
            case "north":
                return NORTH;
            case "east":
                return EAST;
            case "south":
                return SOUTH;
            case "west":
                return WEST;
            case "up":
                return UP;
            case "down":
                return DOWN;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }

    public enum Axis {
        X, Y, Z
    }
}
