package me.tehbeard.utils.testSuite.syringe;

import static org.junit.Assert.*;
import me.tehbeard.utils.syringe.configInjector.YamlConfigExtractor;
import me.tehbeard.utils.syringe.configInjector.InjectConfig;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class ConfigExtractTest {

    @InjectConfig("test.location")
    private String test = "foo";
    
    @InjectConfig("test.int")
    private int value = 1;
    
    @InjectConfig("test.vector")
    private Vector vector = new Vector(5, 5, 5);
    
    @Before
    public void setup(){
        test = "foo";
    }
    
    @Test
    public void test(){
       
        YamlConfigExtractor cx = new YamlConfigExtractor();
        
         ConfigurationSection c = cx.getConfiguration(this);
         assertEquals("string",c.getString("test.location"),test);
         assertEquals("int",c.getInt("test.int"),value);
         assertEquals("vector",c.get("test.vector"),vector);
         
        
        //cx.inject(this);
        
        

        
    }
}
