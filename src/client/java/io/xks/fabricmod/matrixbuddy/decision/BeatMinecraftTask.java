package io.xks.fabricmod.matrixbuddy.decision;

import java.util.function.Consumer;

public class BeatMinecraftTask extends TimeSlicedTask{
    /**
     * Constructs a new beat minecraft task. The player will chop down trees,
     * mine ores.
     *
     *
     * @param callback a callback to be invoked when the task completes
     */
    public BeatMinecraftTask(Consumer<TimeSlicedTask> callback) {
        super(callback);
    }

    @Override
    public void run() {
        super.run();
        new MineTask(16, this::afterChoppingTree, "minecraft:oak_log","minecraft:birch_log","minecraft:spruce_log","minecraft:jungle_log","minecraft:acacia_log","minecraft:dark_oak_log", "minecraft:mangrove_log", "minecraft:cherry_log").run();
    }

    public void afterChoppingTree(TimeSlicedTask task){
        new MineTask(16,this::afterCollectingDirt, "minecraft:dirt").run();
    }

    private void afterCollectingDirt(TimeSlicedTask task) {
//        new Craft2by2Task
        complete();
    }
}
