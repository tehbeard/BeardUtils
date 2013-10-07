package com.tehbeard.utils.testSuite.syringe;

import static org.junit.Assert.assertEquals;
import com.tehbeard.utils.syringe.configInjector.InjectConfig;
import com.tehbeard.utils.syringe.configInjector.YamlConfigExtractor;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class ConfigExtractTest {

    @InjectConfig("test.location")
    private String test   = "foo";

    @InjectConfig("test.int")
    private int    value  = 1;

    @InjectConfig("test.vector")
    private Vector vector = new Vector(5, 5, 5);

    @Before
    public void setup() {
        this.test = "foo";
    }

    @Test
    public void test() {

        YamlConfigExtractor cx = new YamlConfigExtractor();

        ConfigurationSection c = cx.getConfiguration(this);
        assertEquals("string", c.getString("test.location"), this.test);
        assertEquals("int", c.getInt("test.int"), this.value);
        assertEquals("vector", c.get("test.vector"), this.vector);

        // cx.inject(this);

    }
}
