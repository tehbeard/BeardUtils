package me.tehbeard.utils.schematic;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import me.tehbeard.utils.map.tileEntities.TileEntity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public class BukkitDelayedSchematicLoader {

	private Schematic schematic;
	private Plugin plugin;
	public BukkitDelayedSchematicLoader(Plugin plugin,Schematic schematic){
		this.plugin = plugin;
		this.schematic = schematic;
	}

	public void paste(final Location l){

		long count =1L;
		
		//clear the area
		for(int y=schematic.getHeight();y>=0;y--){
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,new clearer(schematic,l,y) , count);
			count += 5L;
		}
		//lay out the area
		for(int layer = 0;layer<3;layer++){
			for(int y=0;y<schematic.getHeight();y+=2){
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,new runner(schematic,l,layer,y) , count);
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,new runner(schematic,l,layer,y+1) , count+1);
				count += 5L;
			}
		}

		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){

			public void run() {
				for(TileEntity t:schematic.getTileEntities()){
					t.place(l);
				}
			}
			
		}, count+5L);


		for(TileEntity t:schematic.getTileEntities()){
			t.place(l);
		}
	}

		private class runner implements Runnable{
		
		private Schematic sch;
		private Location l;
		private int layer;
		private int y;

		
		public runner(Schematic sch,Location l,int layer,int y){
			this.sch = sch;
			this.l = l;
			this.layer = layer;
			this.y = y;
		}
		public void run() {
			if(y<sch.getHeight()){
				for(int z = 0;z<sch.getLength();z++){
					for(int x = 0;x<sch.getWidth();x++){
						if(BlockType.getOrder(sch.getBlockId(x, y, z)) == layer){
							l.getWorld().getBlockAt(l.getBlockX() + x, l.getBlockY() + y, l.getBlockZ() + z).setTypeIdAndData(sch.getBlockId(x, y, z),
									(byte)sch.getBlockData(x, y, z),false);
							l.getWorld().getBlockAt(l.getBlockX() + x, l.getBlockY() + y, l.getBlockZ() + z).getState().update();
						}

					}	
				}
			}
			System.out.println("layer done y: " + y + " type: " + layer);

		}

	}
		
		private class clearer implements Runnable {

			private Schematic sch;
			private Location l;
			private int y;
			public clearer(Schematic schematic, Location l, int y) {
				this.sch = schematic;
				this.l = l;
				this.y=y;
				
			}

			public void run() {
				for(int z = 0;z<sch.getLength();z++){
					for(int x = 0;x<sch.getWidth();x++){

							l.getWorld().getBlockAt(l.getBlockX() + x, l.getBlockY() + y, l.getBlockZ() + z).setTypeIdAndData(0,(byte)0,false);
							l.getWorld().getBlockAt(l.getBlockX() + x, l.getBlockY() + y, l.getBlockZ() + z).getState().update();
						}

					}
				System.out.println("layer cleared y: " + y);

			}

		}

	
}
