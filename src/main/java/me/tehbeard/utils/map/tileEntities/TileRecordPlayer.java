package me.tehbeard.utils.map.tileEntities;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;

import com.mojang.nbt.CompoundTag;

@TileEntityType(id="RecordPlayer")
public class TileRecordPlayer extends TileEntity {

	private byte record;
	public void setData(CompoundTag tag) {
		super.setData(tag);
		record = tag.getByte("Record");

	}


	@Override
	public String toString() {
		return "TileSign [record=" + record+ "]";
	}

	@Override
	public void place(Location l) {
		
		BlockState state = l.getWorld().getBlockAt(l.clone().add(getX(), getY(), getZ())).getState();
		if(state instanceof Jukebox){
			Jukebox jukebox = (Jukebox)state;
			jukebox.setPlaying(Material.getMaterial(record));
			jukebox.update(true);
		}
	}



}
