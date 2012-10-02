package me.tehbeard.utils.syringe.configInjector;

import java.lang.reflect.Field;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;

import me.tehbeard.utils.syringe.Injector;

public class ConfigExtractor extends Injector<Object,InjectConfig> {

    private ConfigurationSection section;
    public ConfigExtractor() {
        
        super(InjectConfig.class);
        cleanConfig();

    }
    private void cleanConfig(){
        this.section = new MemoryConfiguration();
    }

    @Override
    protected void doInject(InjectConfig annotation, Object object, Field field)
            throws IllegalArgumentException, IllegalAccessException {
        
        section.set(annotation.value(),field.get(object));
        
        // TODO Auto-generated method stub
        
    }
    
    public ConfigurationSection getConfiguration(Object object){
        cleanConfig();
        inject(object);
        return section;
        
    }

}
