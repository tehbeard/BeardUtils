package com.tehbeard.utils.json;

import javax.naming.OperationNotSupportedException;

import org.bukkit.inventory.ItemStack;

import com.tehbeard.utils.json.item.JsonItem;
import com.tehbeard.utils.json.item.JsonNbtTag;

/**
 * Wrapper interface to handle various json
 * Implementations may throw {@link OperationNotSupportedException}, particularly when using Native (Bukkit Unsafe "API") variant 
 * @author James
 *
 */
public interface IMinecraftJsonParser {
    
    
    // Item related functions
    public JsonItem itemToJson(ItemStack is);
    public JsonNbtTag tagToJson(ItemStack is);
    
    public ItemStack jsonToItem(String json);
    public ItemStack applyJsonTagToItem(ItemStack is, String json);

    
}
