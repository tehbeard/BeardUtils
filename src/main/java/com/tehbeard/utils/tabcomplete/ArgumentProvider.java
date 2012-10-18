package com.tehbeard.utils.tabcomplete;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;


/**
 * Provides a list of possible arguments for tab completion
 * @author James
 *
 */
public interface ArgumentProvider {

    public static final ArgumentProvider PLAYER = new ArgumentProvider(){

        public List<String> provide(Permissible sender, String match) {
            List<String> l = new ArrayList<String>();
            for(Player p : Bukkit.getOnlinePlayers()){
                l.add(p.getName());
            }
            return l;
        }};
        
    public static final ArgumentProvider YESNO = new ArgumentProvider(){
        public List<String> provide(Permissible sender, String match) {
            List<String> l = new ArrayList<String>();
            l.add("yes");
            l.add("no");
            return l;
        }};
        
    public static final ArgumentProvider TRUEFALSE = new ArgumentProvider(){
        public List<String> provide(Permissible sender, String match) {
            List<String> l = new ArrayList<String>();
            l.add("true");
            l.add("false");
            return l;
        }};
    
    public List<String> provide(Permissible sender,String match);
}
