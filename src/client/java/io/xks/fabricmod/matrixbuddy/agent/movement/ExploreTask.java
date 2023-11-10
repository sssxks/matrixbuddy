package io.xks.fabricmod.matrixbuddy.agent.movement;

import io.xks.fabricmod.matrixbuddy.MatrixBuddyClient;
import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;

import java.util.function.Consumer;

public class ExploreTask extends Task {
    private final int centerX;
    private final int centerY;
    /**
     * Constructs a new cooperative task.
     *
     * @param callback a callback to be invoked when the task completes
     */
    public ExploreTask(int centerX, int centerY, Consumer<Task> callback) {
        super(callback);
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public void run() {
        super.run();

        MatrixBuddyClient.INSTANCE.baritone.getExploreProcess().explore(centerX, centerY);
    }

    @Override
    public void interrupt() {
        super.interrupt();

        MatrixBuddyClient.INSTANCE.baritone.getPathingBehavior().cancelEverything();
    }

    @Override
    public void resume() {
        super.resume();

        run();
    }

    @Override
    public void complete() {

        super.complete();
    }
}
