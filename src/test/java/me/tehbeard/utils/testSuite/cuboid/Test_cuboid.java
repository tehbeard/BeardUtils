package me.tehbeard.utils.testSuite.cuboid;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import me.tehbeard.utils.cuboid.Cuboid;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class Test_cuboid {

    Cuboid c;
    World  w;

    @Before
    public void setUp() {
        this.c = new Cuboid("world:0:0:0:9:1:9");
        this.w = mock(World.class);
        when(this.w.getName()).thenReturn("world");
    }

    @Test
    public void size() {

        assertTrue("Correct size returned", this.c.size() == (10 * 2 * 10));
    }

    @Test
    public void inside() {
        Location l = new Location(this.w, 1, 1, 1);

        assertTrue("inside cuboid", this.c.isInside(l));
    }

    @Test
    public void outside() {
        Location l = new Location(this.w, 1, 1, 11);

        assertFalse("outside cuboid", this.c.isInside(l));
    }
}
