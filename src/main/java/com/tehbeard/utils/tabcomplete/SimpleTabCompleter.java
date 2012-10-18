package com.tehbeard.utils.tabcomplete;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * Represents a simple tab completer, for simpler commands
 * this is useful for commands such that follow a simple structure
 * e.g.
 * /group add|remove [PLAYER]
 * @author James
 *
 */
public class SimpleTabCompleter implements TabCompleter {
    
    ArgumentProvider[] providers;
    
   
    public SimpleTabCompleter(ArgumentProvider... providers){
        this.providers= providers;
    }
    
    public List<String> onTabComplete(CommandSender sender, Command cmd,
            String alias, String[] args) {
        
        
        if(args.length > providers.length){
            return null;
        }
        return providers[args.length].provide(sender, args[args.length-1]);

    }

}
