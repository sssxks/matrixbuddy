package io.xks.fabricmod.matrixbuddy.agent.movement;

import baritone.api.pathing.goals.GoalBlock;
import baritone.api.process.ICustomGoalProcess;
import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;
import io.xks.fabricmod.matrixbuddy.eventbus.EventBus;
import io.xks.fabricmod.matrixbuddy.eventbus.events.DecisionTickEvent;
import io.xks.fabricmod.matrixbuddy.eventbus.events.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public class GetToBlockTask extends Task {
    BlockPos pos;


    /**
     * Constructs a new cooperative task.
     *
     * @param callback a callback to be invoked when the task completes
     */
    public GetToBlockTask(BlockPos pos, Consumer<Task> callback) {
        super(callback);
        this.pos = pos;
    }

    @Override
    public void run() {
        super.run();
        ICustomGoalProcess customGoalProcess = MatrixBuddyClient.instance.baritone.getCustomGoalProcess();

        GoalBlock walkingGoal = new GoalBlock(pos);
        customGoalProcess.setGoalAndPath(walkingGoal);

        EventBus.subscribe(DecisionTickEvent.class, this::tick);

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

}
