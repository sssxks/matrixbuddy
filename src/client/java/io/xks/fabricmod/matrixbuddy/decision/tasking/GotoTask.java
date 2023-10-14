package io.xks.fabricmod.matrixbuddy.decision.tasking;

import baritone.api.pathing.goals.GoalXZ;
import baritone.api.process.ICustomGoalProcess;
import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public class GotoTask extends PeriodicTimeSlicedTask {
    private int x;
    private int z;

    /**
     * Constructs a new periodic cooperative task.
     *
     * @param callback a callback to be invoked when the task completes
     */
    public GotoTask(Consumer<TimeSlicedTask> callback) {
        super(callback);
    }

    @Override
    public void run() {
        super.run();
        ICustomGoalProcess customGoalProcess = MatrixBuddyClient.instance.baritone.getCustomGoalProcess();
        GoalXZ walkingGoal = new GoalXZ(x,z);
        customGoalProcess.setGoalAndPath(walkingGoal);
    }

    @Override
    public void tick() {
        assert MinecraftClient.getInstance().player != null;
        BlockPos pos = MinecraftClient.getInstance().player.getBlockPos();
        if (pos.getX() == x && pos.getZ() == z){
            complete();
        }

    }
}
