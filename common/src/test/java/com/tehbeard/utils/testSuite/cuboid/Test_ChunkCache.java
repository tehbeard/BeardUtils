package com.tehbeard.utils.testSuite.cuboid;

import com.tehbeard.utils.Vec3;
import com.tehbeard.utils.cuboid.ChunkCache;
import com.tehbeard.utils.cuboid.Cuboid;
import com.tehbeard.utils.cuboid.CuboidEntry;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class Test_ChunkCache {

    ChunkCache<Boolean> cache;

    @Before
    public void setUp() {
        this.cache = new ChunkCache<Boolean>();
        this.cache.addEntry(new Cuboid("world:0:0:0:9:9:9"), true);
        this.cache.addEntry(new Cuboid("world:5:5:5:14:14:14"), false);
    }

    @Test
    public void test_inTrue() {

        Vec3 l = new Vec3(1, 1, 1);
        List<CuboidEntry<Boolean>> entries = this.cache.getEntries(l,"world");
        assertTrue("TRUE: One Entry returned", entries.size() == 1);
        assertTrue("TRUE: value is true", entries.get(0).getEntry());
    }

    @Test
    public void test_inFalse() {
        Vec3 l = new Vec3(11, 11, 11);
        List<CuboidEntry<Boolean>> entries = this.cache.getEntries(l,"world");
        assertTrue("FALSE: One Entry returned", entries.size() == 1);
        assertTrue("FALSE: value is false", !entries.get(0).getEntry());
    }

    @Test
    public void inBoth() {
        Vec3 l = new Vec3(9, 9, 9);
        List<CuboidEntry<Boolean>> entries = this.cache.getEntries(l,"world");
        assertTrue("Two Entries returned", entries.size() == 2);
        boolean b1 = entries.get(0).getEntry();
        boolean b2 = entries.get(1).getEntry();

        assertTrue("Different values", b1 != b2);

    }
}
