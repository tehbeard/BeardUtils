package me.tehbeard.utils.testSuite.commands;

import static org.junit.Assert.*;

import me.tehbeard.utils.commands.ArgumentPack;

import org.junit.Before;
import org.junit.Test;


public class TestArgumentPack {

    ArgumentPack pack;
    /**
     * create steveAB -type creeper -a -c "foo bar"
[create, steveAB]
[a]
{c=foo bar, type=creeper}
foo bar
     */
    @Before
    public void setup(){
        String arg = "create 1.5 -type creeper -a -c \"foo bar\"";
        String[] bool = {"a"};
        String[] opt = {"type","c","d"};
        pack = new ArgumentPack(bool, opt,arg);


    }

    @Test
    public void test_boolFlags(){
        assertTrue("Boolean Flag that exists",pack.getFlag("a"));
        assertFalse("Boolean Flag that doesn't exist",pack.getFlag("c"));
    }
    
    @Test
    public void test_optFlags(){
        assertTrue("Option flag is not null",pack.getOption("c")!=null);
        assertTrue("Option flag returned",pack.getOption("c").equals("foo bar"));
        assertTrue("Second Option flag returned",pack.getOption("type").equals("creeper"));
    }
    
    @Test
    public void test_string(){
        assertTrue("Get first string argument",pack.get(0).equals("create"));
        assertTrue(pack.getNumber(1).doubleValue()==1.5);
    }
}
