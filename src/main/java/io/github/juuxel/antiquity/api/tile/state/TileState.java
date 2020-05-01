package io.github.juuxel.antiquity.api.tile.state;

import com.google.common.collect.ImmutableMap;
import com.mojang.minecraft.level.tile.Tile;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public final class TileState {
    public static final TileState AIR = new TileState(null, new TileStateManager.Builder(null).build(), ImmutableMap.of());

    private final @Nullable Tile tile;
    private final TileStateManager manager;
    private final Map<Property<?>, Object> properties;

    TileState(@Nullable Tile tile, TileStateManager manager, Map<Property<?>, Object> properties) {
        this.tile = tile;
        this.manager = manager;
        this.properties = properties;
    }

    public boolean hasProperty(Property<?> property) {
        return manager.hasProperty(property);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Property<T> property) {
        if (!hasProperty(property)) {
            throw new IllegalArgumentException("State " + this + " does not contain property '" + property.getName() + "'!");
        }

        return (T) properties.get(property);
    }

    public <T> TileState with(Property<T> property, T value) {
        if (!property.getValues().contains(value)) {
            throw new IllegalArgumentException("Property '" + property.getName() + "' does not have value '" + value + "'!");
        }

        for (TileState state : manager.getStates()) {
            boolean hasCorrectValues = true;

            for (Map.Entry<Property<?>, Object> entry : state.properties.entrySet()) {
                Object expectedValue = entry.getKey() == property ? value : get(property);
                hasCorrectValues &= entry.getValue().equals(expectedValue);
            }

            if (hasCorrectValues) {
                return state;
            }
        }

        throw new IllegalStateException("Could not find state!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TileState tileState = (TileState) o;
        return Objects.equals(tile, tileState.tile) &&
                properties.equals(tileState.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tile, properties);
    }

    @Override
    public String toString() {
        return toString(true);
    }

    @SuppressWarnings("unchecked")
    public String toString(boolean includeId) {
        StringBuilder builder = new StringBuilder();

        if (includeId) {
            builder.append(tile != null ? tile.id : 0);
        }

        builder.append('[');

        boolean first = true;
        for (Map.Entry<Property<?>, Object> entry : properties.entrySet()) {
            if (!first) {
                builder.append(',');
            } else {
                first = false;
            }

            Property<Object> property = (Property<Object>) entry.getKey();
            builder.append(property.getName());
            builder.append('=');
            builder.append(property.serialize(entry.getValue()));
        }

        builder.append(']');

        return builder.toString();
    }
}
