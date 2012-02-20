package me.tehbeard.utils.map;

import java.io.File;

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
