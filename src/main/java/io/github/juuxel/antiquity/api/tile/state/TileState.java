package io.github.juuxel.antiquity.api.tile.state;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.mojang.minecraft.level.tile.Tile;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;

public final class TileState implements Comparable<TileState> {
    public static final TileState AIR = new TileState(null, new TileStateManager.Builder(null).build(), ImmutableMap.of());

    private final @Nullable Tile tile;
    private final TileStateManager manager;
    private final SortedMap<Property<?>, Object> properties;

    TileState(@Nullable Tile tile, TileStateManager manager, Map<Property<?>, Object> properties) {
        this.tile = tile;
        this.manager = manager;
        this.properties = ImmutableSortedMap.copyOf(properties, Comparator.comparing(Property::getName));
    }

    public TileStateManager getManager() {
        return manager;
    }

    public boolean hasProperty(Property<?> property) {
        return manager.hasProperty(property);
    }

    public boolean hasProperty(String name) {
        return manager.hasProperty(name);
    }

    public @Nullable Tile getTile() {
        return tile;
    }

    public int getTileId() {
        return tile != null ? tile.id : 0;
    }

    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> T get(Property<T> property) {
        if (!hasProperty(property)) {
            throw new IllegalArgumentException("State " + this + " does not contain property '" + property.getName() + "'!");
        }

        return (T) properties.get(property);
    }

    public <T extends Comparable<T>> TileState with(Property<T> property, T value) {
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

    @SuppressWarnings({"unchecked", "rawtypes"})
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

            Property property = entry.getKey();
            builder.append(property.getName());
            builder.append('=');
            builder.append(property.serialize((Comparable) entry.getValue()));
        }

        builder.append(']');

        return builder.toString();
    }

    @SuppressWarnings({"rawtypes", "unchecked", "RedundantCast"})
    @Override
    public int compareTo(TileState o) {
        if (tile != o.tile) {
            return Integer.compare(getTileId(), o.getTileId());
        }

        for (Property<?> property : properties.keySet()) {
            int comparison = ((Comparable) get(property)).compareTo(o.get(property));

            if (comparison != 0) {
                return comparison;
            }
        }

        return 0;
    }
}
