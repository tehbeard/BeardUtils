package me.tehbeard.utils.testSuite.syringe;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.tehbeard.utils.syringe.configInjector.ConfigInjector;
import me.tehbeard.utils.syringe.configInjector.InjectConfig;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class ConfigInjectTest {

    @InjectConfig("test.location")
    private String test = "foo";
    
    @InjectConfig("test.int")
    private int value = 1;
    
    @InjectConfig("test.vector")
    private Vector vector = null;
    
    @InjectConfig("test.list")
    private List<String> psy = null;
    @Before
    public void setup(){
        test = "foo";
    }
    
    @Test
    public void test() throws FileNotFoundException, IOException, InvalidConfigurationException{
        ConfigurationSection section = new MemoryConfiguration();
        List<String> t = new ArrayList<String>();
        t.add("oppa");
        t.add("gangnam");
        t.add("style");
        section.set("location","bar");
        section.set("int",5);
        section.set("vector",new Vector(1,2,3));
        //section.set("list",t);
        section.set("list",new String[]{"oppa","gangnam","style"});
        
        
        YamlConfiguration c = new YamlConfiguration();
        c.set("test", section);
        String s = c.saveToString();
        
        c.loadFromString(s);
        
        ConfigInjector ci = new ConfigInjector(c);
        ci.inject(this);
        
        assertEquals("variable test foo->bar","bar",test);
        assertEquals("variable value 1->5",5,value);
        assertEquals("variable vector equal",new Vector(1,2,3),vector);
        
    }
}
