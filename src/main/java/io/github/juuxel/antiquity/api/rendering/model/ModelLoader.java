package io.github.juuxel.antiquity.api.rendering.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.renderer.Textures;
import io.github.juuxel.antiquity.api.tile.ExtendedTile;
import io.github.juuxel.antiquity.api.tile.state.TileState;
import io.github.juuxel.antiquity.api.util.Identifier;
import io.github.juuxel.antiquity.impl.rendering.model.TileModels;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.util.*;

public final class ModelLoader {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();
    private static final Map<Identifier, Model> models = new HashMap<>();
    private static final Map<Identifier, TileModels> tileModels = new HashMap<>();
    private static final Model MISSINGNO = getModelUnchecked(new Identifier("antiquity", "missingno"));

    public static Model getModel(Identifier id) {
        return models.computeIfAbsent(id, it -> {
            try {
                return loadModel(id);
            } catch (Exception e) {
                LOGGER.warn("Could not load model {}", id, e);
                return MISSINGNO;
            }
        });
    }

    private static Model getModelUnchecked(Identifier id) {
        return models.computeIfAbsent(id, it -> {
            try {
                return loadModel(id);
            } catch (IOException e) {
                throw new UncheckedIOException("Could not load core model: " + id, e);
            }
        });
    }

    private static TileModels getTileModelMap(Identifier id, Tile tile) {
        return tileModels.computeIfAbsent(id, it -> {
            try {
                return loadTileModels(id, tile);
            } catch (Exception e) {
                // TODO: fallback?
                throw new RuntimeException("Could not load tile state definition " + id, e);
            }
        });
    }

    public static Model getTileModel(Identifier id, TileState state) {
        TileModels models = getTileModelMap(id, state.getTile());
        return getModel(models.variants.get(state).model);
    }

    public static Set<Model> getAllTileModels(Identifier id, Tile tile) {
        TileModels models = getTileModelMap(id, tile);
        Set<Model> result = new HashSet<>();

        for (TileState state : ExtendedTile.of(tile).getStateManager().getStates()) {
            result.add(getModel(models.variants.get(state).model));
        }

        return result;
    }

    private static Model loadModel(Identifier id) throws IOException {
        String path = "/assets/" + id.getNamespace() + "/models/" + id.getPath() + ".json";
        try (InputStream in = Textures.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new NoSuchFileException(path);
            }

            try (Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                return Model.fromJson(json);
            }
        }
    }

    private static TileModels loadTileModels(Identifier id, Tile tile) throws IOException {
        String path = "/assets/" + id.getNamespace() + "/tilestates/" + id.getPath() + ".json";
        try (InputStream in = Textures.class.getResourceAsStream(path)) {
            InputStream usedStream = in;
            if (in == null) {
                String json = "{\"variants\":{\"\":{\"model\":\"" + id + "\"}}}";
                usedStream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
            }

            try (Reader reader = new InputStreamReader(usedStream, StandardCharsets.UTF_8)) {
                JsonObject json = GSON.fromJson(reader, JsonObject.class);
                return TileModels.fromJson(tile, json);
            }
        }
    }
}
