package me.tehbeard.utils.factory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Represents a factory for various classes
 * Any item placed into the factory must have an annotation
 * This annotation defines the product name, which is used to retrieve instances of said class.
 * @author james
 *
 * @param <C>
 */
public abstract class ConfigurableFactory<C,A> {
    protected Map<String,Class<? extends C>> products;
    private Class<? extends Annotation> annotation;

    /**
     * Constructs a new Configurable factory
     */
    public ConfigurableFactory(Class<? extends Annotation> annotation){
        this.annotation = annotation;
        products = new HashMap<String, Class<? extends C>>();
    }

    /**
     * Parse a product and add it to the factory
     * @param product product to add to the factory
     * @return whether it was added or not
     */
    @SuppressWarnings("unchecked")
    public boolean addProduct(Class<? extends C> product){
        Annotation tag = product.getAnnotation(annotation);
        if(tag!=null){
                String t = getTag(((A) tag));
                if(t!=null){
                    products.put(t,product);
                    return true;
                }
        }
        return false;
    }

    /**
     * Returns an instance of a product
     * @param tag tag of product to make
     * @return an instance of the product, or null if not found
     * @throws IllegalStateException
     */
    public C getProduct(String tag)throws IllegalStateException{
        if(products.containsKey(tag)){
            try {
                return produce(tag);
            } catch (InstantiationException e) {
                throw new IllegalStateException("Could not create instance of the object");
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Could not create instance of the object due to an access exception");

            }	
        }
        return null;
    }
    
    /**
     * Method that produces products, override to change initialisation behaviour
     * @param tag
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    protected C produce(String tag) throws InstantiationException, IllegalAccessException{
        return products.get(tag).newInstance();
    }

    /**
     * Return the tag code for a class
     * @param annotation annotation to parse
     * @return tag to be used for getting this object, or null if not found
     */
    public abstract String getTag(A annotation);

    /**
     * Returns all tags factory can produce
     * @return
     */
    public Set<String> getTags(){
        
        return products.keySet();
    }
}
