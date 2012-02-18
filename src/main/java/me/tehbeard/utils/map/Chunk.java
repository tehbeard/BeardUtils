package me.tehbeard.utils.map;

import com.mojang.nbt.CompoundTag;
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

	private final CompoundTag chunk;
	
	public Chunk(CompoundTag chunk){
		this.chunk = chunk;
	}
	public byte getBiome(int x, int z) {
		return chunk.getByteArray("Biomes")[(z*16)+4];
	}

	public byte getBlockData(int x, int y, int z) {
		return readDataLayer(x, y, z, "Data");
	}

	public int getBlockEmittedLight(int x, int y, int z) {
		return readDataLayer(x, y, z, "BlockLight");
	}

	public int getBlockSkyLight(int x, int y, int z) {
		// TODO Auto-generated method stub
		return readDataLayer(x, y, z, "SkyLight");
	}

	public int getBlockTypeId(int x, int y, int z) {
		// TODO Auto-generated method stub
		CompoundTag section = getSection(y/16);
		if(section!=null){
			return section.getByteArray("Blocks")[(y << 8) | (z << 4) | x];
		}
		
		return 0;
	}

	public int getHighestBlockYAt(int x, int z) {
		return chunk.getByteArray("HeightMap")[z*16+x];
	}

	public double getRawBiomeRainfall(int x, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getRawBiomeTemperature(int x, int z) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getX() {
		return chunk.getInt("xPos");
	}

	public int getZ() {
		return chunk.getInt("zPos");
	}

	
	private CompoundTag getSection(int y){
			
		//find the section
		for(Tag tag : chunk.getCompound("Sections").getAllTags()){
			if(((CompoundTag)tag).getInt("Y") == y){
				return (CompoundTag)tag;
			}
		}
		return null;
	}
	
	private byte readDataLayer(int x, int y, int z,String layer){
		byte data = 0;
		int localY = y % 16;
		CompoundTag section = getSection(y/16);
		if(section!=null){
			data = (byte) (new DataLayer(chunk.getByteArray(layer), 4)).get(x, localY, z);
		}
		
		return data;
	}

	
}
