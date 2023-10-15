package io.xks.fabricmod.matrixbuddy.decision.storage;

import io.xks.fabricmod.matrixbuddy.player.Backpack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * helps manage the inventory of a player, chest, enderchest and so on. Unite multiple physical storage device.
 * this is more like a imagination of items storage from the bot's perspective, so it doesn't update itself to avoid hallucination and out of sync. Update via update().
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
    public void update(PlayerInventory inventory) {
        for (int i = Backpack.BackpackSlot.INVENTORY_1.id; i <= Backpack.BackpackSlot.INVENTORY_36.id ; i++) {
            ItemStack stack = inventory.getStack(i);

            // update the vault.
            Item item = stack.getItem();
            if (!content.containsKey(item)){
                content.put(item, new Entry(new ArrayList<>(), 0));
            }

            Entry entry = content.get(item);
            entry.quantity += stack.getCount();

            ItemLocationDescriptor locationDescriptor = new ItemLocationDescriptor();
            locationDescriptor.setProperty("type", "inventory");
            locationDescriptor.setProperty("slot", Backpack.BackpackSlot.fromId(i));
            locationDescriptor.setProperty("quantity", stack.getCount());
            entry.locations.add(locationDescriptor);
        }
    }

    /**
     * describes the location of an item. can be inventory, enderchest, chest...
     * shulker boxes will be viewed as an extension for the types above.
     * <p>
     * {
     *     "type": "inventory",
     *     "quantity": int,
     *     "slot": Backpack.BackpackSlot
     * }
     * <p>
     * {
     *     "type": "enderchest",
     *     "quantity": int,
     *     "slot": ??EnderChestSlot
     * }
     * <p>
     * {
     *     "type": "chest",
     *     "quantity": int,
     *     "location": ????(x,y),
     *     "isLarge": boolean,
     *     "slot": ????
     * }
     */
    public class ItemLocationDescriptor {
        private Map<String, Object> properties;

        public ItemLocationDescriptor() {
            this.properties = new HashMap<>(5);
        }

        public Object getProperty(String key) {
            return properties.get(key);
        }

        public void setProperty(String key, Object value) {
            properties.put(key, value);
        }

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
     * this method is used to test whether the backpack contains adequate amount of items specified in the items parameter. call it before crafting something.
     * @param items items, most of the time the recipe.
     * @param batchSize multiplier for the quantity for each item.
     * @return whether backpack contains the items specified.
     */
    public boolean contains(Map<Item, Integer> items, int batchSize){
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            if (!contains(entry.getKey(), entry.getValue() * batchSize)){
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
            int amountToTake = Math.max(location.quantity, quantity);
            result.add(new AbstractMap.SimpleEntry<>(location, amountToTake));
            if (quantity == 0){
                break;
            }
        }

        return result;
    }
}
