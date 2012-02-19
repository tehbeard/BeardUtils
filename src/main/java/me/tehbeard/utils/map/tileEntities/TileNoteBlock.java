package me.tehbeard.utils.map.tileEntities;


import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.NoteBlock;

import com.mojang.nbt.CompoundTag;

@TileEntityType(id="Music")
public class TileNoteBlock extends TileEntity {

	private byte note;
	public void setData(CompoundTag tag) {
		super.setData(tag);
		note = tag.getByte("note");

	}


	@Override
	public String toString() {
		return "TileNoteBlock [note=" + note+ "]";
	}

	@Override
	public void place(Location l) {
		
		BlockState state = l.getWorld().getBlockAt(l.clone().add(getX(), getY(), getZ())).getState();
		if(state instanceof NoteBlock){
			NoteBlock noteblock = (NoteBlock)state;
			noteblock.setRawNote(note);
			noteblock.update(true);
		}
	}



}
