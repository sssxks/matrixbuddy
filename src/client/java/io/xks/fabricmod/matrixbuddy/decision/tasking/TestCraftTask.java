package io.xks.fabricmod.matrixbuddy.decision.tasking;

import io.xks.fabricmod.matrixbuddy.decision.storage.Recipe;
import io.xks.fabricmod.matrixbuddy.player.Backpack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;

public class TestCraftTask implements Task{

    @Override
    public void run() {


//        MinecraftClient client = MinecraftClient.getInstance();
//        assert client.player != null;
//        Backpack backpack = new Backpack(client.player.getInventory());
//
        Recipe recipe = new Recipe(new Item[][]{{Registries.ITEM.get(new Identifier("minecraft", "oak_log")).asItem(), null}, {null, null}});

        new Craft2by2Task(recipe, 67).run();
//        backpack.craft(recipe, 67);
//        backpack.clickSlot(Backpack.BackpackSlot.INVENTORY_28, SlotActionType.PICKUP, 0);
//        backpack.clickSlot(Backpack.BackpackSlot.CRAFT_INPUT_1, SlotActionType.PICKUP, 0);
//        backpack.clickSlot(Backpack.BackpackSlot.CRAFT_OUTPUT, SlotActionType.QUICK_MOVE, 0);

    }
}
