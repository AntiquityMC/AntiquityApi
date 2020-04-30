package io.github.juuxel.antiquity.api.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.renderer.Tesselator;
import io.github.juuxel.antiquity.api.rendering.Texture;
import io.github.juuxel.antiquity.api.rendering.model.Model;
import io.github.juuxel.antiquity.api.rendering.model.ModelLoader;
import io.github.juuxel.antiquity.api.util.Identifier;

import java.util.Collection;

public class TileWithModel extends Tile implements ExtendedTile {
    private final Identifier modelId;

    public TileWithModel(int id, Identifier modelId) {
        super(id);
        this.modelId = modelId;
    }

    public TileWithModel(int id, int tex, Identifier modelId) {
        super(id, tex);
        this.modelId = modelId;
    }

    public Model getModel() {
        return ModelLoader.getModel(modelId);
    }

    @Override
    public Collection<String> getAllTextures() {
        return getModel().getResolvedTextures();
    }

    @Override
    public Texture getParticleTexture() {
        return getModel().resolveTexture("#particle");
    }

    @Override
    public void renderFace(Tesselator tesselator, int x, int y, int z, int face) {
        renderFace(tesselator, x, y, z, Direction.values()[face], false);
    }

    @Override
    public boolean render(Level level, int x, int y, int z, Tesselator tesselator) {
        boolean rendered = false;
        float brightnessX = 0.6f;
        float brightnessY = 0.5f;
        float brightnessZ = 0.8f;

        for (Direction face : Direction.values()) {
            int tx = face.transformX(x);
            int ty = face.transformY(y);
            int tz = face.transformZ(z);
            boolean shouldCull = !shouldRenderFace(level, tx, ty, tz, face.ordinal());
            float brightness = getBrightness(level, tx, ty, tz);
            float brightnessModifier;

            if (face == Direction.UP) {
                brightnessModifier = 1;
            } else {
                switch (face.getAxis()) {
                    case X:
                        brightnessModifier = brightnessX;
                        break;
                    case Y:
                        brightnessModifier = brightnessY;
                        break;
                    case Z:
                    default:
                        brightnessModifier = brightnessZ;
                        break;
                }
            }

            tesselator.color(brightness * brightnessModifier, brightness * brightnessModifier, brightness * brightnessModifier);
            rendered |= renderFace(tesselator, x, y, z, face, shouldCull);
        }

        return rendered;
    }

    private boolean renderFace(Tesselator tesselator, int x, int y, int z, Direction face, boolean shouldCull) {
        return getModel().render(tesselator, x, y, z, face, shouldCull);
    }
}
