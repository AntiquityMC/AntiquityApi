package io.github.juuxel.antiquity.api.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.player.Player;
import io.github.juuxel.antiquity.api.rendering.Texture;
import io.github.juuxel.antiquity.api.tile.state.TileState;
import io.github.juuxel.antiquity.api.tile.state.TileStateManager;

import java.util.Collection;
import java.util.Collections;

/**
 * An interface implemented on all tiles.
 */
public interface ExtendedTile {
    String DEFAULT_TEXTURE = "/terrain.png";

    // TODO: JD
    default boolean use(Level level, int x, int y, int z, Direction side, Player player) {
        return false;
    }

    // TODO: JD
    default void onPlaced(Level level, int x, int y, int z, Player player) {}

    default TileState getDefaultState() {
        return getStateManager().getDefaultState();
    }

    default TileStateManager getStateManager() {
        throw new IllegalStateException("Dummy implementation of ExtendedTile.getStateManager() called!");
    }

    // TODO: JD
    default void appendProperties(TileStateManager.Builder builder) {}

    default Texture getParticleTexture() {
        return new Texture(getTexturePath(), ((Tile) this).tex);
    }

    /**
     * Gets the path of this tile's texture.
     *
     * <p>This texture is used for particles, and by default for the sided {@link #getTexturePath(int) getTexturePath()}.
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
     * Returns all textures used by this tile.
     * Used for atlas stitching.
     *
     * <p>Returns a singleton set of {@link #getTexturePath()} by default.
     *
     * @return the collection of all textures used by this tile
     */
    default Collection<String> getAllTextures() {
        return Collections.singleton(getTexturePath());
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
