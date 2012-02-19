package me.tehbeard.utils.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

}
