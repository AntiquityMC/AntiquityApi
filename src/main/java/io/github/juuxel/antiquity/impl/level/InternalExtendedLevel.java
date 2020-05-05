package io.github.juuxel.antiquity.impl.level;

import io.github.juuxel.antiquity.api.level.ExtendedLevel;
import io.github.juuxel.antiquity.api.tile.state.TileState;

public interface InternalExtendedLevel extends ExtendedLevel {
    void setRawTileState(int position, byte state);

    TileState[] getRawStates();
}
