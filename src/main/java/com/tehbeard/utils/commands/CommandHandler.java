package com.tehbeard.utils.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.permissions.Permissible;

/**
 * Processes commands inputed by a user Currently supports
 * CommandPreprocessEvent and manual call via executeCommand
 * 
 * @author James
 * 
 */
public class CommandHandler implements Listener {

    public Map<String, CommandInfo> commandMap;
    private Server server;
    
    public CommandHandler(Server server) {
        this.commandMap = new HashMap<String, CommandInfo>();
        this.server = server;
    }

    public CommandHandler() {
        this.commandMap = new HashMap<String, CommandInfo>();
        this.server = Bukkit.getServer();
    }

    
    public List<CommandInfo> addCommand(Class<?> executor){
        return addCommand(executor,null);
    }
    /**
     * Add a command executor to this handler
     * 
     * @param executor
     *            executor to add
     * @return true if added, false if not
     */
    public List<CommandInfo> addCommand(Class<?> executor,Object instance) {
        
        List<CommandInfo> commands = new ArrayList<CommandInfo>();

        for (Method m : executor.getMethods()) {
            CommandDescriptor scrip = m.getAnnotation(CommandDescriptor.class);
            if (scrip != null) {
                if (!m.getReturnType().equals(boolean.class)) {
                    throw new IllegalArgumentException(m.getName() + " Methods must return a boolean");
                }
                    Class<?>[] params = m.getParameterTypes();
                    if (params.length != 1) {
                        throw new IllegalArgumentException("Invalid number of parameters for function handling "
                                + scrip.label());
                    }

                    if (!params[0].equals(ArgumentPack.class)) {
                        throw new IllegalArgumentException(
                                "Parameter must be of type ArgumentPack for function handling " + scrip.label());
                    }

                    CommandPermission cp = m.getAnnotation(CommandPermission.class);
                    String permission = "";
                    if (cp != null) {
                        permission = cp.value();
                    }

                    CommandBooleanFlags cbf = m.getAnnotation(CommandBooleanFlags.class);
                    String[] _cbf = cbf != null ? cbf.value() : new String[0];

                    CommandOptionFlags cof = m.getAnnotation(CommandOptionFlags.class);
                    String[] _cof = cof != null ? cof.value() : new String[0];

                    

                    String tag = null;
                    if (!this.commandMap.containsKey(scrip.label())) {
                        tag = scrip.label();
                    } else {
                        for (String t : scrip.alias()) {
                            if (!this.commandMap.containsKey(t)) {
                                tag = t;
                                break;
                            }
                        }
                    }
                    if (tag == null) {
                       throw new IllegalStateException("Could not add CommandExecutor, name + aliases already taken for "
                                + scrip.label());
                    }
                    
                    CommandInfo ci = new CommandInfo(tag,instance,m, permission, _cbf, _cof, scrip.senderType());

                    if(server.getPluginCommand(tag) != null){
                        System.out.println(tag + " bound to PluginCommand");
                        server.getPluginCommand(tag).setExecutor(ci);
                        ci.isRealCommand = true;
                    }
                    else
                    {
                        System.out.println(tag + " running as preprocess command");
                        this.commandMap.put(tag, ci);
                    }
                    commands.add(ci);
            }

        }
        return commands;
    }

    /**
     * Handles a command event
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) {
            return;
        }
        System.out.println("event fired " + event.getMessage());

        event.setCancelled(executeCommand(event.getPlayer(), event.getMessage().substring(1)));

    }

    /**
     * Handles a command event from the console
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(ServerCommandEvent event) {

        executeCommand(event.getSender(), event.getCommand());
    }

    /**
     * execute a command as sender
     * 
     * @param sender
     *            who to execute as
     * @param command
     *            command to execute, should NOT have leading /
     * @return
     */
    public boolean executeCommand(CommandSender sender, String command) {

        String cmd = command.split(" ")[0];
        if (!this.commandMap.containsKey(cmd)) {
            return false;
        }

        CommandInfo c = getInfo(cmd);
        if (!hasPermission(sender, c)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return false;
        }

        if (!c.senderType.isValid(sender)) {

            return false;
        }

        String[] raw = command.split(" ");
        String[] args = new String[raw.length - 1];

        for (int i = 1; i < raw.length; i++) {
            args[i - 1] = raw[i];
        }

        return c.onCommand(sender, null, raw[0],args);
    }

    private boolean hasPermission(Permissible p, CommandInfo executor) {
        return p.hasPermission(executor.permission) || (executor.permission == "");
    }

    public class CommandInfo implements CommandExecutor {
        
        public final String     label;
        private final Object    object;
        private final Method    method;
        public final String     permission;
        public final String[]   boolFlags;
        public final String[]   optFlags;
        public final SenderType senderType;
        public boolean    isRealCommand = false;

        /**
         * @param method
         * @param permission
         * @param boolFlags
         * @param optFlags
         */
        public CommandInfo(String label,Object object,Method method, String permission, String[] boolFlags, String[] optFlags,
                SenderType senderType) {
            super();
            this.label = label;
            this.object = object;
            this.method = method;
            this.permission = permission;
            this.boolFlags = boolFlags;
            this.optFlags = optFlags;
            this.senderType = senderType;
        }

        public Boolean execute(ArgumentPack pack) {
            try {
                // enforce sender type validity
                if (pack == null) {
                    throw new IllegalArgumentException("MUST SUPPLY ARGUMENT PACK");
                }
                if ((pack.getSender() != null) && !this.senderType.isValid(pack.getSender())) {
                    pack.getSender().sendMessage(
                            "Command can only be executed by " + this.senderType.toString().toLowerCase());
                    return true;
                }
                return (Boolean) this.method.invoke(object, pack);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String cmdlbl, String[] args) {
            return execute(new ArgumentPack(sender, boolFlags, optFlags, args));
        }

    }

    public CommandInfo getInfo(String comm) {
        return this.commandMap.get(comm);
    }
    
    public void removeCommand(CommandInfo command){
        if(command.isRealCommand){
            server.getPluginCommand(command.label).setExecutor(null);
        }
        else
        {
            commandMap.remove(command.label);
        }
    }

}
