package me.tehbeard.utils.map.tileEntities;




import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;

import com.mojang.nbt.CompoundTag;

@TileEntityType(id="MobSpawner")
public class TileSpawner extends TileEntity {

    private String type;
    private int delay;
    public void setData(CompoundTag tag) {
        super.setData(tag);
        type = tag.getString("EntityId");
        delay = tag.getShort("Delay");
    }


    @Override
    public String toString() {
        return "TileMobSpawner [type=" + type+ "]";
    }

    @Override
    public void place(Location l) {

        BlockState state = l.getWorld().getBlockAt(l.clone().add(getX(), getY(), getZ())).getState();
        if(state instanceof CreatureSpawner ){
            CreatureSpawner spawner = (CreatureSpawner)state;
            spawner.setCreatureTypeByName(type);
            spawner.setDelay(delay);
            spawner.update(true);
        }

    }



}
;