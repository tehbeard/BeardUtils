package me.tehbeard.utils.testSuite.cuboid;

import static org.junit.Assert.*;

import org.bukkit.Location;
import org.bukkit.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import me.tehbeard.utils.cuboid.Cuboid;


import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.*;
@RunWith(PowerMockRunner.class)
public class Test_cuboid {

    Cuboid c;
    World w;
    @Before
    public void setUp(){
        c = new Cuboid("world:0:0:0:9:1:9");
        w = mock(World.class);
        when(w.getName()).thenReturn("world");
    }
    
    @Test
    public void size(){
        
        
        assertTrue("Correct size returned",c.size() == 10 * 2 * 10);
    }
    
    @Test
    public void inside(){
        Location l = new Location(w, 1,1, 1);
        
        assertTrue("inside cuboid",c.isInside(l));
    }
    
    @Test
    public void outside(){
        Location l = new Location(w, 1,1, 11);
        
        assertFalse("outside cuboid",c.isInside(l));
    }
}
