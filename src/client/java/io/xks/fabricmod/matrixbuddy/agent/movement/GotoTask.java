package io.xks.fabricmod.matrixbuddy.agent.movement;

import baritone.api.pathing.goals.GoalXZ;
import baritone.api.process.ICustomGoalProcess;
import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;
import io.xks.fabricmod.matrixbuddy.eventbus.EventBus;
import io.xks.fabricmod.matrixbuddy.eventbus.events.DecisionTickEvent;
import io.xks.fabricmod.matrixbuddy.eventbus.events.Event;
import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;

/**
 * as if type in the baritone goto command. //TODO:more goal types.
 */
public class GotoTask extends Task {
    private int x;
    private int z;

    /**
     * Constructs a new periodic cooperative task.
     *
     * @param callback a callback to be invoked when the task completes
     */
    public GotoTask(int x, int y, Consumer<Task> callback) {
        super(callback);
    }

    @Override
    public void run() {
        super.run();
        ICustomGoalProcess customGoalProcess = MatrixBuddyClient.instance.baritone.getCustomGoalProcess();
        GoalXZ walkingGoal = new GoalXZ(x,z);
        customGoalProcess.setGoalAndPath(walkingGoal);

        EventBus.subscribe(DecisionTickEvent.class, this::tick);
    }

    public void tick(Event event) {
        assert MinecraftClient.getInstance().player != null;
        //TODO: test needed.
//        if (!MatrixBuddyClient.instance.baritone.getPathingBehavior().isPathing()) {
//            complete();
//        }
        if (!MatrixBuddyClient.instance.baritone.getCustomGoalProcess().isActive()) {
            complete();
        }

//        BlockPos pos = MinecraftClient.getInstance().player.getBlockPos();
//        if (pos.getX() == x && pos.getZ() == z){
//            complete();
//        }

    }

    @Override
    public void interrupt() {
        super.interrupt();
        MatrixBuddyClient.instance.baritone.getPathingBehavior().cancelEverything();

    }

    @Override
    public void resume() {
        super.resume();
        run();

    }

    @Override
    public void complete() {
        super.complete();
        EventBus.unsubscribe(DecisionTickEvent.class, this::tick);
    }
}
