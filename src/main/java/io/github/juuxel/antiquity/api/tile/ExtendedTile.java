package io.github.juuxel.antiquity.api.tile;

import com.mojang.minecraft.level.tile.Tile;

/**
 * An interface implemented on all tiles.
 */
public interface ExtendedTile {
    /**
     * Gets the path of this tile's texture.
     *
     * <p>By default returns {@code /terrain.png}.
     *
     * @return the texture path
     */
    default String getTexturePath() {
        return "/terrain.png";
    }

    /**
     * Converts the tile into an {@code ExtendedTile}.
     *
     * @param tile the tile
     * @return the tile casted to {@code ExtendedTile}
     */
    static ExtendedTile of(Tile tile) {
        return (ExtendedTile) tile;
    }
}
