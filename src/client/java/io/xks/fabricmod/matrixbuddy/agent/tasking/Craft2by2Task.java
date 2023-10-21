package io.xks.fabricmod.matrixbuddy.agent.tasking;

import io.xks.fabricmod.matrixbuddy.agent.storage.Recipe;
import io.xks.fabricmod.matrixbuddy.player.Backpack;
import net.minecraft.client.MinecraftClient;

public class Craft2by2Task implements Task{

    final Recipe recipe;
    final int batchSize;
    public Craft2by2Task(Recipe recipe, int batchSize){
        this.recipe = recipe;
        this.batchSize = batchSize;
    }
    @Override
    public void run() {
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        Backpack backpack = new Backpack(client.player.getInventory());

        backpack.craft(recipe, batchSize);
    }

}
