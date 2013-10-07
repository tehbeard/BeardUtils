package com.tehbeard.utils.testSuite.commands;

import com.tehbeard.utils.commands.ArgumentPack;
import com.tehbeard.utils.commands.CommandDescriptor;

public class ACommand {

    @CommandDescriptor(label = "command")
    public static boolean command(ArgumentPack pack) {
        System.out.println("Called!");
        return false;
    }
}
