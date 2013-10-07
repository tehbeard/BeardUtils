package com.tehbeard.utils.cuboid.selector;

import java.util.HashSet;
import java.util.Set;

import com.tehbeard.utils.cuboid.Cuboid;
import com.tehbeard.utils.cuboid.selector.CuboidSelector.StatusIndicator.Activity;
import com.tehbeard.utils.session.SessionStore;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Standard class for selecting a region of a world (cuboid) Relies on
 * SessionStore and Cuboid packages
 * 
 * @author James
 * 
 */
public class CuboidSelector implements Listener {

    private SessionStore<Cuboid> session = new SessionStore<Cuboid>();
    private Material             tool;
    StatusIndicator              indicator;

    private Set<String>          active  = new HashSet<String>();

    public CuboidSelector(Material tool, StatusIndicator indicator) {
        this.indicator = indicator;
        this.tool = tool;
    }

    private void indicate(Activity activity, Player player, Cuboid cuboid) {
        if (this.indicator != null) {
            this.indicator.cuboidUpdate(activity, player, cuboid);
        }
    }

    public void setActive(Player player) {
        this.active.add(player.getName());

        this.session.putSession(player.getName(), new Cuboid());
        indicate(Activity.ACTIVE, player, null);
    }

    public void setInActive(Player player) {
        this.active.remove(player.getName());
        this.session.clearSession(player.getName());
        indicate(Activity.INACTIVE, player, null);
    }

    public boolean isActive(Player player) {
        return this.active.contains(player.getName());
    }

    public boolean toggle(Player player) {
        if (isActive(player)) {
            setInActive(player);
        } else {
            setActive(player);
        }

        return isActive(player);
    }

    @SuppressWarnings("incomplete-switch")
    @EventHandler
    public void click(PlayerInteractEvent event) {
        String player = event.getPlayer().getName();
        if (!isActive(event.getPlayer())) {
            return;
        }
        if (event.getPlayer().getItemInHand().getType() != this.tool) {
            return;
        }
        event.setCancelled(true);

        switch (event.getAction()) {
        case LEFT_CLICK_BLOCK:
            this.session.getSession(player).setV1(event.getClickedBlock().getLocation().toVector());
            indicate(Activity.SELECT_CORNER_ONE, event.getPlayer(), this.session.getSession(player));
            break;
        case RIGHT_CLICK_BLOCK:
            this.session.getSession(player).setV2(event.getClickedBlock().getLocation().toVector());
            indicate(Activity.SELECT_CORNER_TWO, event.getPlayer(), this.session.getSession(player));
            break;
        }
    }

    public Cuboid getCuboid(String player) {
        return this.session.getSession(player);
    }

    public interface StatusIndicator {
        public enum Activity {
            SELECT_CORNER_ONE, SELECT_CORNER_TWO, ACTIVE, INACTIVE
        }

        public void cuboidUpdate(Activity activity, Player player, Cuboid cuboid);
    }
}
