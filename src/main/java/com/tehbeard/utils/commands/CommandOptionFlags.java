package com.tehbeard.utils.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Lists accepted flags that take a parameter
 * 
 * @author James
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandOptionFlags {
    String[] value();
}
