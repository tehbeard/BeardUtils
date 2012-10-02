package me.tehbeard.utils.testSuite.syringe;

import static org.junit.Assert.*;
import me.tehbeard.utils.syringe.configInjector.ConfigExtractor;
import me.tehbeard.utils.syringe.configInjector.ConfigInjector;
import me.tehbeard.utils.syringe.configInjector.InjectConfig;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class ConfigExtractTest {

    @InjectConfig("test.location")
    private String test = "foo";
    
    @InjectConfig("test.int")
    private int value = 1;
    
    @InjectConfig("test.vector")
    private Vector vector = null;
    @Before
    public void setup(){
        test = "foo";
    }
    
    @Test
    public void test(){
        ConfigurationSection section = new MemoryConfiguration();
        
        ConfigExtractor cx = new ConfigExtractor();
        
        YamlConfiguration c = new YamlConfiguration();
        c.set("root",cx.getConfiguration(this));
        System.out.println(c.saveToString());
        
        //cx.inject(this);
        
        

        
    }
}
