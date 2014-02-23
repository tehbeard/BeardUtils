package com.tehbeard.utils.json.item;


import org.bukkit.inventory.ItemStack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonItem {
    
    @Expose
    @SerializedName("Slot")
    private int slot;
    
    @Expose
    private int id;
    
    @Expose
    @SerializedName("Damage")
    private int damage;
    
    @Expose
    @SerializedName("Count")
    private int count;
    
    @Expose
    private JsonNbtTag tag;
    
    public JsonItem(){}
    
    @SuppressWarnings("deprecation")
    public JsonItem(int slot,ItemStack item){
        this.slot = slot;
        
        this.id = item.getTypeId();
        this.damage = item.getDurability();
        this.count = item.getAmount();
        this.tag = new JsonNbtTag(item.getItemMeta());
    }
}
