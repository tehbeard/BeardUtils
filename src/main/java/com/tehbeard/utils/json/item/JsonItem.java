package com.tehbeard.utils.json.item;


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
    
    
}
