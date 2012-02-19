package me.tehbeard.utils.schematic;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.tehbeard.utils.factory.ConfigurableFactory;
import me.tehbeard.utils.map.tileEntities.*;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.NbtIo;
import com.mojang.nbt.Tag;

/**
 * Provides a way to access and manipulate .schematic files
 * @author James
 *
 */
public class Schematic {

	private short width = 0;
	private short height = 0;
	private short length = 0;

	private byte[] blocks;
	private byte[] addBlocks;
	private byte[] blockData;

	private final List<TileEntity> tileEntities = new ArrayList<TileEntity>();

	public Schematic(File file) throws IOException{

		loadSchematic(file);
	}



	/**
	 * loads the schematic data into memory
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void loadSchematic(File file) throws IOException{
		CompoundTag tag  = NbtIo.readCompressed(new FileInputStream(file));

		if(!tag.getName().equalsIgnoreCase("schematic")){
			throw new IOException("File is not a valid schematic");
		}

		width = tag.getShort("Width");
		height = tag.getShort("Height");
		length = tag.getShort("Length");
		int size = width+height+length;
		blocks = new byte[size];
		addBlocks = new byte[size];
		blockData = new byte[size];

		//read in block data;
		blocks = tag.getByteArray("Blocks");
		if(tag.contains("AddBlocks")){
			addBlocks = tag.getByteArray("AddBlocks");
		}
		blockData = tag.getByteArray("Data");

		System.out.println("Schematic block data is width : " + width +
				" height: " + height +
				" length: " +length);
		//prepare the factory
		
		
		ListTag<CompoundTag> tileEntityTag = (ListTag<CompoundTag>) tag.getList("TileEntities");

		for(int i = 0 ; i< tileEntityTag.size();i++){
			CompoundTag tileEntity = tileEntityTag.get(i);
			TileEntity t = TileEntityFactory.getInstance().getProduct(tileEntity.getString("id"));
			if(t!=null){
				t.setData(tileEntity);
				//System.out.println(t.toString());
				this.tileEntities.add(t);
			}
			else
			{
				System.out.println("Could not load entity " + tileEntity.getString("id"));
			}
		}
	}

	public int getBlockId(int x,int y,int z){

		int index =  y * width *length + z * width + x;
		if(index < 0 || index >= blocks.length){
			return 0;
		}
		//((addBlocks[pos] << 8 )
		return blocks[index];
	}

	public byte getBlockData(int x,int y,int z){

		int index =  y * width *length + z * width + x;
		if(index < 0 || index >= blockData.length){
			return 0;
		}
		return blockData[index];
	}

	public final short getWidth() {
		return width;
	}

	public final short getHeight() {
		return height;
	}

	public final short getLength() {
		return length;
	}



	public final List<TileEntity> getTileEntities() {
		return tileEntities;
	}




}
