package me.tehbeard.utils.addons;
/**
 * Provides a way to initialise a class
 * @author james
 *
 * @param <K> type of class to make
 */
public abstract class ClassBootStrapper<K>{
    /**
     * Makes a class of classType
     * @param classType
     */
    public abstract void makeClass(Class<? extends K> classType);
}