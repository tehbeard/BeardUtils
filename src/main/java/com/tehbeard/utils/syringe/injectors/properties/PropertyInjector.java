package com.tehbeard.utils.syringe.injectors.properties;

import com.tehbeard.utils.syringe.InjectConfig;
import java.lang.reflect.Field;

import com.tehbeard.utils.syringe.Injector;
import java.util.Properties;


/**
 * Injects data into fields of an object from a ConfigurationSection, using @InjectConfig
 * annotations
 * 
 * @author James
 * 
 */
public class PropertyInjector extends Injector<Object, InjectConfig> {

    private final Properties section;

    public PropertyInjector(Properties section) {

        super(InjectConfig.class);
        this.section = section;
    }

    @Override
    protected void doInject(InjectConfig annotation, Object object, Field field) throws IllegalArgumentException,
            IllegalAccessException {
        Object value = this.section.get(annotation.value());
        if(value != null){
        field.set(object, value);
        }

    }

}
