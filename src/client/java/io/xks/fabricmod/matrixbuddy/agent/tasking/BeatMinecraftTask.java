package io.xks.fabricmod.matrixbuddy.agent.tasking;

import io.xks.fabricmod.matrixbuddy.agent.collect.MineTask;
import io.xks.fabricmod.matrixbuddy.agent.crafting.Craft2by2Task;
import io.xks.fabricmod.matrixbuddy.agent.crafting.CraftPipelineTask;
import io.xks.fabricmod.matrixbuddy.agent.storage.Recipe;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class BeatMinecraftTask extends Task {
    /**
     * Constructs a new beat minecraft task. The player will chop down trees,
     * mine ores.
     *
     *
     * @param callback a callback to be invoked when the task completes
     */
    public BeatMinecraftTask(Consumer<Task> callback) {
        super(callback);
    }

    @Override
    public void run() {
        super.run();
        new MineTask(16, this::afterChoppingTree, "minecraft:oak_log","minecraft:birch_log","minecraft:spruce_log","minecraft:jungle_log","minecraft:acacia_log","minecraft:dark_oak_log", "minecraft:mangrove_log", "minecraft:cherry_log").run();
    }

    public void afterChoppingTree(Task task){
        new MineTask(16,this::afterCollectingDirt, "minecraft:dirt").run();
    }

    public void afterCollectingDirt(Task task) {
        Recipe planksRecipe = new Recipe(new Item[][]{{Registries.ITEM.get(new Identifier("minecraft", "oak_log")).asItem(), null}, {null, null}});

        Item planks = Registries.ITEM.get(new Identifier("minecraft", "oak_planks")).asItem();
        Recipe workbenchRecipe = new Recipe(new Item[][]{{planks, planks}, {planks, planks}});

        new Craft2by2Task(planksRecipe, 1);
        new CraftPipelineTask(new LinkedList<>(List.of(new Craft2by2Task(planksRecipe, 1), new Craft2by2Task(workbenchRecipe, 1))), this::afterCraftingCraftingTable).run();
    }

    public void afterCraftingCraftingTable(Task task){

    }

}
