package io.github.juuxel.antiquity.api.level;

import com.mojang.minecraft.level.Level;
import io.github.juuxel.antiquity.api.tile.state.TileState;

/**
 * An interface implemented on all levels.
 */
public interface ExtendedLevel {
    TileState getTileState(int x, int y, int z);

    void setTileState(int x, int y, int z, TileState state);

    void setTileStateNoUpdate(int x, int y, int z, TileState state);

    static ExtendedLevel of(Level level) {
        return (ExtendedLevel) level;
    }
}
