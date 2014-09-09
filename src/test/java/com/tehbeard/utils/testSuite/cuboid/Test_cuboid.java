package com.tehbeard.utils.testSuite.cuboid;

import com.tehbeard.utils.Vec3;
import static org.junit.Assert.*;
import com.tehbeard.utils.cuboid.Cuboid;

import org.junit.Before;
import org.junit.Test;

public class Test_cuboid {

    Cuboid c;

    @Before
    public void setUp() {
        this.c = new Cuboid("world:0:0:0:9:1:9");
    }

    @Test
    public void size() {

        assertEquals("Correct size returned", (10 * 2 * 10), this.c.size());
    }

    @Test
    public void inside() {
        Vec3 l = new Vec3(1, 1, 1);

        assertTrue("inside cuboid", this.c.isInside(l));
    }

    @Test
    public void outside() {
        Vec3 l = new Vec3(1, 1, 11);

        assertFalse("outside cuboid", this.c.isInside(l));
    }
}
