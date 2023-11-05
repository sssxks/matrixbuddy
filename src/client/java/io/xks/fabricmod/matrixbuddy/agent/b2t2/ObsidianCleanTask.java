package io.xks.fabricmod.matrixbuddy.agent.b2t2;

import baritone.cache.WorldScanner;
import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import io.xks.fabricmod.matrixbuddy.agent.movement.GetToBlockTask;
import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;
import io.xks.fabricmod.matrixbuddy.agent.tasking.TaskExecutor;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ObsidianCleanTask extends Task {

    private final TaskExecutor taskExecutor;

    /**
     * Constructs a new cooperative task.
     *
     * @param callback a callback to be invoked when the task completes
     */
    public ObsidianCleanTask(Consumer<Task> callback) {
        super(callback);
        this.taskExecutor = new TaskExecutor();
    }

    @Override
    public void run() {
        super.run();

        CompletableFuture.supplyAsync(this::calculateObsidianGroup).thenAccept(result -> {
//            if (status = Status.PENDING) {
//TODO: task Stack? task state store & resume.
//            }
            result.forEach(group -> {
                if (group.size() >= 50){
                    return;
                }
                
                group.forEach(blockPos -> {
                    taskExecutor.add(new GetToBlockTask(blockPos, task -> {}), 1);
                });
            });


        }).exceptionally(ex -> {

            return null;
        });
    }

    private ArrayList<LinkedList<BlockPos>> calculateObsidianGroup() {
        ArrayList<LinkedList<BlockPos>> groups = new ArrayList<>();

        List<BlockPos> posList = WorldScanner.INSTANCE.scanChunkRadius(MatrixBuddyClient.instance.baritone.getPlayerContext(), List.of(Blocks.OBSIDIAN), 1000, 45, 32);

        Map<BlockPos, Boolean> visited = new HashMap<>();

        for (BlockPos pos : posList) {
            if (visited.getOrDefault(pos, false)){
                continue;
            }

            groups.add(new BlockVisitor().breadthFirstSearch(new TangibleBlockView(pos), visited));

        }


        return groups;
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void complete() {
        super.complete();
    }
}
