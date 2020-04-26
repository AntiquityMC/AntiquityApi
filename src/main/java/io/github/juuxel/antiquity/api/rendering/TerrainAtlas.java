package io.github.juuxel.antiquity.api.rendering;

import io.github.juuxel.antiquity.impl.rendering.TerrainAtlasImpl;

/**
 * The terrain atlas contains all tile textures.
 * It is the Antiquity API replacement for {@code /terrain.png};
 * with Antiquity, {@code /terrain.png} is only one texture of the atlas.
 */
public interface TerrainAtlas {
    TerrainAtlas INSTANCE = TerrainAtlasImpl.INSTANCE;

    /**
     * Adds a texture path to the terrain atlas.
     *
     * @param path the path; must start with {@code /} and end with {@code .png}
     * @throws NullPointerException     if the path is null
     * @throws IllegalArgumentException if the path is otherwise invalid
     */
    void add(String path);

    /**
     * Gets the height of the terrain atlas.
     * Useful for UV scaling.
     *
     * @return the height
     */
    float getHeight();
}
