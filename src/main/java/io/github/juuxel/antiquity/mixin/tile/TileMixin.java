package io.github.juuxel.antiquity.mixin.tile;

import io.github.juuxel.antiquity.api.tile.ExtendedTile;
import com.mojang.minecraft.level.tile.Tile;
import io.github.juuxel.antiquity.api.tile.state.TileStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Tile.class)
abstract class TileMixin implements ExtendedTile {
    @Shadow
    public Tile.SoundGroup soundGroup;

    @Unique
    private TileStateManager antiquity_stateManager;

    @Inject(method = "<init>(I)V", at = @At("RETURN"))
    private void antiquity_onConstruct(int id, CallbackInfo info) {
        // Set the default sound group to NONE because it's a bit annoying to set currently
        soundGroup = Tile.SoundGroup.NONE;

        TileStateManager.Builder builder = new TileStateManager.Builder((Tile) (Object) this);
        appendProperties(builder);
        antiquity_stateManager = builder.build();
    }

    @Override
    public TileStateManager getStateManager() {
        return antiquity_stateManager;
    }
}
