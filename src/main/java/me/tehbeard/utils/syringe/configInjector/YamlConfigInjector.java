package me.tehbeard.utils.syringe.configInjector;

import java.lang.reflect.Field;

import org.bukkit.configuration.ConfigurationSection;

import me.tehbeard.utils.syringe.Injector;

/**
 * Injects data into fields of an object from a ConfigurationSection,
 * using @InjectConfig annotations
 * @author James
 *
 */
public class YamlConfigInjector extends Injector<Object,InjectConfig> {

    private ConfigurationSection section;
    public YamlConfigInjector(ConfigurationSection section) {
        
        super(InjectConfig.class);
        this.section = section;
    }

    @Override
    protected void doInject(InjectConfig annotation, Object object, Field field)
            throws IllegalArgumentException, IllegalAccessException {
        Object value = section.get(annotation.value());
        field.set(object,value);
        
    }

}
