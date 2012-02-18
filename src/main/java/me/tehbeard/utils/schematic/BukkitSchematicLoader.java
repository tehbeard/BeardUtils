package me.tehbeard.utils.schematic;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import me.tehbeard.utils.map.tileEntities.TileEntity;

import org.bukkit.Location;

public class BukkitSchematicLoader {

	private Schematic schematic;

	public BukkitSchematicLoader(Schematic schematic){
		this.schematic = schematic;
	}

	public void paste(Location l){

		addBlocks(l,0);
		addBlocks(l,1);
		addBlocks(l,2);
		
		for(TileEntity t:schematic.getTileEntities()){
			t.place(l);
		}
	}

	private void addBlocks(Location l,int layer){
		for(int y = 0;y<schematic.getHeight();y++){
			for(int z = 0;z<schematic.getLength();z++){
				for(int x = 0;x<schematic.getWidth();x++){
					if(BlockType.getOrder(schematic.getBlockId(x, y, z)) == layer){
						l.getWorld().getBlockAt(l.getBlockX() + x, l.getBlockY() + y, l.getBlockZ() + z).setTypeIdAndData(schematic.getBlockId(x, y, z),
								(byte)schematic.getBlockData(x, y, z),false);
						l.getWorld().getBlockAt(l.getBlockX() + x, l.getBlockY() + y, l.getBlockZ() + z).getState().update();
					}

				}	
			}	
		}
	}

}
