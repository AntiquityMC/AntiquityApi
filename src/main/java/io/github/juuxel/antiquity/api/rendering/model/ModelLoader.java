package io.github.juuxel.antiquity.api.rendering.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.minecraft.renderer.Textures;
import io.github.juuxel.antiquity.api.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ModelLoader {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new Gson();
    private static final Model EMPTY = new Model(null, Collections.emptyMap(), Collections.emptyList());
    private static final Map<Identifier, Model> models = new HashMap<>();

    public static Model getModel(Identifier id) {
        return models.computeIfAbsent(id, it -> {
            try {
                return loadModel(id);
            } catch (Exception e) {
                LOGGER.warn("Could not load model {}", id, e);
                return EMPTY;
            }
        });
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
}
