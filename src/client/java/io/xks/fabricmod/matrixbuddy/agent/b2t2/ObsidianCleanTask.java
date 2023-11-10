package io.xks.fabricmod.matrixbuddy.agent.b2t2;

import baritone.api.pathing.goals.GoalBlock;
import baritone.api.pathing.goals.GoalComposite;
import baritone.cache.WorldScanner;
import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import io.xks.fabricmod.matrixbuddy.agent.movement.CustomGoalTask;
import io.xks.fabricmod.matrixbuddy.agent.movement.ExploreTask;
import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;
import io.xks.fabricmod.matrixbuddy.agent.tasking.TaskExecutor;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
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

            GoalBlock[] goals = result.parallelStream().filter(group -> group.size() < 50).flatMap(group -> group.stream().map(GoalBlock::new)).toArray(GoalBlock[]::new);

            if (goals.length != 0) {
                taskExecutor.add(new CustomGoalTask(new GoalComposite(goals), task -> run()), 1);
            } else {
                assert MinecraftClient.getInstance().player != null;
                taskExecutor.add(new ExploreTask(MinecraftClient.getInstance().player.getBlockX(), MinecraftClient.getInstance().player.getBlockY(), task -> run()), 1);
            }

        }).exceptionally(ex -> {

            return null;
        });
    }



    private ArrayList<LinkedList<BlockPos>> calculateObsidianGroup() {
        ArrayList<LinkedList<BlockPos>> groups = new ArrayList<>();

        List<BlockPos> posList = WorldScanner.INSTANCE.scanChunkRadius(MatrixBuddyClient.INSTANCE.baritone.getPlayerContext(), List.of(Blocks.OBSIDIAN), 1000, 45, 32);

        Map<BlockPos, Boolean> visited = new HashMap<>();

        for (BlockPos pos : posList) {
            if (visited.getOrDefault(pos, false)) {
                continue;
            }

            groups.add(findGroupOfBlock(new TangibleBlockView(pos), visited));

        }


        return groups;
    }

    public LinkedList<BlockPos> findGroupOfBlock(BlockView startBlock, Map<BlockPos, Boolean> visited) {
        assert startBlock.getBlock() == Blocks.OBSIDIAN;
        LinkedList<BlockPos> group = new LinkedList<>();
        Queue<BlockView> queue = new LinkedList<>();
        queue.add(startBlock);

        while (!queue.isEmpty()) {
            BlockView block = queue.poll();
            group.add(block.getPos());
            visited.put(block.getPos(), true);

            for (BlockView neighbor : block.getAdjacent()) {
                if (neighbor.getBlock() == Blocks.OBSIDIAN
                        && !visited.getOrDefault(neighbor.getPos(), false)
                        && !queue.contains(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }

        return group;
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
