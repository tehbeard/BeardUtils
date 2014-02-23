package com.tehbeard.utils.json.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonNbtTag {

    //Enchantments and stored enchantments
    @Expose
    @SerializedName("ench")
    public List<Ench> ench;

    @Expose
    @SerializedName("StoredEnchantments")
    public List<Ench> storedEnch;
    public class Ench{
        public Ench(int id, int lvl) {
            this.id = id;
            this.lvl = lvl;
        }
        @Expose
        public int id;
        @Expose
        public int lvl;
    }

    //Anvil repair cost
    @Expose
    @SerializedName("RepairCost")
    public int repairCost;

    //TODO - Attribute modifiers

    //Book
    @Expose
    public String title;
    @Expose
    public String author;
    @Expose
    public List<String> pages;

    //Display sub category
    @Expose
    public JsonNbtDisplay display;
    public class JsonNbtDisplay{
        @Expose
        public int color;
        @Expose
        @SerializedName("Name")
        public String name;
        @Expose
        @SerializedName("Lore")
        public List<String> lore;
    }

    //TODO - Custom Potion effect data

    //Skull
    @Expose
    @SerializedName("SkullOwner")
    public String skullOwner;

    //TODO - Fireworks
    
    public JsonNbtTag(){}

    public JsonNbtTag(ItemMeta itemMeta) {
        if(itemMeta.hasDisplayName()){
            initDisplay();
            display.name = itemMeta.getDisplayName();
        }
        if(itemMeta.hasLore()){
            initDisplay();
            display.lore = itemMeta.getLore();
        }
        if(itemMeta.hasEnchants()){
            ench = new ArrayList<Ench>();
            for( Entry<Enchantment, Integer> e : itemMeta.getEnchants().entrySet()){
                ench.add(new Ench(e.getKey().getId(),e.getValue()));
            }
        }
        if(itemMeta instanceof LeatherArmorMeta){
            initDisplay();
            display.color = ((LeatherArmorMeta) itemMeta).getColor().asRGB();
        }

        if(itemMeta instanceof Repairable){
            Repairable r = (Repairable)itemMeta;
            if(r.hasRepairCost()){
                repairCost = r.getRepairCost();
            }
        }

        if(itemMeta instanceof BookMeta){
            BookMeta r = (BookMeta)itemMeta;
            if(r.hasAuthor()){
                author = r.getAuthor();
            }
            if(r.hasTitle()){
                title = r.getTitle();
            }
            if(r.hasPages()){
                pages = r.getPages();
            }
        }

        if(itemMeta instanceof SkullMeta){
            SkullMeta r = (SkullMeta)itemMeta;
            if(r.hasOwner()){
                skullOwner = r.getOwner();
            }
        }

        if(itemMeta instanceof EnchantmentStorageMeta){
            EnchantmentStorageMeta ee = (EnchantmentStorageMeta)itemMeta;
            if(ee.hasStoredEnchants()){
                storedEnch = new ArrayList<Ench>();
                for( Entry<Enchantment, Integer> e : ee.getStoredEnchants().entrySet()){
                    storedEnch.add(new Ench(e.getKey().getId(),e.getValue()));
                }
            }
        }
    }

    public void initDisplay(){
        if(display == null){
            display = new JsonNbtDisplay();
        }
    }

}
