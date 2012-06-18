package me.tehbeard.utils.map.tileEntities;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TileEntityType {
String id();
}
