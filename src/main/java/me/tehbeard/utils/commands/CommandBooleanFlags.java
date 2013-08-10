package me.tehbeard.utils.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Define the boolean flags a command supports
 * 
 * @author James
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandBooleanFlags {
    String[] value();
}
