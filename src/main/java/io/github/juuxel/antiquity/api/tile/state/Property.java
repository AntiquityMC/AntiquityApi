package io.github.juuxel.antiquity.api.tile.state;

import java.util.Optional;
import java.util.Set;

public interface Property<T extends Comparable<T>> {
    String getName();
    Set<T> getValues();

    Optional<T> deserialize(String value);
    String serialize(T value);
}
