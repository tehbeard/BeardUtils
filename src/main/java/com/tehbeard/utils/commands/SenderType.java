//TODO : Refactor or pull in sk89q's library
//package com.tehbeard.utils.commands;
//
//import org.bukkit.command.BlockCommandSender;
//import org.bukkit.command.CommandSender;
//import org.bukkit.command.ConsoleCommandSender;
//import org.bukkit.entity.Player;
//
//@SuppressWarnings("unchecked")
//public enum SenderType {
//    CONSOLE(ConsoleCommandSender.class), PLAYER(Player.class),COMMANDBLOCK(BlockCommandSender.class), ALL(CommandSender.class);
//
//    private Class<? extends CommandSender>[] valid;
//
//    SenderType(Class<? extends CommandSender>... senders) {
//        this.valid = senders;
//    }
//
//    public boolean isValid(Object sender) {
//        for (Class<? extends CommandSender> c : this.valid) {
//            if (c.isAssignableFrom(sender.getClass())) {
//                return true;
//            }
//        }
//        return false;
//
//    }
//}