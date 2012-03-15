package me.tehbeard.utils.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public ArgumentPack(String[] boolFlags,String[] flagOpts,String rawArguments){
        //initialise
        strArgs = new ArrayList<String>();


        System.out.println(rawArguments);
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
                        l("" + token.length());
                        tokens.add(token.toString().trim());
                    }
                    l("adding token");
                    token = new StringBuilder();
                }
                ;break;
            case '"':
            inQuotes = !inQuotes;
            l("Swapping to quote mode " +( inQuotes ?"on":"off"));
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
                String t = tok.replace("-","");
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

    public static void main(String[] args){
        String arg = "hello \"world trees\" this is a \"test of the argument pack.\"";
        ArgumentPack pack = new ArgumentPack(new String[0], new String[0],arg);

        l(pack.strArgs.toString());
    }
    public static void l(String l){
        System.out.println(l);
    }
}
