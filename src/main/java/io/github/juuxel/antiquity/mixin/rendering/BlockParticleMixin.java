package io.github.juuxel.antiquity.mixin.rendering;

import com.mojang.minecraft.particle.BlockParticle;
import io.github.juuxel.antiquity.api.rendering.TerrainAtlas;
import io.github.juuxel.antiquity.api.rendering.Texture;
import io.github.juuxel.antiquity.api.tile.ExtendedTile;
import io.github.juuxel.antiquity.impl.rendering.TerrainAtlasImpl;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.particle.Particle;
import com.mojang.minecraft.renderer.Tesselator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockParticle.class)
abstract class BlockParticleMixin extends Particle {
    private BlockParticleMixin(Level level, float x, float y, float z, float xs, float ys, float zs) {
        super(level, x, y, z, xs, ys, zs);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void antiquity_onConstruct(Level level, float x, float y, float z, float xs, float ys, float zs, Tile tile, CallbackInfo info) {
        Texture texture = ExtendedTile.of(tile).getParticleTexture();
        tex = texture.index + TerrainAtlasImpl.getTextureOffset(texture.path);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/minecraft/renderer/Tesselator;vertexUV(FFFFF)V"))
    private void antiquity_redirectVertexUV(Tesselator tesselator, float x, float y, float z, float u, float v) {
        tesselator.vertexUV(x, y, z, u, v * 256f / TerrainAtlas.INSTANCE.getHeight());
    }
}
