package io.github.juuxel.antiquity.api.tile.state;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mojang.minecraft.level.tile.Tile;
import io.github.juuxel.antiquity.api.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class TileStateManager {
    private final Map<String, Property<?>> properties;
    private final Set<TileState> states;
    private final TileState defaultState;

    private TileStateManager(Builder builder) {
        this.properties = ImmutableMap.copyOf(builder.properties);

        List<Set<Pair<Property<?>, Object>>> propertyValues = new ArrayList<>();
        for (Property<?> property : properties.values()) {
            Set<Pair<Property<?>, Object>> valuePairs = new HashSet<>();
            for (Object value : property.getValues()) {
                valuePairs.add(new Pair<>(property, value));
            }
            propertyValues.add(valuePairs);
        }

        Set<List<Pair<Property<?>, Object>>> product = Sets.cartesianProduct(propertyValues);
        ImmutableSet.Builder<TileState> stateBuilder = ImmutableSet.builder();

        TileState defaultState = null;

        for (List<Pair<Property<?>, Object>> propertyList : product) {
            ImmutableMap.Builder<Property<?>, Object> propertiesBuilder = ImmutableMap.builder();

            boolean isDefaultState = defaultState == null;

            for (Pair<Property<?>, Object> entry : propertyList) {
                propertiesBuilder.put(entry.getFirst(), entry.getSecond());
                isDefaultState &= builder.defaultValues.get(entry.getFirst()).equals(entry.getSecond());
            }

            TileState state = new TileState(builder.tile, this, propertiesBuilder.build());
            stateBuilder.add(state);

            if (isDefaultState) {
                defaultState = state;
            }
        }

        if (defaultState == null) {
            throw new IllegalStateException("Could not construct default state!");
        }

        states = stateBuilder.build();
        this.defaultState = defaultState;
    }

    public TileState getDefaultState() {
        return defaultState;
    }

    public Set<TileState> getStates() {
        return states;
    }

    public boolean hasProperty(Property<?> property) {
        return properties.containsValue(property);
    }

    public static final class Builder {
        private static final String PROPERTY_NAME_REGEX = "^[a-z0-9_]+$";
        private final @Nullable Tile tile;
        private final Map<String, Property<?>> properties = new HashMap<>();
        private final Map<Property<?>, Object> defaultValues = new HashMap<>();

        public Builder(@Nullable Tile tile) {
            this.tile = tile;
        }

        public Builder addProperties(Property<?>... properties) {
            Objects.requireNonNull(properties, "properties");

            for (Property<?> property : properties) {
                if (this.properties.containsKey(property.getName())) {
                    throw new IllegalArgumentException("Property with name '" + property.getName() + "' already exists!");
                }

                if (property.getValues().isEmpty()) {
                    throw new IllegalArgumentException("Property '" + property.getName() + "' has no values!");
                }

                this.properties.put(property.getName(), property);
                Iterator<?> values = property.getValues().iterator();
                defaultValues.put(property, values.next());
            }

            return this;
        }

        public <T> Builder addProperty(Property<T> property, T defaultValue) {
            Objects.requireNonNull(property, "property");
            Objects.requireNonNull(defaultValue, "defaultValue");

            if (properties.containsKey(property.getName())) {
                throw new IllegalArgumentException("Property with name '" + property.getName() + "' already exists!");
            } else if (property.getValues().isEmpty()) {
                throw new IllegalArgumentException("Property '" + property.getName() + "' has no values!");
            } else if (!property.getValues().contains(defaultValue)) {
                throw new IllegalArgumentException("Default value '" + defaultValue + "' is not a valid value for '" + property.getName() + "'!");
            } else if (!property.getName().matches(PROPERTY_NAME_REGEX)) {
                throw new IllegalArgumentException("Property name '" + property.getName() + "' must only contain [a-z0-9_]!");
            }

            // TODO: Check values with regex

            properties.put(property.getName(), property);
            defaultValues.put(property, defaultValue);

            return this;
        }

        public TileStateManager build() {
            return new TileStateManager(this);
        }
    }
}
