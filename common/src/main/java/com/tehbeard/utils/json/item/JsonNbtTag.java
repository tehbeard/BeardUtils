package com.tehbeard.utils.json.item;

import java.util.List;

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

    public class Ench {

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
    public JsonNbtDisplay display = new JsonNbtDisplay();

    public class JsonNbtDisplay {

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
    
}
