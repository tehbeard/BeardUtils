package com.tehbeard.utils.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * defines the permission node needed for this command
 * 
 * @author James
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandPermission {
    String value();
}
