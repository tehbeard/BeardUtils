package me.tehbeard.utils.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Describe the possible names for a command
 * @author James
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandDescriptor {
String label();
String[] alias() default {};
SenderType senderType() default SenderType.ALL;
}
