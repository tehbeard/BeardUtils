package com.tehbeard.utils.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Statement;

/**
 * Embedded raw SQL, be very careful about using only standard SQL
 * @author James
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SQLRaw {
    String sql();
    String type();
    int flags() default Statement.NO_GENERATED_KEYS;
}
