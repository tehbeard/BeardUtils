package me.tehbeard.utils.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;

/**
 * Constructs an argument pack to allow building commands eaiser
 * Takes an argument string as input and provides simple methods to access
 * boolean flags (-a , -b)
 * option flags (-f "flag")
 * Concatenating strings "this will not be 6 args"
 * @author james
 *
 */
public class ArgumentPack {

    private Set<String> boolFlags;
    private Map<String,String> flagOptions;
    private List<String> strArgs;
    private CommandSender sender;
    
    public CommandSender getSender() {
        return sender;
    }
   
    public ArgumentPack(CommandSender sender,String[] boolFlags,String[] flagOpts,String[] rawArguments){
        this(boolFlags,flagOpts,rawArguments);
        this.sender = sender;
    }
    public ArgumentPack(String[] boolFlags,String[] flagOpts,String[] rawArguments){
        String r = "";
        for(String s : rawArguments){
            if(r.length() > 0){r+=" ";}
            r+=s;
        }
        initialise(boolFlags,flagOpts,r);
    }
    public ArgumentPack(String[] boolFlags,String[] flagOpts,String rawArguments){
        initialise(boolFlags,flagOpts,rawArguments);
    }
    
    private void initialise(String[] boolFlags,String[] flagOpts,String rawArguments){
        //initialise
        strArgs = new ArrayList<String>();
        this.boolFlags = new HashSet<String>();
        this.flagOptions = new HashMap<String, String>();
        boolean inQuotes = false;
        StringBuilder token = new StringBuilder();
        List<String> tokens = new ArrayList<String>();
        for(int i = 0; i< rawArguments.length();i++){
            char c = rawArguments.charAt(i);
            switch(c){
            case ' ':
                if(inQuotes){
                    token.append(c);
                }else{
                    if(token.length() > 0){
                        tokens.add(token.toString().trim());
                    }
                    token = new StringBuilder();
                }
                ;break;
            case '"':
            inQuotes = !inQuotes;
            break;
            default: token.append(c);break;
            }

        }
        if(token.length() > 0){
            tokens.add(token.toString().trim());
        }
        //parse list of tokens


        Iterator<String> it = tokens.iterator();
        while(it.hasNext()){
            String tok = it.next();
            //check if it's a potential option (optFlag off, starts with -
            if(tok.startsWith("-")){
                String t = tok.substring(1);
                if(inArray(boolFlags, t)){
                    this.boolFlags.add(t);
                    continue;
                }
                if(inArray(flagOpts, t)){
                    if(it.hasNext()){
                        this.flagOptions.put(t, it.next());
                    }
                    continue;
                }
            }
            strArgs.add(tok);
        }


    }

    private boolean inArray(String[] arr,String search){
        for(String a : arr){
            if(a.equalsIgnoreCase(search)){
                return true;
            }
        }
        return false;

    }

    /**
     * Return whether a boolean flag was set
     * @param flag
     * @return
     */
    public boolean getFlag(String flag){
        return boolFlags.contains(flag);
    }
    
    /**
     * Return value of option flag
     * @param flag value or null on not set
     * @return
     */
    public String getOption(String flag){
        return flagOptions.get(flag); 
    }
    
    /**
     * Number of string arguments
     * @return
     */
    public int size(){
        return strArgs.size();
    }
    
    /**
     * Get a string argument
     * @param index
     * @return
     */
    public String get(int index){
        return strArgs.get(index);
    }
    
    /**
     * Return string argument as number.
     * @param index
     * @return
     */
    public Number getNumber(int index){
        return Double.parseDouble(get(index));
    }

}
