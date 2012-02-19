package me.tehbeard.utils.map;

import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.util.List;

import me.tehbeard.utils.map.tileEntities.TileEntity;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.NbtIo;
import com.mojang.nbt.Tag;




/**
 * Processes NBT data to provide chunk data access
 * @author James
 *
 */

/*
 * TAG_Compound("Level"): Chunk data.
 * TAG_Byte_Array("HeightMap"): 256 bytes of heightmap data. 16 x 16. Each byte records the lowest level in each column where the light from the sky is at full strength. Speeds computing of the SkyLight. Note: This array's indexes are ordered Z,X whereas the other array indexes are ordered X,Z,Y.
 * TAG_List("Entities"): Each TAG_Compound in this list defines an entity in the chunk. See Entity Format below.
 * TAG_List("TileEntities"): Each TAG_Compound in this list defines a tile entity in the chunk. See Tile Entity Format below.
 * TAG_List("TileTicks"): Each TAG_Compound in this list is an "active" block in this chunk waiting to be updated. These are used to save the state of redstone machines, falling sand or water, and other activity. See Tile Tick Format below.
 * TAG_Long("LastUpdate"): Tick when the chunk was last saved.
 * TAG_Int("xPos"): X position of the chunk. Should match the file name.
 * TAG_Int("zPos"): Z position of the chunk. Should match the file name.
 * TAG_Byte("TerrainPopulated"): 1 or not present (true/false) indicate whether the terrain in this chunk was populated with special things. (Ores, special blocks, trees, dungeons, flowers, waterfalls, etc.) If set to zero then minecraft will regenerate the chunk.
 * TAG_Compound("Sections")
 *   TAG_Compound()
 *     TAG_Int("Y"): Section of the chunk this is, 0 is 0-15, 1 16-31 etc,
 *     TAG_Byte_Array("Blocks"): 4096 bytes of block IDs defining the terrain. 8 bits per block. See Block Format below for byte ordering.
 *     TAG_Byte_Array("AddBlocks"): 4096 bytes provides upper 4 bits of block id's
 *     TAG_Byte_Array("Data"): 4096 bytes of block data additionally defining parts of the terrain. 4 bits per block.
 *     TAG_Byte_Array("SkyLight"): 4096 bytes recording the amount of sun or moonlight hitting each block. 4 bits per block. Makes day/night transitions smoother compared to recomputing per level change.
 *  TAG_Byte_Array("Biomes") : z/x ordered array of biome values    
 */
public class Chunk {

	int xPos,zPos;
	byte[] Biomes;
	byte[] HeightMap;
	Section[] sections;
	List<TileEntity> tileEntities;
	
	public final CompoundTag chunk;

	public Chunk(CompoundTag chunk){
		this.chunk = chunk;
	}
	


	
	private class Section{
		byte Y;
		byte[] Blocks;//4096
		DataLayer AddBlocks;
		DataLayer Data;
		DataLayer SkyLight;
		DataLayer BlockLight;//2048
		
		private DataLayer getLayer(byte[] data){
			return new DataLayer(data.length >0? data : new byte[2048],4);
		}
		
		Section(CompoundTag tag){
			Y = tag.getByte("Y");
			Blocks     = tag.getByteArray("Blocks");
			AddBlocks  = getLayer(tag.getByteArray("AddBlocks") );
			Data       = getLayer(tag.getByteArray("Data")      );
			SkyLight   = getLayer(tag.getByteArray("SkyLight")  );
			BlockLight = getLayer(tag.getByteArray("BlockLight"));
		}
		
		public int getBlockId(int x,int y,int z){
			if(
					((x >=0 && x<16)&&
					(y >=0 && y<16)&&
					(z >=0 && z<16)) == false
					){
				throw new IllegalArgumentException("Out of Bounds" + x + ","+ y + ","+ z);
			}
			return (AddBlocks.get(x, y, z) <<8) | Blocks[(y << 8) | (z << 4) | x];
		}
		
		public int getData(int x,int y,int z){
			if(
					((x >=0 && x<16)&&
					(y >=0 && y<16)&&
					(z >=0 && z<16)) == false
					){
				throw new IllegalArgumentException("Out of Bounds" + x + ","+ y + ","+ z);
			}
			return Data.get(x,y,z);
		}
		
		public int getSkyLight(int x,int y,int z){
			if(
					((x >=0 && x<16)&&
					(y >=0 && y<16)&&
					(z >=0 && z<16)) == false
					){
				throw new IllegalArgumentException("Out of Bounds" + x + ","+ y + ","+ z);
			}
			return SkyLight.get(x,y,z);
		}
		
		public int getBlockLight(int x,int y,int z){
			if(
					((x >=0 && x<16)&&
					(y >=0 && y<16)&&
					(z >=0 && z<16)) == false
					){
				throw new IllegalArgumentException("Out of Bounds" + x + ","+ y + ","+ z);
			}
			return BlockLight.get(x,y,z);
		}
		
		//setters
		public void setBlockId(int x,int y,int z,int val){
			if(
					((x >=0 && x<16)&&
					(y >=0 && y<16)&&
					(z >=0 && z<16)) == false
					){
				throw new IllegalArgumentException("Out of Bounds" + x + ","+ y + ","+ z);
			}
			AddBlocks.set(x, y, z,val >>8);
			Blocks[(y << 8) | (z << 4) | x] = (byte) (val % 256); 
		}
		
		public void setData(int x,int y,int z,int val){
			if(
					((x >=0 && x<16)&&
					(y >=0 && y<16)&&
					(z >=0 && z<16)) == false
					){
				throw new IllegalArgumentException("Out of Bounds" + x + ","+ y + ","+ z);
			}
			Data.set(x,y,z,val);
		}
		
		public void setSkyLight(int x,int y,int z,int val){
			if(
					((x >=0 && x<16)&&
					(y >=0 && y<16)&&
					(z >=0 && z<16)) == false
					){
				throw new IllegalArgumentException("Out of Bounds" + x + ","+ y + ","+ z);
			}
			SkyLight.set(x,y,z,val);
		}
		
		public void setBlockLight(int x,int y,int z,int val){
			if(
					((x >=0 && x<16)&&
					(y >=0 && y<16)&&
					(z >=0 && z<16)) == false
					){
				throw new IllegalArgumentException("Out of Bounds" + x + ","+ y + ","+ z);
			}
			BlockLight.set(x,y,z,val);
		}
		
	}
	
}
