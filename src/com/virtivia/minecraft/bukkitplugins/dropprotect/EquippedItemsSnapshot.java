package com.virtivia.minecraft.bukkitplugins.dropprotect;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class EquippedItemsSnapshot {
	private final int NUM_SLOTS = 9;
	
	private ItemStack helmet, chestplate, leggings, boots;
	private ItemStack[] itemsInSlots;
	
	public EquippedItemsSnapshot(Player player) {
		PlayerInventory inventory = player.getInventory();
		
		helmet = inventory.getHelmet();
		chestplate = inventory.getChestplate();
		leggings = inventory.getLeggings();
		boots = inventory.getBoots();
		
		itemsInSlots = new ItemStack[NUM_SLOTS];
		
		for (int i = 0; i < NUM_SLOTS; i++) {
			itemsInSlots[i] = inventory.getItem(i);
		}
	}
	
	public void mergeToPlayerInventory(Player player) {
		PlayerInventory inventory = player.getInventory();
		
		inventory.setHelmet(helmet);
		inventory.setChestplate(chestplate);
		inventory.setLeggings(leggings);
		inventory.setBoots(boots);
		
		for (int i = 0; i < NUM_SLOTS; i++) {
			inventory.setItem(i, itemsInSlots[i]);
		}
	}
	
	public Set<ItemStack> toSet() {
		Set<ItemStack> set = new HashSet<ItemStack>();
		
		addIfNotNull(set, helmet);
		addIfNotNull(set, chestplate);
		addIfNotNull(set, leggings);
		addIfNotNull(set, boots);
		
		for (int i = 0; i < NUM_SLOTS; i++) {
			addIfNotNull(set, itemsInSlots[i]);
		}
		
		return set;
	}
	
	private void addIfNotNull(Collection<ItemStack> collection, ItemStack item) {
		if (item != null) {
			collection.add(item);
		}
	}
	
	public void filterItemStackList(List<ItemStack> list) {
		List<ItemStack> filteredList = new LinkedList<ItemStack>();
		
		Set<ItemStack> remainingEquippedItems = this.toSet();
		
		for (ItemStack itemStack : list) {
			if (remainingEquippedItems.contains(itemStack)) {
				remainingEquippedItems.remove(itemStack);
			} else {
				filteredList.add(itemStack);
			}
		}
		
		// remainingEquippedItems should now be empty
		
		list.clear();
		list.addAll(filteredList);
	}
}