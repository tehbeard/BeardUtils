package com.tehbeard.utils.factory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Provides a string -> class mapping
 * @author James
 *
 * @param <T>
 */
public abstract class ClassCatalogue<T> {
    
    private Map<String,Class<? extends T>> catalogue = new HashMap<String, Class<? extends T>>();
    
    public void addProduct(Class<? extends T> _class){
        catalogue.put(getTag(_class), _class);
    }
    
    public Class<? extends T> get(String tag){
        return catalogue.get(tag);
    }
    
    public Collection<String> getTags(){
        return catalogue.keySet();
    }
    
    public Collection<Class<? extends T>> getClasses(){
    	return catalogue.values();
    }

    protected abstract String getTag(Class<? extends T> _class);
}
