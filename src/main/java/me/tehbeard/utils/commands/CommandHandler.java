package me.tehbeard.utils.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.permissions.Permissible;

public class CommandHandler implements Listener {


    private Map<String,CommandInfo> commandMap;

    public CommandHandler(){
        commandMap = new HashMap<String, CommandInfo>();
    }


    /**
     * Add a command executor to this handler
     * @param executor executor to add
     * @return true if added, false if not
     */
    public boolean addCommand(Object executor){

        for(Method m : executor.getClass().getMethods()){
            CommandDescriptor scrip = m.getAnnotation(CommandDescriptor.class);
            if(scrip != null){
                if(Modifier.isStatic(m.getModifiers())){
                    String permission = "";
                    Class<?>[] params = m.getParameterTypes();
                    if(params.length != 1){
                        throw new IllegalArgumentException("Invalid number of parameters for function handling " + scrip.label());
                    }
                    
                    if(!params[0].equals(ArgumentPack.class)){
                        throw new IllegalArgumentException("Parameter must be of type ArgumentPack for function handling " + scrip.label());
                    }
                    CommandPermission cp = m.getAnnotation(CommandPermission.class);
                    if(cp!=null){permission = cp.permissionNode();}
                    CommandInfo ci = new CommandInfo(m, permission);
                    
                    String tag = "";
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
                    commandMap.put(tag, ci);
                }
                else
                {
                    throw new IllegalStateException("Command " + scrip.label() + " Must be a static method");
                }
            }
            
            

        }
        return true; 
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event){
        if(event.isCancelled()){return;}
        String cmd = event.getMessage().split(" ")[0];
        if(!commandMap.containsKey(cmd)){return;}

        CommandInfo c = commandMap.get(cmd);
        if(!hasPermission(event.getPlayer(),c)){return;}


        String[] raw = event.getMessage().split(" ");
        String[] args = new String[raw.length - 1];

        for(int i = 1;i<raw.length;i++){
            args[i-1]=raw[i];
        }

        c.execute(null);

    }


    private boolean hasPermission(Permissible p,CommandInfo executor){
        return p.hasPermission(executor.permission) || executor.permission=="";
    }

    private class CommandInfo {
        private final Method method;
        public final String permission;
        public CommandInfo(Method method,String permission){
            this.method = method;
            this.permission = permission;
        }
        
        public void execute(ArgumentPack pack){
            try {
                method.invoke(null, pack);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
