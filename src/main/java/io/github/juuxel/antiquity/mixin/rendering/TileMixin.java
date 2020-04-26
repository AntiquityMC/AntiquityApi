package io.github.juuxel.antiquity.mixin.rendering;

import io.github.juuxel.antiquity.api.rendering.TerrainAtlas;
import io.github.juuxel.antiquity.api.tile.ExtendedTile;
import io.github.juuxel.antiquity.impl.rendering.TerrainAtlasImpl;
import com.mojang.minecraft.level.tile.Tile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(Tile.class)
abstract class TileMixin implements ExtendedTile {
    @ModifyVariable(method = "method_421", at = @At("HEAD"), argsOnly = true, ordinal = 4)
    private int antiquity_offsetTexture(int texture) {
        return texture + TerrainAtlasImpl.getTextureOffset(getTexturePath());
    }

    @Redirect(method = "renderBackFace", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/level/tile/Tile;getTextureForSide(I)I"))
    private int antiquity_replaceBackFaceTextureId(Tile tile, int side) {
        return ((TileAccessor) tile).callGetTextureForSide(side) + TerrainAtlasImpl.getTextureOffset(ExtendedTile.of(tile).getTexturePath());
    }

    // TODO: Investigate renderBackFace
    @ModifyConstant(method = "method_421", constant = @Constant(floatValue = 256), slice = @Slice(from = @At(value = "CONSTANT", args = "floatValue=256", ordinal = 2)))
    private float modifyV(float original) {
        return TerrainAtlas.INSTANCE.getHeight();
    }
}
