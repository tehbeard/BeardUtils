package me.tehbeard.utils.map.tileEntities;

import org.bukkit.Location;

import com.mojang.nbt.CompoundTag;

/**
 * Represents a tile entity
 * @author James
 *
 */

public abstract class TileEntity {
	
	private int x,y,z;
	private String id;
	/**
	 * @param x location of tile entity
	 * @param y location of tile entity
	 * @param z location of tile entity
	 * @param id name of entity
	 */
	public void setData(CompoundTag tag) {
		
		this.x = tag.getInt("x");
		this.y = tag.getInt("y");
		this.z = tag.getInt("z");
		this.id = tag.getName();
	}
	public final int getX() {
		return x;
	}
	public final int getY() {
		return y;
	}
	public final int getZ() {
		return z;
	}
	public final String getId() {
		return id;
	}

	public abstract void place(Location l);
	
	
}
