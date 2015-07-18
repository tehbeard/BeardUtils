/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tehbeard.utils.sponge;

import com.tehbeard.utils.syringe.InjectConfig;
import com.tehbeard.utils.syringe.Injector;
import java.lang.reflect.Field;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

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
        field.set(object, node.getNode((Object[])annotation.value().split("\\.")).getValue());
    }
    
}
