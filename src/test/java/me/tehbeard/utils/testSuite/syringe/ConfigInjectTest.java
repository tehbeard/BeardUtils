package me.tehbeard.utils.testSuite.syringe;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.tehbeard.utils.syringe.configInjector.InjectConfig;
import me.tehbeard.utils.syringe.configInjector.YamlConfigInjector;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.util.Vector;
import org.junit.Before;
import org.junit.Test;

public class ConfigInjectTest {

    @InjectConfig("location")
    private String   test   = "foo";

    @InjectConfig("int")
    private int      value  = 1;

    @InjectConfig("vector")
    private Vector   vector = null;

    @InjectConfig("list")
    private String[] psy    = null;

    @Before
    public void setup() {
        this.test = "foo";
    }

    @SuppressWarnings("deprecation")
    @Test
    public void test() throws FileNotFoundException, IOException, InvalidConfigurationException {
        ConfigurationSection section = new MemoryConfiguration();
        List<String> t = new ArrayList<String>();
        t.add("oppa");
        t.add("gangnam");
        t.add("style");
        section.set("location", "bar");
        section.set("int", 5);
        section.set("vector", new Vector(1, 2, 3));
        // section.set("list",t);
        section.set("list", new String[] { "oppa", "gangnam", "style" });

        YamlConfigInjector ci = new YamlConfigInjector(section);
        ci.inject(this);

        assertEquals("variable test foo->bar", "bar", this.test);
        assertEquals("variable value 1->5", 5, this.value);
        assertEquals("variable vector equal", new Vector(1, 2, 3), this.vector);
        assertEquals("variable psy", this.psy, new String[] { "oppa", "gangnam", "style" });

    }
}
