package me.tehbeard.utils.cuboid.selector;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.tehbeard.utils.cuboid.Cuboid;
import me.tehbeard.utils.cuboid.selector.CuboidSelector.StatusIndicator.Activity;
import me.tehbeard.utils.session.SessionStore;

public class CuboidSelector implements Listener{

    private SessionStore<Cuboid> session = new SessionStore<Cuboid>();
    private Material tool;
    StatusIndicator indicator;
    
    private Set<String> active = new HashSet<String>();
    
    
    public CuboidSelector(Material tool,StatusIndicator indicator){
        this.indicator = indicator;
        this.tool = tool;
    }
    
    private void indicate(Activity activity,Player player,Cuboid cuboid){
        if(indicator!=null){
            indicator.cuboidUpdate(activity, player, cuboid);
        }
    }
    
    public void setActive(Player player){
        active.add(player.getName());
        
        session.putSession(player.getName(),new Cuboid());
        indicate(Activity.ACTIVE,player,null);
    }
    
    public void setInActive(Player player){
        active.remove(player.getName());
        session.clearSession(player.getName());
        indicate(Activity.INACTIVE,player,null);
    }
    
    public boolean isActive(Player player){
        return active.contains(player.getName());
    }
    
    public boolean toggle(Player player){
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
        if(!isActive(event.getPlayer())){ return; }
        if(event.getPlayer().getItemInHand().getType()!=tool){ return; }
        event.setCancelled(true);
        
        switch(event.getAction()){
        case LEFT_CLICK_BLOCK:session.getSession(player).setV1(event.getClickedBlock().getLocation().toVector());indicate(Activity.SELECT_CORNER_ONE,event.getPlayer(),session.getSession(player));break;
        case RIGHT_CLICK_BLOCK:session.getSession(player).setV2(event.getClickedBlock().getLocation().toVector());indicate(Activity.SELECT_CORNER_TWO,event.getPlayer(),session.getSession(player));break;
        }
    }
    
    public Cuboid getCuboid(String player){
        return session.getSession(player);
    }
    
    
    public interface StatusIndicator{
        public enum Activity {
            SELECT_CORNER_ONE,
            SELECT_CORNER_TWO,
            ACTIVE,
            INACTIVE
        }
        
        public void cuboidUpdate(Activity activity,Player player,Cuboid cuboid);
    }
}
