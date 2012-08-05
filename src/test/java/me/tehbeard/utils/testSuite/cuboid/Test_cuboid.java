package me.tehbeard.utils.testSuite.cuboid;

import static org.junit.Assert.*;

import org.bukkit.Location;
import org.junit.Before;
import org.junit.Test;

import me.tehbeard.utils.cuboid.Cuboid;

public class Test_cuboid {

    Cuboid c;
    @Before
    public void setUp(){
        c = new Cuboid("world:0:0:0:9:1:9");
    }
    
    @Test
    public void size(){
        
        
        assertTrue("Correct size returned",c.size() == 10 * 2 * 10);
    }
    
    @Test
    public void inside(){
        Location l = new Location(new FakeWorld("world"), 1,1, 1);
        
        assertTrue("inside cuboid",c.isInside(l));
    }
    
    @Test
    public void outside(){
        Location l = new Location(new FakeWorld("world"), 1,1, 11);
        
        assertFalse("outside cuboid",c.isInside(l));
    }
}
