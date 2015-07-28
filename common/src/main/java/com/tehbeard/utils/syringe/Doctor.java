package com.tehbeard.utils.syringe;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * Runs a collection of injectors on a class
 * 
 * @author James
 * 
 */
public class Doctor<T> {

    Set<Injector<T, ? extends Annotation>> injections;

    public Doctor() {
        this.injections = new HashSet<Injector<T, ? extends Annotation>>();
    }

    /**
     * Add an injector for this type of doctor
     * 
     * @param injector
     */
    public void addInjector(Injector<T, ? extends Annotation> injector) {

        this.injections.add(injector);
    }

    /**
     * Administer injections to an object
     * 
     * @param patient
     */
    public void administer(T patient) {
        for (Injector<T, ? extends Annotation> inject : this.injections) {
            inject.inject(patient);
        }

    }

}
