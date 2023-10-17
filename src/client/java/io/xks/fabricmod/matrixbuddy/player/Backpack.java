package io.xks.fabricmod.matrixbuddy.player;

import io.xks.fabricmod.matrixbuddy.decision.storage.Recipe;
import io.xks.fabricmod.matrixbuddy.decision.storage.Vault;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.SlotActionType;

import java.util.*;

public class Backpack implements Inventory {
    public enum BackpackSlot {
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
        INVENTORY_36(44),

        OFFHAND(45);


        public final int id;
        BackpackSlot(int id) {
            this.id = id;
        }

        public static BackpackSlot fromClickSlotId(int id){
            if (id > 44 || id < 0){
                throw new IllegalArgumentException("No enum constant BackPackSlot with id " + id);
            }

            return BackpackSlot.values()[id];
        }

        public static BackpackSlot fromPlayerInventoryId(int id){
            if (0<= id && id <= 8) { // hotbar
                return BackpackSlot.values()[id + 36];
            } else if (9 <= id && id <= 35) { //inv
                return  BackpackSlot.values()[(id - 9) + 9];
            } else if (36 <= id && id <= 39) { //armor
                return BackpackSlot.values()[8 - (id - 36)];
            } else if (id == 40) {//offhand
                return OFFHAND;
            } else {
                throw new IllegalArgumentException("id should be within the range [0, 40]!");
            }
        }

    }

    PlayerInventory inventory;
    Vault vault;
    public Backpack(PlayerInventory inventory){
        this.inventory = inventory;
        this.vault = new Vault(inventory);
    }


    private void pickupItem(BackpackSlot slot, int quantity){
        int stackQuantity = inventory.getStack(slot.id).getCount();

        if (quantity == stackQuantity){
            clickSlot(slot, SlotActionType.PICKUP, 0);
        } else {
            clickSlot(slot, SlotActionType.PICKUP, 0);
            for (int i = 0; i < quantity - stackQuantity; i++) {
                clickSlot(slot, SlotActionType.PICKUP, 1);
            }
        }
    }

    /**
     * underlying method that clicks the slot.
     * @param slot slot
     * @param actionType actionType
     * @param button button. 0 for left-click, 1 for right-click. I don't know the rest.
     */
    public void clickSlot(BackpackSlot slot, SlotActionType actionType, int button){
        MinecraftClient client = MinecraftClient.getInstance();

        assert client.player != null;
        int syncId = client.player.currentScreenHandler.syncId;
        assert client.interactionManager != null;
        client.interactionManager.clickSlot(syncId, slot.id, button, actionType, client.player);
    }

    public void craft(Recipe recipe, int quantity){
        Map<Item, Integer> ingredientsWithQuantities = recipe.getIngredientsWithQuantities();

        // multiply the map by quantity.
        Iterator<Map.Entry<Item, Integer>> iterator = ingredientsWithQuantities.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Item, Integer> entry = iterator.next();
            entry.setValue(entry.getValue() * quantity);
        }

        if (!vault.contains(ingredientsWithQuantities)) {
            throw new IllegalStateException("you don't have the adequate amount of items!");
        }

        //split the quantity into batches. a batchSize refers to the quantity of input items.
        int fullStacks = quantity % 64;
        int remainingItems = quantity / 64;
        int[] batches = new int[fullStacks + remainingItems == 0 ? 0 :1];
        for (int i = 0; i < fullStacks; i++) {
            batches[i] = 64;
        }
        if (remainingItems != 0){
            batches[fullStacks] = remainingItems;
        }

        //figuring out where each ingredient is located.
        Map<Item, List<Map.Entry<Vault.ItemLocationDescriptor, Integer>>> ingredientLocations = new HashMap<>(4);
        ingredientsWithQuantities.forEach((ingredient, ingredientQuantity) -> ingredientLocations.put(ingredient, vault.retrieve(ingredient, quantity)));

        //craft in batches.
        for (int batchSize : batches) {
            craftSingleBatch(recipe, batchSize, ingredientLocations);
        }
    }

    /**
     * This method is for crafting a single batch of items. The reason why we crafting in batches is that the quantity of input stacks can only be at most 64. you should use craft() most of the time for convenience.
     *
     * @param recipe              the recipe
     * @param quantity            int between 1 and 64
     * @param locations this will be seen as a temporary object, will be modified after executing this method.
     */
    public void craftSingleBatch(Recipe recipe, int quantity, Map<Item, List<Map.Entry<Vault.ItemLocationDescriptor, Integer>>> locations){
        if (quantity > 64 || quantity < 1){
            throw new IllegalArgumentException("the quantity of input stacks can only be at most 64 per batch");
        }

        //for each slot in the crafting table
        for (int rowIndex = 0; rowIndex < recipe.inputs.length; rowIndex++) {
            for (int colIndex = 0; colIndex < recipe.inputs.length; colIndex++) {
                int recipePosition = rowIndex + colIndex + 1;
                Item item = recipe.inputs[rowIndex][colIndex];

                int remaining = quantity;
                // pick up item from each location and put them in the crafting table
                for (Map.Entry<Vault.ItemLocationDescriptor, Integer> locationAndCount : locations.get(item)) {
                    //only wants the items in inventory
                    if (locationAndCount.getKey().device != Vault.ItemLocationDescriptor.Device.INVENTORY){
                        continue;
                    }

                    Vault.InventoryDescriptor locationDescriptor = ((Vault.InventoryDescriptor) locationAndCount.getKey());
                    //when the item doesn't match what we have thought, throw an error
                    assert locationDescriptor.quantity == inventory.getStack(locationDescriptor.slot.id).getCount();

                    pickupItem(locationDescriptor.slot, locationAndCount.getValue());
                    clickSlot(BackpackSlot.valueOf("CRAFT_INPUT_" + recipePosition), SlotActionType.PICKUP, 1);

                    remaining -= locationDescriptor.quantity;
                    if (remaining == 0){
                        break;
                    }

                    vault.update(inventory);
                }


            }
        }
    }
}
