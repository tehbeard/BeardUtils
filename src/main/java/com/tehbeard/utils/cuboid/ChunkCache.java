package com.tehbeard.utils.cuboid;

import com.tehbeard.utils.Vec3;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


/**
 * handles checking a cuboid cache against a player's location
 * 
 * @author Tehbeard
 * 
 */
public class ChunkCache<T> {

    private HashMap<String, HashSet<CuboidEntry<T>>> cache;

    public ChunkCache() {
        clearCache();
    }

    /**
     * Clears the currently loaded cache
     */
    public void clearCache() {
        this.cache = new HashMap<String, HashSet<CuboidEntry<T>>>();
    }

    /**
     * Create an entry for the cuboid cache
     * 
     * @param cuboid
     *            cuboid used to trigger this.
     * @param entry
     *            Entry to add to this cuboid
     */
    public void addEntry(Cuboid cuboid, T entry) {
        CuboidEntry<T> cuboidEntry = new CuboidEntry<T>(cuboid, entry);
        for (String tag : cuboid.getChunks()) {
            if (!this.cache.containsKey(tag)) {
                this.cache.put(tag, new HashSet<CuboidEntry<T>>());
            }
            this.cache.get(tag).add(cuboidEntry);
        }
        // ADD ITEM TO CACHE
    }


    /**
     * Get entries
     * @param loc Vec3 of location
     * @param dim Dimension string target
     * @return 
     */
    public List<CuboidEntry<T>> getEntries(Vec3 loc,String dim) {
        String world = dim;
        String cx = "" + (int)(Math.floor(loc.x) / 16);
        String cz = "" + (int)(Math.floor(loc.z) / 16);
        List<CuboidEntry<T>> ret = new ArrayList<CuboidEntry<T>>();

        if (this.cache.containsKey("" + world + "," + cx + "," + cz)) {
            for (CuboidEntry<T> entry : this.cache.get("" + world + "," + cx + "," + cz)) {
                if (entry.getCuboid().isInside(loc)) {
                    ret.add(entry);
                }
            }
        }

        return ret;
    }

    public void remove(T entry) {

        for (HashSet<CuboidEntry<T>> e : this.cache.values()) {
            Iterator<CuboidEntry<T>> it = e.iterator();
            while (it.hasNext()) {
                CuboidEntry<T> ee = it.next();
                if (ee.getEntry().equals(entry)) {
                    it.remove();
                }
            }
        }
    }
}
