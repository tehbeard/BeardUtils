/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tehbeard.utils.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author James
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface PostUpgrade {
    
}
