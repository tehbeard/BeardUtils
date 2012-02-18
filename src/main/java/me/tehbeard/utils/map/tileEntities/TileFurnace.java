package me.tehbeard.utils.map.tileEntities;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;


@TileEntityType(id="Furnace")
public class TileFurnace extends TileEntity{

	ItemStack[] items = new ItemStack[3];
    short burn,cook;
	@Override
	public void setData(CompoundTag tag) {
		super.setData(tag);

		ListTag<CompoundTag> list = (ListTag<CompoundTag>) tag.getList("Items");
		for(int i = 0;i<list.size();i++){
			items[list.get(i).getByte("Slot")]=	makeItem(list.get(i));
		}
		burn = tag.getShort("BurnTime");
		cook = tag.getShort("CookTime");
	}

	private ItemStack makeItem(CompoundTag tag){
		short id = tag.getShort("id");
		short data = tag.getShort("Damage");
		byte count = tag.getByte("Count");
		ItemStack i = new ItemStack(id, count, data);
		//read the enchants
		if(tag.contains("tag")){
			if(tag.getCompound("tag").contains("ench")){
				for(int k = 0;k<tag.getCompound("tag").getList("ench").size();k++){
					i.addEnchantment(
							Enchantment.getById(
									((CompoundTag)tag.getCompound("tag").getList("ench").get(k)).getShort("id")), 
									((CompoundTag)tag.getCompound("tag").getList("ench").get(k)).getShort("lvl")		
							);
					;
				}
			}
		}
		return i;
	}
	@Override
	public void place(Location l) {

		BlockState state = l.getWorld().getBlockAt(l.clone().add(getX(), getY(), getZ())).getState();
		if(state instanceof Furnace){
			Furnace furnace = (Furnace)state;

			furnace.getInventory().setContents(items);
			furnace.update(true);
		}
	}

	@Override
	public String toString() {
		return "TileFurance [items=" + Arrays.toString(items) + "]";
	}
	


}
