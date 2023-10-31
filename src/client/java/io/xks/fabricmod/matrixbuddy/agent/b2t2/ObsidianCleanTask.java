package io.xks.fabricmod.matrixbuddy.agent.b2t2;

import baritone.Baritone;
import baritone.cache.CachedChunk;
import baritone.cache.WorldScanner;
import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ObsidianCleanTask extends Task {


    /**
     * Constructs a new cooperative task.
     *
     * @param callback a callback to be invoked when the task completes
     */
    public ObsidianCleanTask(Consumer<Task> callback) {
        super(callback);
    }

    @Override
    public void run() {
        super.run();

        CompletableFuture.supplyAsync(this::calculateObsidianGroup).thenAccept(result -> {
//            if (status = Status.PENDING) {
//TODO: task Stack? task state store & resume.
//            }


        }).exceptionally(ex -> {

            return null;
        });
    }

    private ArrayList<LinkedList<Vec3i>> calculateObsidianGroup() {
        List<BlockPos> posList = WorldScanner.INSTANCE.scanChunkRadius(MatrixBuddyClient.instance.baritone.getPlayerContext(), List.of(Blocks.OBSIDIAN), 1000, 45, 32);


        return null;
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
