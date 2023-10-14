package io.xks.fabricmod.matrixbuddy.player;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Backpack implements Inventory {
    enum BackpackSlot {
        CRAFT_OUTPUT(0),
        CRAFT_INPUT_1(1),
        CRAFT_INPUT_2(2),
        CRAFT_INPUT_3(3),
        CRAFT_INPUT_4(4),
        ARMOR_HELMET(5),
        ARMOR_CHESTPLATE(6),
        ARMOR_LEGGINGS(7),
        ARMOR_BOOTS(8),
        INVENTORY_1(9),
        INVENTORY_2(10),
        INVENTORY_3(11),
        INVENTORY_4(12),
        INVENTORY_5(13),
        INVENTORY_6(14),
        INVENTORY_7(15),
        INVENTORY_8(16),
        INVENTORY_9(17),
        INVENTORY_10(18),
        INVENTORY_11(19),
        INVENTORY_12(20),
        INVENTORY_13(21),
        INVENTORY_14(22),
        INVENTORY_15(23),
        INVENTORY_16(24),
        INVENTORY_17(25),
        INVENTORY_18(26),
        INVENTORY_19(27),
        INVENTORY_20(28),
        INVENTORY_21(29),
        INVENTORY_22(30),
        INVENTORY_23(31),
        INVENTORY_24(32),
        INVENTORY_25(33),
        INVENTORY_26(34),
        INVENTORY_27(35),
        INVENTORY_28(36),
        INVENTORY_29(37),
        INVENTORY_30(38),
        INVENTORY_31(39),
        INVENTORY_32(40),
        INVENTORY_33(41),
        INVENTORY_34(42),
        INVENTORY_35(43),
        INVENTORY_36(44);

        public final int id;

        BackpackSlot(int id) {
            this.id = id;
        }

        public static BackpackSlot fromId(int id){
            for (BackpackSlot slot : BackpackSlot.values()) {
                if (slot.id == id){
                    return slot;
                }
            }
            throw new IllegalArgumentException("No enum constant BackPackSlot with id " + id);

        }
    }

    PlayerInventory inventory;
    Map<Item, Map.Entry<Integer, LinkedList<BackpackSlot>>> vault;
    public Backpack(PlayerInventory inventory){
        this.inventory = inventory;

        for (int i = BackpackSlot.INVENTORY_1.id; i <= BackpackSlot.INVENTORY_36.id ; i++) {
            ItemStack stack = inventory.getStack(i);

            // update the vault count and slot id list.
            Map.Entry<Integer, LinkedList<BackpackSlot>> oldValue = vault.getOrDefault(stack.getItem(), new AbstractMap.SimpleEntry<>(0, new LinkedList<>()));
            int newCount = oldValue.getKey() + stack.getCount();
            oldValue.getValue().add(BackpackSlot.fromId(i)); //TODO: unnecessary O(n^2) time complexity
            vault.put(stack.getItem(), new AbstractMap.SimpleEntry<>(newCount, oldValue.getValue()));
        }
    }

    public boolean contains(Item item, int count){
        return (vault.containsKey(item) ? vault.get(item).getKey() : 0 )>= count;
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

    private void clickSlot(BackpackSlot slot, SlotActionType actionType, int button){
        MinecraftClient client = MinecraftClient.getInstance();

        assert client.player != null;
        int syncId = client.player.currentScreenHandler.syncId;
        assert client.interactionManager != null;
        client.interactionManager.clickSlot(syncId, slot.id, button, actionType, client.player);
    }
    public void craft(Item[][] recipe, int batchSize){
        if (!contains(recipe, batchSize)) {
            throw new IllegalStateException("you don't have the adequate amount of items!");
        }


        for (int rowIndex = 0; rowIndex < recipe.length; rowIndex++) {
            for (int colIndex = 0; colIndex < recipe[rowIndex].length; colIndex++) {
                int recipePosition = rowIndex + colIndex;

                BackpackSlot slot = vault.get(recipe[rowIndex][colIndex]).getValue().pop();


                clickSlot(slot, SlotActionType.PICKUP, 0);
                for (int i = 0; i < batchSize; i++) {
                    clickSlot(BackpackSlot.valueOf("CRAFT_" + recipePosition), SlotActionType.PICKUP, 1);
                }
            }
        }
    }
}
