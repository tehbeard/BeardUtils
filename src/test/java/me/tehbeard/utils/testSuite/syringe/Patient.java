package me.tehbeard.utils.testSuite.syringe;

import me.tehbeard.utils.syringe.Inject;

public class Patient {

    @Inject
    String name = "john";
    
    public String toString(){return name;}
}
