package com.tehbeard.utils.invmenu;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

/**
 * Class to add gui menus to minecraft using inventories
 *
 * @author James
 *
 */
public final class InvMenu implements InventoryHolder, Listener {

    private Inventory inv;
    private IMenuHandler handler;

    public InvMenu(Plugin plugin, int rows, String title) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.inv = Bukkit.createInventory(this, Math.max(0, Math.min(rows, 6)) * 9, title);
    }

    public void cleanup() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() != this) {
            return;
        }
        event.setCancelled(true);
        getHandler().onEvent(event);

        for (HumanEntity he : event.getViewers()) {
            ((Player) he).updateInventory();
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPaintEvent(InventoryDragEvent event) {
        if (event.getInventory().getHolder() != this) {
            return;
        }
        event.setCancelled(true);
        getHandler().onPaint(event);

        for (HumanEntity he : event.getViewers()) {
            ((Player) he).updateInventory();
        }
    }

    /**
     * @return the handler
     */
    public IMenuHandler getHandler() {
        return handler;
    }

    /**
     * @param handler the handler to set
     */
    public void setHandler(IMenuHandler handler) {
        this.handler = handler;
    }

    public interface IMenuHandler {

        public void onPaint(InventoryDragEvent event);

        public void onEvent(InventoryClickEvent event);
    }
}
