package io.github.juuxel.antiquity.mixin.bugfix;

import com.mojang.minecraft.level.tile.TransparentTile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Makes the boolean parameter in the constructor actually used,
 * as it's supposed to be the value of renderAdjacentFaces.
 */
@Mixin(TransparentTile.class)
abstract class TransparentTileMixin {
    @Shadow
    private boolean renderAdjacentFaces;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void antiquity_onConstruct(int id, int tex, boolean renderAdjacentFaces, CallbackInfo info) {
        this.renderAdjacentFaces = renderAdjacentFaces;
    }
}
