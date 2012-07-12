package me.tehbeard.utils.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permissible;

public class CommandHandler implements Listener {

    private Map<String,CommandExecutor> commandMap;
    
    public CommandHandler(){
        commandMap = new HashMap<String, CommandExecutor>();
    }
    
    
    /**
     * Add a command executor to this handler
     * @param executor executor to add
     * @return true if added, false if not
     */
    public boolean addCommand(CommandExecutor executor){
        CommandDescriptor scrip = executor.getClass().getAnnotation(CommandDescriptor.class);
        if(scrip==null){throw new IllegalArgumentException("No @CommandDescriptor found!");}
        String tag = null;
        
        if(!commandMap.containsKey(scrip.label())){
            tag=scrip.label();
        }
        else
        {
            for(String t:scrip.alias()){
                if(!commandMap.containsKey(t)){
                    tag=t;
                    break;
                }
            }
        }
        if(tag==null){System.out.println("Could not add CommandExecutor, name + aliases already taken for " + scrip.label() );return false;}
        
        commandMap.put(tag,executor);
        
        return true; 
    }
    
    @EventHandler(priority=EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(event.isCancelled()){return;}
        String cmd = event.getMessage().split(" ")[0];
        if(!commandMap.containsKey(cmd)){return;}
        
        CommandExecutor c = commandMap.get(cmd);
        if(!hasPermission(event.getPlayer(),c)){return;}
        
        
        String[] raw = event.getMessage().split(" ");
        String[] args = new String[raw.length - 1];
        
        for(int i = 1;i<raw.length;i++){
            args[i-1]=raw[i];
        }
        
        c.onCommand(event.getPlayer(),null ,cmd, args);
        
    }
    
    
    private boolean hasPermission(Permissible p,CommandExecutor executor){
        CommandPermission pscrip = executor.getClass().getAnnotation(CommandPermission .class);
        if(pscrip==null){return true;}
        return p.hasPermission(pscrip.permissionNode());
    }
    
}
