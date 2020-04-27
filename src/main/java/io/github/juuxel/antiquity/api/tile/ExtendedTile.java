package io.github.juuxel.antiquity.api.tile;

import com.mojang.minecraft.level.tile.Tile;

/**
 * An interface implemented on all tiles.
 */
public interface ExtendedTile {
    String DEFAULT_TEXTURE = "/terrain.png";

    /**
     * Gets the path of this tile's texture.
     *
     * <p>Returns {@link #DEFAULT_TEXTURE} by default.
     *
     * @return the texture path
     */
    default String getTexturePath() {
        return DEFAULT_TEXTURE;
    }

    /**
     * Gets the path of this tile's texture for the specific side.
     *
     * <p>Returns {@link #getTexturePath()} by default.
     *
     * <p>Side indices:
     * <ul>
     *     <li>0: bottom</li>
     *     <li>1: top</li>
     *     <li>2: north</li>
     *     <li>3: south</li>
     *     <li>4: west</li>
     *     <li>5: east</li>
     * </ul>
     *
     * @param side the side of the tile
     * @return the texture path
     */
    default String getTexturePath(int side) {
        return getTexturePath();
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
