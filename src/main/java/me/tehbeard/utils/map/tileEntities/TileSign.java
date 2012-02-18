package me.tehbeard.utils.map.tileEntities;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import com.mojang.nbt.CompoundTag;

@TileEntityType(id="Sign")
public class TileSign extends TileEntity {

	private String[] line;
	public void setData(CompoundTag tag) {
		super.setData(tag);
		line = new String[4];
		line[0] = tag.getString("Text1");
		line[1] = tag.getString("Text2");
		line[2] = tag.getString("Text3");
		line[3] = tag.getString("Text4");

	}

	public void setLine(int i,String text){
		if(i >=0 && i<=3){
			line[i] = text;
		}

	}


	public String getLine(int i){
		if(i >=0 && i<=3){
			return line[i];
		}
		return null;
	}

	@Override
	public String toString() {
		return "TileSign [line=" + Arrays.toString(line) + "]";
	}

	@Override
	public void place(Location l) {
		
		BlockState state = l.getWorld().getBlockAt(l.clone().add(getX(), getY(), getZ())).getState();
		if(state instanceof Sign){
			Sign sign = (Sign)state;
			for(int i = 0;i<4;i++){
				sign.setLine(i, line[i]);
			}
			sign.update(true);
		}
	}



}
