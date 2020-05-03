package io.github.juuxel.antiquity.api.tile.state;

import com.google.common.collect.ImmutableSet;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class BooleanProperty implements Property<Boolean> {
    private static final ImmutableSet<Boolean> VALUES = ImmutableSet.of(true, false);
    private final String name;

    public BooleanProperty(String name) {
        this.name = Objects.requireNonNull(name, "name");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Boolean> getValues() {
        return VALUES;
    }

    @Override
    public Optional<Boolean> deserialize(String value) {
        return Optional.of(Boolean.valueOf(value));
    }

    @Override
    public String serialize(Boolean value) {
        return value.toString();
    }
}
