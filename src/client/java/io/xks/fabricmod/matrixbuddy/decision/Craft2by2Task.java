package io.xks.fabricmod.matrixbuddy.decision;

import io.xks.fabricmod.matrixbuddy.player.Backpack;
import net.minecraft.client.MinecraftClient;

import net.minecraft.item.Item;


public class Craft2by2Task implements Task{

    Item[][] recipe;
    int batchSize;
    public Craft2by2Task(Item[][] recipe, int batchSize){
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
