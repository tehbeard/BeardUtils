package me.tehbeard.utils.map.tileEntities;

import me.tehbeard.utils.factory.ConfigurableFactory;

public class TileEntityFactory {

	private static ConfigurableFactory<TileEntity, TileEntityType> tileEntityFactory;
	
	static{
		tileEntityFactory = new ConfigurableFactory<TileEntity, TileEntityType>(TileEntityType.class) {

			@Override
			public String getTag(TileEntityType annotation) {
				return annotation.id();
			}
		};
		tileEntityFactory.addProduct(TileSign.class);
		tileEntityFactory.addProduct(TileTrap.class);
		tileEntityFactory.addProduct(TileChest.class);
		tileEntityFactory.addProduct(TileFurnace.class);
		tileEntityFactory.addProduct(TileRecordPlayer.class);
		tileEntityFactory.addProduct(TileNoteBlock.class);
		tileEntityFactory.addProduct(TileSpawner.class);
		tileEntityFactory.addProduct(TileCauldron.class);
	}
	
	public static ConfigurableFactory<TileEntity, TileEntityType> getInstance(){
		
		return tileEntityFactory;
	}
}
