package io.github.juuxel.antiquity.api.registry;

import io.github.juuxel.antiquity.api.util.Identifier;

@FunctionalInterface
public interface RegistrationListener<T> {
    void onEntryRegistered(Identifier id, T entry);
}
