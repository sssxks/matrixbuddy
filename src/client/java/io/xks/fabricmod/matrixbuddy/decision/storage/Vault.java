package io.xks.fabricmod.matrixbuddy.decision.storage;

import io.xks.fabricmod.matrixbuddy.player.Backpack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * helps manage the inventory of a player, chest, enderchest and so on. Combine multiple physical storage device.
 * this is more like an imagination of items storage from the bot's perspective, so it doesn't update itself to avoid hallucination and out of sync. Update via update().
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
     * Update the vault based on a player inventory.
     * @param inventory PlayerInventory
     */
    public void update(PlayerInventory inventory) {
        //TODO: Considering reading from PlayerInventory real-time. But other devices still need manual update.

        //clear inventory items off the vault.
        content.forEach((item, entry) -> {
            Iterator<ItemLocationDescriptor> iterator = entry.locations.iterator();
            while (iterator.hasNext()) {
                ItemLocationDescriptor location = iterator.next();
                if (location.device == ItemLocationDescriptor.Device.INVENTORY) {
                    iterator.remove();
                }
            }
        });

        //iterate over the inventory 36+4(armor)+1(offhand) slots.
        for (int i = 0; i <= 35 ; i++) {
            ItemStack stack = inventory.getStack(i);

            // update the vault.
            Item item = stack.getItem();
            if (!content.containsKey(item)){
                content.put(item, new Entry(new LinkedList<>(), 0));
            }

            Entry entry = content.get(item);
            entry.quantity += stack.getCount();

            InventoryDescriptor locationDescriptor = new InventoryDescriptor(stack.getCount(), Backpack.BackpackSlot.fromPlayerInventoryId(i));
            entry.locations.add(locationDescriptor);
        }
    }

    /**
     * describes the location of an item. can be inventory, enderchest, chest...
     * shulker boxes will be viewed as an extension for the types above.
     */
    public class ItemLocationDescriptor {
        public enum Device{
            INVENTORY,
            ENDERCHEST,
            CHEST
        }

        public final Device device;
        public final int quantity;
        public boolean isShulker;
//        int ShulkerSlot; // the slot in the shulker box
        public ItemLocationDescriptor(Device device, int quantity){
            this.device = device;
            this.quantity = quantity;
        }
    }

    public class InventoryDescriptor extends ItemLocationDescriptor{
        public final Backpack.BackpackSlot slot;

        public InventoryDescriptor(int quantity, Backpack.BackpackSlot slot) {
            super(Device.INVENTORY, quantity);
            this.slot = slot;
        }
    }

    public class EnderChestDescriptor extends ItemLocationDescriptor{
//        int slot

        public EnderChestDescriptor(int quantity) {
            super(Device.ENDERCHEST, quantity);
        }
    }

    public class ChestDescriptor extends ItemLocationDescriptor{
        public ChestDescriptor(int quantity) {
            super(Device.CHEST, quantity);
        }
//        location
//        isLarge
//        slot
    }

    class Entry {
        final LinkedList<ItemLocationDescriptor> locations;
        int quantity;

        public Entry(LinkedList<ItemLocationDescriptor> locations, int quantity) {
            this.locations = locations;
            this.quantity = quantity;
        }
    }
    Map<Item, Entry> content;

    public boolean contains(Item item, int count){
        return (content.containsKey(item) ? content.get(item).quantity : 0 )>= count;
    }

    /**
     * this method is used to test whether the backpack contains adequate amount of items specified in the items parameter. call it before crafting something.
     * @param items items, most of the time the recipe.
     * @return whether backpack contains the items specified.
     */
    public boolean contains(Map<Item, Integer> items){
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            if (!contains(entry.getKey(), entry.getValue())){
                return false;
            }
        }
        return true;
    }

    /**
     * find available locations of the given item. prefer nearer containers (inventory, enderchest, chest...). no actual update on the internal Mao. call update() after you have finished work with the inventory.
     * @param item item
     * @param quantity quantity
     * @return the locations of the item
     */
    public List<Map.Entry<ItemLocationDescriptor,Integer>> retrieve(Item item, int quantity) {
        List<Map.Entry<ItemLocationDescriptor,Integer>> result = new ArrayList<>();
        Entry entry = content.get(item);

        if (quantity > entry.quantity){
            throw new IllegalArgumentException("Asking for too much!");
        }

        for (ItemLocationDescriptor location : entry.locations) {
            int amountToTake = Math.min(location.quantity, quantity);
            result.add(new AbstractMap.SimpleEntry<>(location, amountToTake));
            quantity -= amountToTake;
            if (quantity == 0){
                break;
            }
        }

        return result;
    }
}
