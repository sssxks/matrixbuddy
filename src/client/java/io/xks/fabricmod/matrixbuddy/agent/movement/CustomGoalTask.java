package io.xks.fabricmod.matrixbuddy.agent.movement;

import baritone.api.pathing.goals.Goal;
import baritone.api.process.ICustomGoalProcess;
import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;
import io.xks.fabricmod.matrixbuddy.eventbus.EventBus;
import io.xks.fabricmod.matrixbuddy.eventbus.EventListener;
import io.xks.fabricmod.matrixbuddy.eventbus.events.DecisionTickEvent;
import io.xks.fabricmod.matrixbuddy.eventbus.events.Event;
import net.minecraft.client.MinecraftClient;

import java.util.function.Consumer;

/**
 * task wrapper for baritone CustomGoalProcess, providing lifesaver functionalities including callback after completing the task and pause.
 */
public class CustomGoalTask extends Task {
    private Goal goal;
    private final EventListener listener;


    public CustomGoalTask(Goal goal, Consumer<Task> callback) {
        super(callback);
        this.goal = goal;
        listener = this::tick;
    }

    @Override
    public void run() {
        super.run();

        ICustomGoalProcess customGoalProcess = MatrixBuddyClient.instance.baritone.getCustomGoalProcess();
        customGoalProcess.setGoalAndPath(goal);

        EventBus.subscribe(DecisionTickEvent.class, listener);
    }

    public void tick(Event event) {
        assert MinecraftClient.getInstance().player != null;

        if (!MatrixBuddyClient.instance.baritone.getCustomGoalProcess().isActive()) {
            complete();
        }

        //alternative implementation 1
//        if (!MatrixBuddyClient.instance.baritone.getPathingBehavior().isPathing()) {
//            complete();
//        }
        //alternative implementation 2
//        BlockPos pos = MinecraftClient.getInstance().player.getBlockPos();
//        if (goal.isInGoal(pos)){
//            complete();
//        }

    }

    @Override
    public void interrupt() {
        super.interrupt();
        MatrixBuddyClient.instance.baritone.getPathingBehavior().cancelEverything();
        EventBus.unsubscribe(DecisionTickEvent.class, listener);
    }

    @Override
    public void resume() {
        super.resume();
        run();

    }

    @Override
    public void complete() {
        super.complete();
        EventBus.unsubscribe(DecisionTickEvent.class, listener);
    }
}
