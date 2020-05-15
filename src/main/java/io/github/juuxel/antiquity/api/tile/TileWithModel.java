package io.github.juuxel.antiquity.api.tile;

import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.renderer.Tesselator;
import io.github.juuxel.antiquity.api.level.ExtendedLevel;
import io.github.juuxel.antiquity.api.registry.Registry;
import io.github.juuxel.antiquity.api.rendering.Texture;
import io.github.juuxel.antiquity.api.rendering.model.Model;
import io.github.juuxel.antiquity.api.rendering.model.ModelLoader;
import io.github.juuxel.antiquity.api.tile.state.TileState;
import io.github.juuxel.antiquity.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TileWithModel extends Tile implements ExtendedTile {
    private final @Nullable Identifier modelId;

    public TileWithModel(int id, Identifier modelId) {
        super(id);
        this.modelId = modelId;
    }

    public TileWithModel(int id, int tex, Identifier modelId) {
        super(id, tex);
        this.modelId = modelId;
    }

    public TileWithModel(int id) {
        this(id, null);
    }

    public TileWithModel(int id, int tex) {
        this(id, tex, null);
    }

    private Identifier getModelId() {
        if (modelId != null) {
            return modelId;
        }

        Identifier registryId = Registry.TILE.getId(this);
        if (registryId != null) {
            return registryId;
        }

        throw new IllegalStateException("No model ID provided for unregistered tile " + id);
    }

    public Model getModel(TileState state) {
        return ModelLoader.getTileModel(getModelId(), state);
    }

    @Override
    public Collection<String> getAllTextures() {
        Set<String> textures = new HashSet<>();

        for (Model model : ModelLoader.getAllTileModels(getModelId(), this)) {
            textures.addAll(model.getResolvedTextures());
        }

        return textures;
    }

    @Override
    public Texture getParticleTexture() {
        return getModel(getDefaultState()).resolveTexture("#particle");
    }

    @Override
    public void renderFace(Tesselator tesselator, int x, int y, int z, int face) {
        renderFace(tesselator, x, y, z, getDefaultState(), Direction.values()[face], false);
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
            rendered |= renderFace(tesselator, x, y, z, ExtendedLevel.of(level).getTileState(x, y, z), face, shouldCull);
        }

        return rendered;
    }

    private boolean renderFace(Tesselator tesselator, int x, int y, int z, TileState state, Direction face, boolean shouldCull) {
        return getModel(state).render(tesselator, x, y, z, face, shouldCull);
    }
}
