package io.github.juuxel.antiquity.api.registry;

import io.github.juuxel.antiquity.api.util.Identifier;

/**
 * A registration listener for a registry.
 *
 * @param <T> the entry type
 * @see Registry#addRegistrationListener(RegistrationListener)
 */
@FunctionalInterface
public interface RegistrationListener<T> {
    /**
     * Called when an entry is registered.
     *
     * @param id    the entry's ID
     * @param entry the entry
     */
    void onEntryRegistered(Identifier id, T entry);
}
