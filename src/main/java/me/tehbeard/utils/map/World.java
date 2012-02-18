package me.tehbeard.utils.map;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;
import com.mojang.nbt.NbtIo;

/**
 * Represents a world folder, and allows manipulation of it.
 * @author James
 *
 */
public class World {

	private final RegionFileCache fileCache;
	private final File mapFolder;
	public World(File mapFolder){
		this.mapFolder = mapFolder;
		fileCache = new RegionFileCache();
	}
	public int getBlockIdAt(int x,int y,int z){
		int chunkX = (int) Math.floor((double)x /16);
		int chunkZ = (int) Math.floor((double)z /16);

		return getChunk(chunkX,chunkZ).getBlockTypeId(x % 16, y,z % 16);
	}


	public Chunk getChunk(int x,int z) {
		DataInputStream stream = fileCache.getChunkDataInputStream(mapFolder, x, z);
		CompoundTag chunk = null;
		if(stream!=null){
			try {
				chunk = NbtIo.read(stream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(chunk == null)
		{
			chunk = new CompoundTag("Level");
			chunk.putByteArray("HeightMap", new byte[256]);
			chunk.put("Entities", new ListTag<CompoundTag>("Entities"));
			chunk.put("TileEntities", new ListTag<CompoundTag>("TileEntities"));
			chunk.putCompound("Sections", new CompoundTag("Sections"));
			chunk.putInt("xPos",x);
			chunk.putInt("zPos",z);
		}

		return new Chunk(chunk);


	}

}
