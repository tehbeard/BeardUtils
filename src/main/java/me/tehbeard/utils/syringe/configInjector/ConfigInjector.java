package me.tehbeard.utils.syringe.configInjector;

import java.lang.reflect.Field;

import org.bukkit.configuration.ConfigurationSection;

import me.tehbeard.utils.syringe.Injector;

public class ConfigInjector extends Injector<Object,InjectConfig> {

    private ConfigurationSection section;
    public ConfigInjector(ConfigurationSection section) {
        
        super(InjectConfig.class);
        this.section = section;
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void doInject(InjectConfig annotation, Object object, Field field)
            throws IllegalArgumentException, IllegalAccessException {
        Object value = section.get(annotation.value());
        field.set(object,value);
        // TODO Auto-generated method stub
        
    }

}
