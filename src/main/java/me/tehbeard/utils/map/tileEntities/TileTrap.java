package me.tehbeard.utils.map.tileEntities;

import java.util.Arrays;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;


@TileEntityType(id="Trap")
public class TileTrap extends TileEntity{

	ItemStack[] items = new ItemStack[9];

	@Override
	public void setData(CompoundTag tag) {
		super.setData(tag);

		ListTag<CompoundTag> list = (ListTag<CompoundTag>) tag.getList("Items");
		for(int i = 0;i<list.size();i++){
			items[list.get(i).getByte("Slot")]=	makeItem(list.get(i));
		}
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
		if(state instanceof Dispenser){
			Dispenser dispenser = (Dispenser)state;

			dispenser.getInventory().setContents(items);
			dispenser.getBlock().setData(dispenser.getBlock().getData());
			dispenser.update(true);
		}
	}

	@Override
	public String toString() {
		return "TileTrap [items=" + Arrays.toString(items) + "]";
	}
	


}
