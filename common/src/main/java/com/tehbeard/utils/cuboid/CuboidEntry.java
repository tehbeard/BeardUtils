package com.tehbeard.utils.cuboid;

/**
 * Represents an entry in the cuboid cache
 * 
 * @author james
 * 
 * @param <E>
 */
public final class CuboidEntry<E> {
    private final Cuboid cuboid;
    private final E      entry;

    public CuboidEntry(Cuboid cuboid, E entry) {
        this.cuboid = cuboid;
        this.entry = entry;
    }

    public Cuboid getCuboid() {
        return this.cuboid;
    }

    public E getEntry() {
        return this.entry;
    }
}