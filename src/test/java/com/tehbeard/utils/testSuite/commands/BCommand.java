package com.tehbeard.utils.testSuite.commands;

import org.bukkit.command.CommandSender;

import com.tehbeard.utils.commands.ArgumentPack;
import com.tehbeard.utils.commands.CommandBooleanFlags;
import com.tehbeard.utils.commands.CommandDescriptor;
import com.tehbeard.utils.commands.CommandOptionFlags;

public class BCommand {

    @CommandDescriptor(label = "create")
    @CommandBooleanFlags({ "a" })
    @CommandOptionFlags({ "type", "c", "d" })
    public static boolean command(ArgumentPack<CommandSender> pack) {

        System.out.println("Name: " + pack.get(0));
        System.out.println("Type: " + pack.getOption("type"));
        System.out.println("C   : " + pack.getOption("c"));
        System.out.println("A   : " + pack.getFlag("a"));
        pack.getSender().sendMessage("SUCCESS");
        return true;
    }
}
