package me.tehbeard.utils.testSuite.cuboid;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import me.tehbeard.utils.cuboid.ChunkCache;
import me.tehbeard.utils.cuboid.Cuboid;
import me.tehbeard.utils.cuboid.CuboidEntry;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class Test_ChunkCache {

    ChunkCache<Boolean> cache;
    World               world;

    @Before
    public void setUp() {
        this.cache = new ChunkCache<Boolean>();
        this.cache.addEntry(new Cuboid("world:0:0:0:9:9:9"), true);
        this.cache.addEntry(new Cuboid("world:5:5:5:14:14:14"), false);

        this.world = mock(World.class);
        when(this.world.getName()).thenReturn("world");
    }

    @Test
    public void test_inTrue() {

        Location l = new Location(this.world, 1, 1, 1);
        List<CuboidEntry<Boolean>> entries = this.cache.getEntries(l);
        assertTrue("TRUE: One Entry returned", entries.size() == 1);
        assertTrue("TRUE: value is true", entries.get(0).getEntry());
    }

    @Test
    public void test_inFalse() {
        Location l = new Location(this.world, 11, 11, 11);
        List<CuboidEntry<Boolean>> entries = this.cache.getEntries(l);
        assertTrue("FALSE: One Entry returned", entries.size() == 1);
        assertTrue("FALSE: value is false", !entries.get(0).getEntry());
    }

    @Test
    public void inBoth() {
        Location l = new Location(this.world, 9, 9, 9);
        List<CuboidEntry<Boolean>> entries = this.cache.getEntries(l);
        assertTrue("Two Entries returned", entries.size() == 2);
        boolean b1 = entries.get(0).getEntry();
        boolean b2 = entries.get(1).getEntry();

        assertTrue("Different values", b1 != b2);

    }
}
