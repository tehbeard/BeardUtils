package me.tehbeard.utils.testSuite.cuboid;

import static org.junit.Assert.*;

import java.util.List;

import org.bukkit.Location;
import org.junit.Before;
import org.junit.Test;


import me.tehbeard.utils.cuboid.ChunkCache;
import me.tehbeard.utils.cuboid.Cuboid;
import me.tehbeard.utils.cuboid.CuboidEntry;
import me.tehbeard.utils.testSuite.fake.FakeWorld;

public class Test_ChunkCache {

    
    ChunkCache<Boolean> cache;
    
    @Before
    public void setUp(){
        cache = new ChunkCache<Boolean>();
        cache.addEntry(new Cuboid("world:0:0:0:9:9:9"), true);
        cache.addEntry(new Cuboid("world:5:5:5:14:14:14"), false);
    }
    
    @Test
    public void test_inTrue(){
        Location l = new Location(new FakeWorld("world"), 1,1, 1);
        List<CuboidEntry<Boolean>> entries = cache.getEntries(l);
        assertTrue("TRUE: One Entry returned",entries.size() == 1);
        assertTrue("TRUE: value is true",entries.get(0).getEntry() == true);
    }
    
    @Test
    public void test_inFalse(){
        Location l = new Location(new FakeWorld("world"), 11,11, 11);
        List<CuboidEntry<Boolean>> entries = cache.getEntries(l);
        assertTrue("FALSE: One Entry returned",entries.size() == 1);
        assertTrue("FALSE: value is false",entries.get(0).getEntry() == false);
    }
    
    @Test
    public void inBoth(){
        Location l = new Location(new FakeWorld("world"), 9,9, 9);
        List<CuboidEntry<Boolean>> entries = cache.getEntries(l);
        assertTrue("Two Entries returned",entries.size() == 2);
        boolean b1 = entries.get(0).getEntry();
        boolean b2 = entries.get(1).getEntry();
        
        assertTrue("Different values", b1!=b2);
        
    }
}
