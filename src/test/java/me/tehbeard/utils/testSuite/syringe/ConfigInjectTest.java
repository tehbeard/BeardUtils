package me.tehbeard.utils.testSuite.syringe;

import static org.junit.Assert.*;
import me.tehbeard.utils.syringe.configInjector.ConfigInjector;
import me.tehbeard.utils.syringe.configInjector.InjectConfig;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.Before;
import org.junit.Test;

public class ConfigInjectTest {

    @InjectConfig("test.location")
    private String test = "foo";
    
    @Before
    public void setup(){
        test = "foo";
    }
    
    @Test
    public void test(){
        ConfigurationSection section = new MemoryConfiguration();
        section.set("test.location","bar");
        ConfigInjector ci = new ConfigInjector(section);
        ci.inject(this);
        
        assertEquals("variable test foo->bar","bar",test);
    }
}
