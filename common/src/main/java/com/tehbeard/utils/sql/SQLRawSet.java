package com.tehbeard.utils.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Embedded raw SQL, be very careful about using only standard SQL
 * @author James
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SQLRawSet {
    SQLRaw[] value();
}
