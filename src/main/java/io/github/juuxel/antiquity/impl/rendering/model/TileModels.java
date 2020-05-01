package io.github.juuxel.antiquity.impl.rendering.model;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.minecraft.level.tile.Tile;
import io.github.juuxel.antiquity.api.tile.ExtendedTile;
import io.github.juuxel.antiquity.api.tile.state.Property;
import io.github.juuxel.antiquity.api.tile.state.TileState;
import io.github.juuxel.antiquity.api.tile.state.TileStateManager;
import io.github.juuxel.antiquity.api.util.Identifier;

import java.util.*;

public final class TileModels {
    public final Map<TileState, Variant> variants;

    public TileModels(Map<? extends TileState, ? extends Variant> variants) {
        this.variants = ImmutableMap.copyOf(variants);
    }

    public static TileModels fromJson(Tile tile, JsonObject json) {
        TileStateManager stateManager = ExtendedTile.of(tile).getStateManager();
        Set<TileState> states = new HashSet<>(stateManager.getStates());
        Set<TileState> usedStates = new HashSet<>();
        Map<TileState, Variant> variants = new HashMap<>();
        Map<String, String> properties = new HashMap<>();

        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("variants").entrySet()) {
            String stateStr = entry.getKey();
            if (!stateStr.isEmpty()) {
                properties.clear();
                for (String propertyPair : stateStr.split(" *, *")) {
                    String[] components = propertyPair.split(" *= *");
                    if (components.length != 2) {
                        throw new IllegalArgumentException("Property '" + propertyPair + "' in '" + stateStr + "' has too many components! Expected: 2, found: " + components.length);
                    }

                    properties.put(components[0], components[1]);
                }
            }

            Variant variant = Variant.fromJson(entry.getValue().getAsJsonObject());

            for (TileState state : states) {
                boolean matches = true;

                if (!stateStr.isEmpty()) {
                    for (Map.Entry<String, String> propertyEntry : properties.entrySet()) {
                        String propertyName = propertyEntry.getKey();
                        String valueStr = propertyEntry.getValue();

                        if (!state.hasProperty(propertyName)) {
                            throw new IllegalArgumentException("Tile " + state.getTileId() + " does not have property '" + propertyName + "'!");
                        }

                        Property<?> property = stateManager.getProperty(propertyName);
                        Optional<?> value = property.deserialize(valueStr);
                        matches &= value.isPresent() && value.get().equals(state.get(property));
                    }
                }

                if (matches) {
                    usedStates.add(state);
                    variants.put(state, variant);
                }
            }

            states.removeAll(usedStates);
            usedStates.clear();
        }

        return new TileModels(variants);
    }

    public static final class Variant {
        public final Identifier model;
        public final int x; // TODO: Implement
        public final int y;
        public final boolean uvlock;

        public Variant(Identifier model, int x, int y, boolean uvlock) {
            this.model = model;
            this.x = x;
            this.y = y;
            this.uvlock = uvlock;
        }

        @SuppressWarnings("SimplifiableConditionalExpression")
        public static Variant fromJson(JsonObject json) {
            Identifier model = new Identifier(json.getAsJsonPrimitive("model").getAsString());
            int x = json.has("x") ? validateRotation("x", json.getAsJsonPrimitive("x").getAsInt()) : 0;
            int y = json.has("y") ? validateRotation("y", json.getAsJsonPrimitive("y").getAsInt()) : 0;
            boolean uvlock = json.has("uvlock") ? json.getAsJsonPrimitive("uvlock").getAsBoolean() : false;

            return new Variant(model, x, y, uvlock);
        }

        private static int validateRotation(String name, int rotation) {
            if (rotation != 0 && rotation != 90 && rotation != 180 && rotation != 270) {
                throw new IllegalArgumentException("Rotation '" + name + "' should be 0, 90, 180 or 270. Found: " + rotation);
            }

            return rotation;
        }
    }
}
