package io.github.juuxel.antiquity.mixin.level;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import io.github.juuxel.antiquity.api.level.ExtendedLevel;
import io.github.juuxel.antiquity.api.tile.ExtendedTile;
import io.github.juuxel.antiquity.api.tile.state.TileState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Level.class)
abstract class LevelMixin implements ExtendedLevel {
    @Shadow
    public int width;

    @Shadow
    public int depth;

    @Shadow
    public abstract int getTile(int x, int y, int z);

    @Shadow
    public abstract boolean setTileNoNeighborChange(int x, int y, int z, int tile);

    @Shadow
    public abstract boolean setTile(int x, int y, int z, int tile);

    @Unique
    private TileState[] antiquity_tileStates;

    @Inject(method = "setData", at = @At("RETURN"))
    private void antiquity_onSetData(int width, int height, int depth, byte[] blocks, CallbackInfo info) {
        antiquity_tileStates = new TileState[blocks.length];
    }

    @Override
    public TileState getTileState(int x, int y, int z) {
        // (var2 * this.depth + var3) * this.width + var1
        TileState state = antiquity_tileStates[(y * depth + z) * width + x];

        if (state != null) {
            return state;
        }

        int tileId = getTile(x, y, z);
        return tileId == 0 ? TileState.AIR : ExtendedTile.of(Tile.tiles[tileId]).getDefaultState();
    }

    @Override
    public void setTileState(int x, int y, int z, TileState state) {
        setTile(x, y, z, state.getTileId());
        antiquity_tileStates[(y * depth + z) * width + x] = state;
    }

    @Override
    public void setTileStateNoNeighborChange(int x, int y, int z, TileState state) {
        setTileNoNeighborChange(x, y, z, state.getTileId());
        antiquity_tileStates[(y * depth + z) * width + x] = state;
    }
}
