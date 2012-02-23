package me.tehbeard.utils.commands;

public @interface CommandDescriptor {
String label();
String[] alias() default {};
String description();
}
