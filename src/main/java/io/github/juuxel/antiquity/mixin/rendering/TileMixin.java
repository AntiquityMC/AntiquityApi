package io.github.juuxel.antiquity.mixin.rendering;

import com.mojang.minecraft.renderer.Tesselator;
import io.github.juuxel.antiquity.api.rendering.TerrainAtlas;
import io.github.juuxel.antiquity.api.tile.ExtendedTile;
import io.github.juuxel.antiquity.impl.rendering.TerrainAtlasImpl;
import com.mojang.minecraft.level.tile.Tile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Tile.class)
abstract class TileMixin implements ExtendedTile {
    @Unique
    private int antiquity_method_421_cachedSide;

    @Inject(method = "method_421", at = @At("HEAD"))
    private void antiquity_onMethod_421(Tesselator arg, int x, int y, int z, int side, int tex, CallbackInfo info) {
        this.antiquity_method_421_cachedSide = side;
    }

    @ModifyVariable(method = "method_421", at = @At(value = "CONSTANT", args = { "intValue=4" }, ordinal = 0), argsOnly = true, ordinal = 4)
    private int antiquity_offsetTexture(int texture) {
        String texturePath = getTexturePath(antiquity_method_421_cachedSide);
        return texture + TerrainAtlasImpl.getTextureOffset(texturePath);
    }

    @Redirect(method = "renderBackFace", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/level/tile/Tile;getTextureForSide(I)I"))
    private int antiquity_replaceBackFaceTextureId(Tile tile, int side) {
        return ((TileAccessor) tile).callGetTextureForSide(side) + TerrainAtlasImpl.getTextureOffset(ExtendedTile.of(tile).getTexturePath(side));
    }

    // TODO: Investigate renderBackFace
    @ModifyConstant(method = "method_421", constant = @Constant(floatValue = 256), slice = @Slice(from = @At(value = "CONSTANT", args = "floatValue=256", ordinal = 2)))
    private float antiquity_modifyV(float original) {
        return TerrainAtlas.INSTANCE.getHeight();
    }
}
