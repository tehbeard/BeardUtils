package me.tehbeard.utils.testSuite.commands;

import me.tehbeard.utils.commands.ArgumentPack;
import me.tehbeard.utils.commands.CommandBooleanFlags;
import me.tehbeard.utils.commands.CommandDescriptor;
import me.tehbeard.utils.commands.CommandOptionFlags;

public class BCommand {

    
    @CommandDescriptor(label="create")
    @CommandBooleanFlags({"a"})
    @CommandOptionFlags({"type","c","d"})
    public static boolean command(ArgumentPack pack){
        
        System.out.println("Name: "+ pack.get(0));
        System.out.println("Type: "+ pack.getOption("type"));
        System.out.println("C   : "+ pack.getOption("c"));
        System.out.println("A   : " + pack.getFlag("a"));
        
        
        return true;
    }
}
