package io.xks.fabricmod.matrixbuddy.player;

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
        INVENTORY_36(44);

        public final int id;

        BackpackSlot(int id) {
            this.id = id;
        }

        public static BackpackSlot fromId(int id){//TODO: look up in a map, reduce complexity
            for (BackpackSlot slot : BackpackSlot.values()) {
                if (slot.id == id){
                    return slot;
                }
            }
            throw new IllegalArgumentException("No enum constant BackPackSlot with id " + id);

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
    private void clickSlot(BackpackSlot slot, SlotActionType actionType, int button){
        MinecraftClient client = MinecraftClient.getInstance();

        assert client.player != null;
        int syncId = client.player.currentScreenHandler.syncId;
        assert client.interactionManager != null;
        client.interactionManager.clickSlot(syncId, slot.id, button, actionType, client.player);
    }

    public void craft(Item[][] recipe, int batchSize){
        if (!vault.contains(recipe, batchSize)) {
            throw new IllegalStateException("you don't have the adequate amount of items!");
        }


        for (int rowIndex = 0; rowIndex < recipe.length; rowIndex++) {
            for (int colIndex = 0; colIndex < recipe[rowIndex].length; colIndex++) {
                int recipePosition = rowIndex + colIndex + 1;
                Item item = recipe[rowIndex][colIndex];

                List<Map.Entry<Vault.ItemLocationDescriptor, Integer>> locations = vault.locateItem(item, batchSize);

                for (Map.Entry<Vault.ItemLocationDescriptor, Integer> location : locations) {
                    //when the item doesn't match what we have thought, throw an error
                    assert location.getKey().quantity == inventory.getStack(location.getKey().slot.id).getCount();
                    pickupItem(location.getKey().slot, location.getValue());
                }

                for (int i = 0; i < batchSize; i++) {
                    clickSlot(BackpackSlot.valueOf("CRAFT_INPUT_" + recipePosition), SlotActionType.PICKUP, 1);
                }
            }
        }
    }
}
