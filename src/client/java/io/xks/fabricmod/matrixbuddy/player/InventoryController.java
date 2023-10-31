package io.xks.fabricmod.matrixbuddy.player;

import io.xks.fabricmod.matrixbuddy.agent.storage.Recipe;
import io.xks.fabricmod.matrixbuddy.agent.storage.Vault;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InventoryController implements Inventory {
    public enum InventorySlot {
        CRAFT_OUTPUT(0, -1),//TODO: refactor this later
        CRAFT_INPUT_1(1, -1),
        CRAFT_INPUT_2(2, -1),
        CRAFT_INPUT_3(3, -1),
        CRAFT_INPUT_4(4, -1),
        ARMOR_HELMET(5, 39),
        ARMOR_CHESTPLATE(6, 38),
        ARMOR_LEGGINGS(7, 37),
        ARMOR_BOOTS(8, 36),
        INVENTORY_1(9, 9),
        INVENTORY_2(10, 10),
        INVENTORY_3(11, 11),
        INVENTORY_4(12, 12),
        INVENTORY_5(13, 13),
        INVENTORY_6(14, 14),
        INVENTORY_7(15, 15),
        INVENTORY_8(16, 16),
        INVENTORY_9(17, 17),
        INVENTORY_10(18, 18),
        INVENTORY_11(19, 19),
        INVENTORY_12(20, 20),
        INVENTORY_13(21, 21),
        INVENTORY_14(22, 22),
        INVENTORY_15(23, 23),
        INVENTORY_16(24, 24),
        INVENTORY_17(25, 25),
        INVENTORY_18(26, 26),
        INVENTORY_19(27, 27),
        INVENTORY_20(28, 28),
        INVENTORY_21(29, 29),
        INVENTORY_22(30, 30),
        INVENTORY_23(31, 31),
        INVENTORY_24(32, 32),
        INVENTORY_25(33, 33),
        INVENTORY_26(34, 34),
        INVENTORY_27(35, 35),
        INVENTORY_28(36, 0),
        INVENTORY_29(37, 1),
        INVENTORY_30(38, 2),
        INVENTORY_31(39, 3),
        INVENTORY_32(40, 4),
        INVENTORY_33(41, 5),
        INVENTORY_34(42, 6),
        INVENTORY_35(43, 7),
        INVENTORY_36(44, 8),
        OFFHAND(45, 40);


        public final int clickSlotId;
        public final int playerInventoryId;
        InventorySlot(int clickSlotId, int playerInventoryId) {
            this.clickSlotId = clickSlotId;
            this.playerInventoryId = playerInventoryId;
        }

        public static InventorySlot fromClickSlotId(int id){
            if (id > 44 || id < 0){
                throw new IllegalArgumentException("No enum constant BackPackSlot with clickSlotId " + id);
            }

            return InventorySlot.values()[id];
        }

        public static InventorySlot fromPlayerInventoryId(int id){
            if (0<= id && id <= 8) { // hot-bar
                return InventorySlot.values()[id + 36];
            } else if (9 <= id && id <= 35) { //inv
                return  InventorySlot.values()[(id - 9) + 9];
            } else if (36 <= id && id <= 39) { //armor
                return InventorySlot.values()[8 - (id - 36)];
            } else if (id == 40) {//offhand
                return OFFHAND;
            } else {
                throw new IllegalArgumentException("clickSlotId should be within the range [0, 40]!");
            }
        }

    }

    PlayerInventory inventory;
    Vault vault;
    public InventoryController(PlayerInventory inventory){
        this.inventory = inventory;
        this.vault = new Vault(inventory);
    }


    private void pickupItem(@NotNull InventoryController.InventorySlot slot, int quantity){
        int stackQuantity = inventory.getStack(slot.playerInventoryId).getCount();

        if (quantity == stackQuantity){
            clickSlot(slot, SlotActionType.PICKUP, 0);
        } else {
            clickSlot(slot, SlotActionType.PICKUP, 0);
            for (int i = 0; i < stackQuantity - quantity; i++) {
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
    public void clickSlot(@NotNull InventoryController.InventorySlot slot, SlotActionType actionType, int button){
        MinecraftClient client = MinecraftClient.getInstance();

        assert client.player != null;
        int syncId = client.player.currentScreenHandler.syncId;
        assert client.interactionManager != null;
        client.interactionManager.clickSlot(syncId, slot.clickSlotId, button, actionType, client.player);
    }

    public void craft(@NotNull Recipe recipe, int quantity){
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
        int fullStacks = quantity / 64;
        int remainingItems = quantity % 64;
        int[] batches = new int[fullStacks + (remainingItems == 0 ? 0 :1)];
        for (int i = 0; i < fullStacks; i++) {
            batches[i] = 64;
        }
        if (remainingItems != 0){
            batches[fullStacks] = remainingItems;
        }

        //craft in batches.
        for (int batchSize : batches) {
            //figuring out where each ingredient is located for this single batch
            Map<Item, List<Map.Entry<Vault.ItemLocationDescriptor, Integer>>> ingredientLocations = new HashMap<>(4);
            ingredientsWithQuantities.forEach((ingredient, ingredientQuantity) -> ingredientLocations.put(ingredient, vault.retrieve(ingredient, batchSize)));

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

        //STEP1: put in ingredients. for each slot in the crafting table
        for (int rowIndex = 0; rowIndex < recipe.inputs.length; rowIndex++) {
            for (int colIndex = 0; colIndex < recipe.inputs.length; colIndex++) {
                int recipePosition = rowIndex*2 + colIndex + 1;
                Item item = recipe.inputs[rowIndex][colIndex];
                if (item == null) {
                    continue;
                }

                int remaining = quantity;
                // pick up item from each location and put them in the crafting table
                for (Map.Entry<Vault.ItemLocationDescriptor, Integer> locationAndCount : locations.get(item)) {
                    //only wants the items in inventory
                    if (locationAndCount.getKey().device != Vault.ItemLocationDescriptor.Device.INVENTORY){
                        continue;
                    }

                    Vault.InventoryDescriptor locationDescriptor = ((Vault.InventoryDescriptor) locationAndCount.getKey());
                    //when the item doesn't match what we have thought, throw an error
                    assert locationDescriptor.quantity == inventory.getStack(locationDescriptor.slot.playerInventoryId).getCount();

                    pickupItem(locationDescriptor.slot, locationAndCount.getValue());
                    clickSlot(InventorySlot.valueOf("CRAFT_INPUT_" + recipePosition), SlotActionType.PICKUP, 0);

                    remaining -= locationAndCount.getValue();
                    if (remaining == 0){
                        break;
                    }
                }


            }
        }

        //STEP2: take out the product
        clickSlot(InventorySlot.CRAFT_OUTPUT, SlotActionType.QUICK_MOVE, 0);
        vault.update(inventory);
    }
}
