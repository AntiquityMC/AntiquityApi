package io.github.juuxel.antiquity.api.tile.state;

import com.google.common.collect.ImmutableSet;
import io.github.juuxel.antiquity.api.tile.Direction;

import java.util.Optional;
import java.util.Set;

public final class DirectionProperty implements Property<Direction> {
    private final String name;
    private final Set<Direction> values;

    private DirectionProperty(String name, Set<Direction> values) {
        this.name = name;
        this.values = values;
    }

    public static DirectionProperty of(String name) {
        return new DirectionProperty(name, ImmutableSet.copyOf(Direction.values()));
    }

    public static DirectionProperty horizontal(String name) {
        return new DirectionProperty(name, ImmutableSet.of(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST));
    }

    public static DirectionProperty of(String name, Set<Direction> values) {
        return new DirectionProperty(name, ImmutableSet.copyOf(values));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Direction> getValues() {
        return values;
    }

    @Override
    public Optional<Direction> deserialize(String value) {
        return Optional.ofNullable(Direction.of(value));
    }

    @Override
    public String serialize(Direction value) {
        return value.toString();
    }
}
