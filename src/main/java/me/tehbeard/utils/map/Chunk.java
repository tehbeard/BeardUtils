package me.tehbeard.utils.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import me.tehbeard.utils.map.tileEntities.TileEntity;
import me.tehbeard.utils.map.tileEntities.TileEntityFactory;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.NbtIo;


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
	int[] HeightMap;
	Section[] sections;
	List<TileEntity> tileEntities;


	public Chunk(CompoundTag chunk){
		xPos = chunk.getInt("xPos");
		zPos = chunk.getInt("zPos");
		Biomes = chunk.getByteArray("Biomes").length > 0 ? chunk.getByteArray("Biomes") : new byte[256];
		HeightMap = chunk.getIntArray("HeightMap").length > 0 ? chunk.getIntArray("HeightMap") : new int[256];

		chunk.getList("Entities").print(System.out);
		//load sections
		ListTag<CompoundTag> sectionsTag = (ListTag<CompoundTag>) chunk.getList("Sections");
		sections = new Section[16];
		for(CompoundTag section: sectionsTag){
			sections[section.getByte("Y")] = new Section(section);
		}

		//load tile entities
		tileEntities = new ArrayList<TileEntity>();
		ListTag<CompoundTag> TileEntitytag = (ListTag<CompoundTag>) chunk.getList("TileEntities");
		for(CompoundTag tileEntity : TileEntitytag){
			TileEntity t = TileEntityFactory.getInstance().getProduct(tileEntity.getString("id"));
			if(t!=null){
				t.setData(tileEntity);
				//System.out.println(t.toString());
				tileEntities.add(t);
			}
			else
			{
				System.out.println("Could not load entity " + tileEntity.getString("id"));
			}
		}

		
		//load Actual Entities
		//TODO
	}


	public int getX(){
		return xPos;
	}

	public int getZ(){
		return zPos;
	}

	public int getBlockId(int x,int y,int z){
		int lx = x - (xPos * 16);
		int lz = z - (zPos * 16);
		if(
				((lx >=0 && lx<16)&&
						(y >=0 && y<256)&&
						(lz >=0 && lz<16)) == false
				){
			throw new IllegalArgumentException("Out of Bounds" + lx + ","+ y + ","+ lz);
		}
		if(sections[(int) Math.floor(y/16)]!=null){
			return sections[(int) Math.floor(y/16)].getBlockId(lx, y%16, lz);
		}
		return 0;
	}

	public void setBlockId(int x,int y,int z,int id){
		int lx = x - (xPos * 16);
		int lz = z - (zPos * 16);
		if(
				((lx >=0 && lx<16)&&
						(y >=0 && y<256)&&
						(lz >=0 && lz<16)) == false
				){
			throw new IllegalArgumentException("Out of Bounds" + lx + ","+ y + ","+ lz);
		}

		if(sections[(int) Math.floor(y/16)]==null){
			sections[(int) Math.floor(y/16)] = new Section((int) Math.floor(y/16));
		}

		sections[(int) Math.floor(y/16)].setBlockId(lx, y%16, lz,id);
	}

	public int getBlockData(int x,int y,int z){
		int lx = x - (xPos * 16);
		int lz = z - (zPos * 16);
		if(
				((lx >=0 && lx<16)&&
						(y >=0 && y<256)&&
						(lz >=0 && lz<16)) == false
				){
			throw new IllegalArgumentException("Out of Bounds" + lx + ","+ y + ","+ lz);
		}
		if(sections[(int) Math.floor(y/16)]!=null){
			return sections[(int) Math.floor(y/16)].getData(lx, y%16, lz);
		}
		return 0;
	}

	public void setBlockData(int x,int y,int z,byte data){
		int lx = x - (xPos * 16);
		int lz = z - (zPos * 16);
		if(
				((lx >=0 && lx<16)&&
						(y >=0 && y<256)&&
						(lz >=0 && lz<16)) == false
				){
			throw new IllegalArgumentException("Out of Bounds" + lx + ","+ y + ","+ lz);
		}
		if(sections[(int) Math.floor(y/16)]!=null){
			sections[(int) Math.floor(y/16)].setData(lx, y%16, lz,data);
		}
	}

	public int getHeighestBlock(int x,int z){
		int lx = x - (xPos * 16);
		int lz = z - (zPos * 16);
		if(
				((lx >=0 && lx<16)&&
						(lz >=0 && lz<16)) == false
				){
			throw new IllegalArgumentException("Out of Bounds" + lx + ","+ lz);
		}
		return HeightMap[lz << 4 | lx];
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

		Section(int Y){
			this.Y = (byte) Y;
			Blocks     = new byte[4096];
			AddBlocks  = new DataLayer(new byte[2048],4);
			Data       = new DataLayer(new byte[2048],4);
			SkyLight   = new DataLayer(new byte[2048],4);
			BlockLight = new DataLayer(new byte[2048],4);
			SkyLight.setAll(15);
		}

		Section(CompoundTag tag){

			Y = tag.getByte("Y");
			Blocks     = tag.getByteArray("Blocks");
			AddBlocks  = getLayer(tag.getByteArray("AddBlocks") );
			Data       = getLayer(tag.getByteArray("Data")      );
			SkyLight   = getLayer(tag.getByteArray("SkyLight")  );
			BlockLight = getLayer(tag.getByteArray("BlockLight"));
		}

		public int getY(){
			return Y;
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

		public CompoundTag getTag(){
			CompoundTag tag = new CompoundTag();
			tag.putByte("Y",Y);
			tag.putByteArray("Blocks",Blocks);
			tag.putByteArray("AddBlocks",AddBlocks.data); 
			tag.putByteArray("Data",Data.data);
			tag.putByteArray("SkyLight",SkyLight.data);
			tag.putByteArray("BlockLight",BlockLight.data);
			return tag;
		}

	}

	public void write(DataOutputStream os) throws IOException{

		CompoundTag level = new CompoundTag("Level");
		level.putInt("xPos",xPos);
		level.putInt("zPos",zPos);
		level.putByteArray("Biomes",Biomes);
		level.putIntArray("HeightMap",HeightMap);

		ListTag<CompoundTag> sectionsTag = new ListTag<CompoundTag>();
		for(Section s :sections){
			if(s!=null){
					sectionsTag.add(s.getTag());
			}
		}
		level.put("Sections",sectionsTag);

		//wrap and write
		CompoundTag out = new CompoundTag();
		out.putCompound("Level", level);
		out.print(System.out);
		if(os!=null){
			NbtIo.write(out, os);
		}
	}

	public static void main(String[] args) throws IOException{
		RegionFileCache cache = new RegionFileCache();
		int x = 129;
		int z = 271;
		int chunkX = (int)Math.floor((double)x / 16);
		int chunkZ = (int)Math.floor((double)z / 16);
		//RegionFile region = new RegionFile(new File("C:\\Documents and Settings\\James\\Desktop\\1.8\\.minecraft\\saves\\New World\\region\\r.-1.0.mca"));
		//DataInputStream in = cache.getChunkDataInputStream(new File("C:\\Documents and Settings\\James\\Desktop\\1.8\\.minecraft\\saves\\New World"), chunkX, chunkZ);
		//DataInputStream in = cache.getChunkDataInputStream(new File("C:\\Documents and Settings\\James\\Desktop\\1.8\\.minecraft\\saves\\Shape Sea"), chunkX, chunkZ);
		
		DataInputStream in = cache.getChunkDataInputStream(new File("C:\\Documents and Settings\\James\\Desktop\\1.8\\.minecraft\\saves\\New World-"), chunkX, chunkZ);
		
		CompoundTag lvl = NbtIo.read(in).getCompound("Level");
		//((CompoundTag)lvl.getList("Sections").get(15)).print(System.out);
		//printArray(((CompoundTag)lvl.getList("Sections").get(15)).getByteArray("SkyLight"),16);
		
		Chunk c = new Chunk(lvl);
		in.close();

		//printArray(c.sections[15].Blocks,16);
		for(int y =0 ;y<255;y++){
		c.setBlockId(x, y,z, 22);
		c.sections[y/16].SkyLight.set(x % 16, y%16, z %16, 2);
		}
		//c.write(null);

		//printArray(c.HeightMap,16);
		
		
		
		DataOutputStream out = cache.getChunkDataOutputStream(new File("C:\\Documents and Settings\\James\\Desktop\\1.8\\.minecraft\\saves\\New World-"), chunkX, chunkZ);
		if(out!=null){
			c.write(out);
			out.close();
			cache.clear();
			System.out.println("Save attempted");
		}
		else
		{
			System.out.println("Outfile not found");
		}/**/


	}

	private static void printArray(byte[] arr,int line){
		for(int i = 0;i<arr.length;i++){
			System.out.print(arr[i]);
			if(i % line == line-1){
				System.out.println();
			}
			else
			{
				System.out.print(",");
				if(i % line*line == line*line-1){
					System.out.println();
				}
			}
			
		}
	}

}
