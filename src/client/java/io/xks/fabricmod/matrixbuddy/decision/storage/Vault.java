package io.xks.fabricmod.matrixbuddy.decision.storage;

import io.xks.fabricmod.matrixbuddy.player.Backpack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * helps manage the inventory of a player, chest, enderchest and so on. Unite multiple physical storage device.
 * this is more like a imagination of items storage from the bot's perspective.
 */
public class Vault {

    public Vault() {
        this.content = new HashMap<>();
    }

    public Vault(PlayerInventory inventory){
        this();
        this.update(inventory);

    }

    /**
     * update the vault based on a player inventory.
     * @param inventory PlayerInventory
     */
    private void update(PlayerInventory inventory) {
        for (int i = Backpack.BackpackSlot.INVENTORY_1.id; i <= Backpack.BackpackSlot.INVENTORY_36.id ; i++) {
            ItemStack stack = inventory.getStack(i);

            // update the vault.
            Item item = stack.getItem();
            if (!content.containsKey(item)){
                content.put(item, new Entry(new ArrayList<>(), 0));
            }

            Entry entry = content.get(item);
            entry.quantity += stack.getCount();
            entry.locations.add(new ItemLocationDescriptor(Backpack.BackpackSlot.fromId(i), stack.getCount()));
        }
    }

    /**
     * describes the location of an item. can be inventory, enderchest, chest...
     * shulker boxes will be viewed as an extension for the types above.
     */
    public class ItemLocationDescriptor {
        public Backpack.BackpackSlot slot;
        public int quantity;

        public ItemLocationDescriptor(Backpack.BackpackSlot slot, int quantity) {
            this.slot = slot;
            this.quantity = quantity;
        }
    }

    class Entry {
        List<ItemLocationDescriptor> locations;
        int quantity;

        public Entry(List<ItemLocationDescriptor> locations, int quantity) {
            this.locations = locations;
            this.quantity = quantity;
        }
    }
    Map<Item, Entry> content;

    public boolean contains(Item item, int count){
        return (content.containsKey(item) ? content.get(item).quantity : 0 )>= count;
    }

    /**
     * this method is used to test whether the backpack contains adequate amount of items specified in the item parameter. call it before crafting something.
     * @param items 2d array containing items, most of the time the recipe.
     * @param batchSize multiplier for the quantity for each item.
     * @return whether backpack contains the items specified.
     */
    public boolean contains(Item[][] items, int batchSize){
        Map<Item, Integer> ingredientQuantity = new HashMap<>();
        for (Item[] row : items) {
            for (Item item : row) {
                ingredientQuantity.put(item, ingredientQuantity.getOrDefault(item, 0) + 1);
            }
        }

        for (Map.Entry<Item, Integer> entry : ingredientQuantity.entrySet()) {
            if (!contains(entry.getKey(), entry.getValue() * batchSize)){
                return false;
            }
        }
        return true;
    }

    /**
     * find available locations of the given item. prefer nearer containers (inventory, enderchest, chest...)
     * @param item item
     * @param quantity quantity
     * @return null if cannot find the required amount.
     */
    public List<Map.Entry<ItemLocationDescriptor,Integer>> locateItem(Item item, int quantity) {
        List<Map.Entry<ItemLocationDescriptor,Integer>> result = new ArrayList<>();
        Entry entry = content.get(item);

        if (quantity > entry.quantity){
            throw new IllegalArgumentException("Asking for too much!");
        }

        for (ItemLocationDescriptor location : entry.locations) {
            int amountToTake = Math.max(location.quantity, quantity);
            result.add(new AbstractMap.SimpleEntry<>(location, amountToTake));
            if (quantity == 0){
                break;
            }
        }

        return result;
    }
}
