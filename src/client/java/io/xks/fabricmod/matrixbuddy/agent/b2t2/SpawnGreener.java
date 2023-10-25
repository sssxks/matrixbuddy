package io.xks.fabricmod.matrixbuddy.agent.b2t2;

import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;
import io.xks.fabricmod.matrixbuddy.agent.tasking.TaskExecutor;

import java.util.function.Consumer;

public class SpawnGreener extends Task {

    private final TaskExecutor taskExecutor;

    /**
     * Constructs a new cooperative task.
     *
     * @param callback a callback to be invoked when the task completes
     */
    public SpawnGreener(Consumer<Task> callback) {
        super(callback);
        taskExecutor = new TaskExecutor();
    }

    @Override
    public void run() {
        super.run();
//        taskExecutor.add()
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    @Override
    public void proceed() {
        super.proceed();
    }

    @Override
    public void complete() {
        super.complete();
    }
}
