/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tehbeard.utils.sponge;

import com.google.common.reflect.TypeToken;
import com.tehbeard.utils.syringe.InjectConfig;
import com.tehbeard.utils.syringe.Injector;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

/**
 *
 * @author James
 */
public class HOCONInjector extends Injector<Object, InjectConfig> {
    
    private final CommentedConfigurationNode node;

    public HOCONInjector(CommentedConfigurationNode node) {
        super(InjectConfig.class);
        this.node = node;
    }

    @Override
    protected void doInject(InjectConfig annotation, Object object, Field field) throws IllegalArgumentException, IllegalAccessException {
        try {
            field.set(object, node.getNode(annotation.value()).getValue(TypeToken.of(field.getClass())));
        } catch (ObjectMappingException ex) {
            Logger.getLogger(HOCONInjector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
