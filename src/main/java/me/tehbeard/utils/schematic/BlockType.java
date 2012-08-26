package me.tehbeard.utils.schematic;

import java.util.EnumSet;

public enum BlockType{
	//blocks that rely on other blocks being there
	REDSTONE_WIRE(55,1),
	SAPLING(6,1),
	BED(26,1),
	POWERED_RAIL(27,1),
	DETECTOR_RAIL(28,1),
	LONG_GRASS(31,1),
	DEAD_BUSH(32,1),
	YELLOW_FLOWER(37,1),
	RED_FLOWER(38,1),
	BROWN_MUSHROOM(39,1),
	RED_MUSHROOM(40,1),
	TORCH(50,1),
	FIRE(51,1),
	CROPS(59,1),
	LADDER(65,1),
	MINECART_TRACKS(66,1),
	LEVER(69,1),
	STONE_PRESSURE_PLATE(70,1),
	WOODEN_PRESSURE_PLATE(72,1),
	REDSTONE_TORCH_OFF(75,1),
	REDSTONE_TORCH_ON(76,1),
	STONE_BUTTON(77,1),
	SNOW(78,1),
	PORTAL(90,1),
	REDSTONE_REPEATER_OFF(93,1),
	REDSTONE_REPEATER_ON(94,1),
	TRAP_DOOR(96,1),
	VINE(106,1),
	LILY_PAD(111,1),
	NETHER_WART(115,1),
	PISTON_BASE(29,1),
	PISTON_STICKY_BASE(33,1),
	TRIPWIRE_HOOK(131,1),
	TRIPWIRE(132,1),
	
	//blocks affected by redstone/need blocks above to be there
	PISTON_EXTENSION(34,2),
	PISTON_MOVING_PIECE(36,2),
	SIGN_POST(63,2),
	WOODEN_DOOR(64,2),
	WALL_SIGN(68,2),
	IRON_DOOR(71,2),
	CACTUS(81,2),
	REED(83,2),
	CAKE_BLOCK(92,2),
	DISPENSER(23,2);
	
	public final int typeId;
	public final int order;

	BlockType(int id,int order){
		this.typeId=id;
		this.order = order;
	}

	public static int getOrder(int typeId){
		for(BlockType type : EnumSet.allOf(BlockType.class)){
			if(type.typeId == typeId){
				return type.order;
			}
		}
		return 0;
	}

}