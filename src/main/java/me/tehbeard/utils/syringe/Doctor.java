package me.tehbeard.utils.syringe;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Administers injectors to an object
 * @author James
 *
 */
public class Doctor<T> {


    Set<Injector<T,? extends Annotation>> injections;


    public Doctor(){
        injections = new HashSet<Injector<T, ? extends Annotation>>();
    }


    /**
     * Add an injector for this type of doctor
     * @param injector
     */
    public void addInjector(Injector<T, ? extends Annotation> injector){

        injections.add(injector);
    }

    /**
     * Administer injections to an object
     * @param patient
     */
    public void administer(T patient){
        for(Injector<T,? extends Annotation> inject : injections){
            inject.inject(patient);
        }

    }

}
