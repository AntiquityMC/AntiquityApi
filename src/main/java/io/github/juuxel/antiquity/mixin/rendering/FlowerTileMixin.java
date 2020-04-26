package io.github.juuxel.antiquity.mixin.rendering;

import io.github.juuxel.antiquity.api.rendering.TerrainAtlas;
import io.github.juuxel.antiquity.api.tile.ExtendedTile;
import io.github.juuxel.antiquity.impl.rendering.TerrainAtlasImpl;
import com.mojang.minecraft.level.tile.FlowerTile;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FlowerTile.class)
abstract class FlowerTileMixin extends Tile implements ExtendedTile {
    private FlowerTileMixin(int var1) {
        super(var1);
    }

    /**
     * @author Juuz
     * @reason Can't inject cleanly due to LVT issues
     */
    @Overwrite
    private void method_443(Tesselator t, float x, float y0, float z) {
        int texture = this.getTextureForSide(15) + TerrainAtlasImpl.getTextureOffset(getTexturePath());
        int textureX = texture % 16 << 4;
        int textureY = texture / 16 << 4;
        float atlasHeight = TerrainAtlas.INSTANCE.getHeight();

        float u0 = (float)textureX / 256.0F;
        float u1 = ((float)textureX + 15.99F) / 256.0F;
        float v0 = (float)textureY / atlasHeight;
        float v1 = ((float)textureY + 15.99F) / atlasHeight;

        float piOverFour = (float) Math.PI / 4f;
        float piOverTwo = (float) Math.PI / 2f;

        for (int i = 0; i < 2; ++i) {
            float x0 = (float) (Mth.sin(i * piOverTwo + piOverFour) * 0.5);
            float z0 = (float) (Mth.cos(i * piOverTwo + piOverFour) * 0.5);
            float x1 = x + 0.5F - x0;
            x0 = x + 0.5F + x0;
            float y1 = y0 + 1.0F;
            float z1 = z + 0.5F - z0;
            z0 = z + 0.5F + z0;
            t.vertexUV(x1, y1, z1, u1, v0);
            t.vertexUV(x0, y1, z0, u0, v0);
            t.vertexUV(x0, y0, z0, u0, v1);
            t.vertexUV(x1, y0, z1, u1, v1);
            t.vertexUV(x0, y1, z0, u1, v0);
            t.vertexUV(x1, y1, z1, u0, v0);
            t.vertexUV(x1, y0, z1, u0, v1);
            t.vertexUV(x0, y0, z0, u1, v1);
        }
    }
}
