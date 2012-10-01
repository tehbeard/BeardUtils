package me.tehbeard.utils.syringe.configInjector;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InjectConfig {
String value();
}
