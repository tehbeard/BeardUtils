package com.tehbeard.utils.sql;

import java.io.InputStream;
import java.util.logging.Logger;

public abstract class SQLPlatform {
    
    private final Logger logger;
    
    
    
    /**
     * @param logger
     */
    public SQLPlatform(Logger logger) {
        this.logger = logger;
    }



    public abstract InputStream getResource(String file);



    public Logger getLogger() {
        return logger;
    }
}
