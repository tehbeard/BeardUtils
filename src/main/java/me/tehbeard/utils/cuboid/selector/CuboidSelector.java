package me.tehbeard.utils.cuboid.selector;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.tehbeard.utils.cuboid.Cuboid;
import me.tehbeard.utils.session.SessionStore;

public class CuboidSelector implements Listener{

    private SessionStore<Cuboid> session = new SessionStore<Cuboid>();
    private Material tool;
    
    private Set<String> active = new HashSet<String>();
    
    public CuboidSelector(Material tool){
        this.tool = tool;
    }
    
    public void setActive(String player){
        active.add(player);
        session.putSession(player,new Cuboid());
    }
    
    public void setInActive(String player){
        active.remove(player);
        session.clearSession(player);
    }
    
    public boolean isActive(String player){
        return active.contains(player);
    }
    
    public boolean toggle(String player){
        if(isActive(player)){
            setInActive(player);
        }
        else{
            setActive(player);
        }
        
        return isActive(player);
    }
    @EventHandler
    public void click(PlayerInteractEvent event){
        String player = event.getPlayer().getName();
        if(!isActive(player)){ return; }
        if(event.getPlayer().getItemInHand().getType()!=tool){ return; }
        event.setCancelled(true);
        
        switch(event.getAction()){
        case LEFT_CLICK_BLOCK:session.getSession(player).setV1(event.getClickedBlock().getLocation().toVector());break;
        case RIGHT_CLICK_BLOCK:session.getSession(player).setV2(event.getClickedBlock().getLocation().toVector());break;
        }
    }
    
    public Cuboid getCuboid(String player){
        return session.getSession(player);
    }
    
}
