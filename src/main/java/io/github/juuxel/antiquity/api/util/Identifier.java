package io.github.juuxel.antiquity.api.util;

import java.util.Objects;

public final class Identifier {
    public static final String DEFAULT_NAMESPACE = "minecraft";
    private static final String NAMESPACE_REGEX = "^[a-z0-9-_]+$";
    private static final String PATH_REGEX = "^[a-z0-9-_./]+$";

    private final String namespace;
    private final String path;

    public Identifier(String namespace, String path) {
        this.namespace = Objects.requireNonNull(namespace, "namespace");
        this.path = Objects.requireNonNull(path, "path");
    }

    public Identifier(String combined) {
        Objects.requireNonNull(combined, "combined identifier");
        String[] split = combined.split(":");

        switch (split.length) {
            case 1:
                this.namespace = DEFAULT_NAMESPACE;
                this.path = validatePath(combined);
                break;

            case 2:
                String namespace = split[0];
                String path = split[1];

                this.namespace = validateNamespace(namespace);
                this.path = validatePath(path);
                break;

            default:
                throw new IllegalArgumentException("Combined ID string '" + combined + "' should have exactly two components! Found: " + split.length);
        }
    }

    private static String validateNamespace(String namespace) {
        if (namespace.matches(NAMESPACE_REGEX)) {
            return namespace;
        } else {
            throw new IllegalArgumentException("Invalid ID namespace: " + namespace + "!");
        }
    }

    private static String validatePath(String path) {
        if (path.matches(PATH_REGEX)) {
            return path;
        } else {
            throw new IllegalArgumentException("Invalid ID path: " + path + "!");
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return namespace.equals(that.namespace) &&
                path.equals(that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    @Override
    public String toString() {
        return namespace + ':' + path;
    }
}
