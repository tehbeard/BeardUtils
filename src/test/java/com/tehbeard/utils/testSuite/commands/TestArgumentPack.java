package com.tehbeard.utils.testSuite.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.tehbeard.utils.commands.ArgumentPack;

import org.junit.Before;
import org.junit.Test;

public class TestArgumentPack {

    ArgumentPack<Void> pack;

    /**
     * create steveAB -type creeper -a -c "foo bar" [create, steveAB] [a] {c=foo
     * bar, type=creeper} foo bar
     */
    @Before
    public void setup() {
        String arg = "create 1.5 -type creeper vechs -a -c \"foo bar\"";
        String[] bool = { "a" };
        String[] opt = { "type", "c", "d" };
        this.pack = new ArgumentPack<Void>(bool, opt, arg);

        for (int i = 0; i < this.pack.size(); i++) {
            System.out.println("" + i + " : " + this.pack.get(i));
        }

    }

    @Test
    public void test_boolFlags() {
        assertTrue("Boolean Flag that exists", this.pack.getFlag("a"));
        assertFalse("Boolean Flag that doesn't exist", this.pack.getFlag("c"));
    }

    @Test
    public void test_optFlags() {
        assertTrue("Option flag is not null", this.pack.getOption("c") != null);
        assertTrue("Option flag returned", this.pack.getOption("c").equals("foo bar"));
        assertTrue("Second Option flag returned", this.pack.getOption("type").equals("creeper"));
    }

    @Test
    public void test_string() {
        assertTrue("Get first string argument", this.pack.get(0).equals("create"));
        assertTrue("Get thrid string argument", this.pack.get(2).equals("vechs"));
        assertTrue(this.pack.getNumber(1).doubleValue() == 1.5);
    }
}
