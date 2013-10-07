package com.tehbeard.utils.syringe.configInjector;

import java.lang.reflect.Field;

import me.tehbeard.utils.syringe.Injector;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

/**
 * Reads config values out of a class Values are pulled from fields with @InjectConfig
 * attacked to them
 * 
 * @author James
 * 
 */
public class YamlConfigExtractor extends Injector<Object, InjectConfig> {

    private ConfigurationSection section;

    public YamlConfigExtractor() {

        super(InjectConfig.class);
        cleanConfig();

    }

    private void cleanConfig() {
        this.section = new MemoryConfiguration();
    }

    @Override
    protected void doInject(InjectConfig annotation, Object object, Field field) throws IllegalArgumentException,
            IllegalAccessException {
        this.section.set(annotation.value(), field.get(object));
    }

    /**
     * Returns a configuration object for the supplied object.
     * 
     * @param object
     * @return
     */
    public ConfigurationSection getConfiguration(Object object) {
        cleanConfig();
        inject(object);
        return this.section;

    }

}
