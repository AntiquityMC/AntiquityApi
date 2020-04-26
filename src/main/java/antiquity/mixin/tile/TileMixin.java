package antiquity.mixin.tile;

import antiquity.api.tile.ExtendedTile;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.level.tile.Tile$SoundGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Tile.class)
abstract class TileMixin implements ExtendedTile {
    @SuppressWarnings("ShadowTarget")
    @Shadow
    public Tile$SoundGroup soundGroup;

    @Inject(method = "<init>(I)V", at = @At("RETURN"))
    private void antiquity_onConstruct(int id, CallbackInfo info) {
        // Set the default sound group to NONE because it's a bit annoying to set currently
        soundGroup = Tile$SoundGroup.NONE;
    }
}
