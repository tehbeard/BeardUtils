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
public abstract class InvMenu implements InventoryHolder, Listener {

    private Inventory inv;

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
        onEvent(event);

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
        onPaint(event);

        for (HumanEntity he : event.getViewers()) {
            ((Player) he).updateInventory();
        }
    }

    protected abstract void onPaint(InventoryDragEvent event);

    protected abstract void onEvent(InventoryClickEvent event);
}
