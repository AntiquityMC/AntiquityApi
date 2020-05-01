package io.github.juuxel.antiquity.api.tile.state;

import java.util.Set;

public interface Property<T> {
    String getName();
    Set<T> getValues();

    T deserialize(String value);
    String serialize(T value);
}
