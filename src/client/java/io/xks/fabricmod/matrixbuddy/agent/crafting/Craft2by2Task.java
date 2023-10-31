package io.xks.fabricmod.matrixbuddy.agent.crafting;

import io.xks.fabricmod.matrixbuddy.agent.storage.Recipe;
import io.xks.fabricmod.matrixbuddy.player.InventoryController;
import net.minecraft.client.MinecraftClient;

public class Craft2by2Task implements Runnable {

    private final Recipe recipe;
    private final int batchSize;

    public Craft2by2Task(Recipe recipe, int batchSize) {
        this.recipe = recipe;
        this.batchSize = batchSize;
    }

    public void run() {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        InventoryController inventoryController = new InventoryController(client.player.getInventory());

        inventoryController.craft(recipe, batchSize);
    }

}