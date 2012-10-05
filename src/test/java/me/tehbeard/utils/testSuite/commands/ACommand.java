package me.tehbeard.utils.testSuite.commands;

import me.tehbeard.utils.commands.ArgumentPack;
import me.tehbeard.utils.commands.CommandDescriptor;
public class ACommand {

    
    @CommandDescriptor(label="command")
    public static boolean command(ArgumentPack pack){
        System.out.println("Called!");
        return false;
    }
}
