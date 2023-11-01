package io.xks.fabricmod.matrixbuddy.agent.b2t2;

import baritone.Baritone;
import baritone.cache.CachedChunk;
import baritone.cache.WorldScanner;
import com.sun.source.tree.WhileLoopTree;
import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import io.xks.fabricmod.matrixbuddy.agent.collect.MineTask;
import io.xks.fabricmod.matrixbuddy.agent.movement.GetToBlockTask;
import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ObsidianCleanTask extends Task {
    LinkedList<GetToBlockTask> tasks;

    /**
     * Constructs a new cooperative task.
     *
     * @param callback a callback to be invoked when the task completes
     */
    public ObsidianCleanTask(Consumer<Task> callback) {
        super(callback);
        tasks = new LinkedList<>();
    }

    @Override
    public void run() {
        super.run();

        CompletableFuture.supplyAsync(this::calculateObsidianGroup).thenAccept(result -> {
//            if (status = Status.PENDING) {
//TODO: task Stack? task state store & resume.
//            }
            result.forEach(groups -> groups.forEach(blockPos -> new GetToBlockTask(blockPos, task -> {})));


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
