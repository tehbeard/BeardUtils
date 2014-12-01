package com.tehbeard.utils.syringe.injectors.properties;

import com.tehbeard.utils.syringe.InjectConfig;
import com.tehbeard.utils.syringe.Injector;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Reads config values out of a class Values are pulled from fields with @InjectConfig
 * attacked to them
 * 
 * @author James
 * 
 */
public class PropertyExtractor extends Injector<Object, InjectConfig> {

    private Properties section;

    public PropertyExtractor() {

        super(InjectConfig.class);
        cleanConfig();

    }

    private void cleanConfig() {
        this.section = new Properties();
    }

    @Override
    protected void doInject(InjectConfig annotation, Object object, Field field) throws IllegalArgumentException,
            IllegalAccessException {
        this.section.put(annotation.value(), field.get(object));
    }

    /**
     * Returns a configuration object for the supplied object.
     * 
     * @param object
     * @return
     */
    public Properties getConfiguration(Object object) {
        cleanConfig();
        inject(object);
        return this.section;

    }

}
