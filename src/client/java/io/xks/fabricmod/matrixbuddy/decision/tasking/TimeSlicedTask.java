package io.xks.fabricmod.matrixbuddy.decision.tasking;

import java.util.function.Consumer;

/**
 * A task that cooperates with others by yielding control at regular intervals.
 * It's not truly asynchronous as all tasks run on the same thread.
 */
public abstract class TimeSlicedTask implements Task {
    public enum Status {
        NOT_STARTED,
        RUNNING,
        COMPLETED
    }
    public Status status;
    public final Consumer<? super TimeSlicedTask> callback;

    /**
     * Constructs a new cooperative task.
     *
     * @param callback a callback to be invoked when the task completes
     */
    public TimeSlicedTask(Consumer<TimeSlicedTask> callback){
        status = Status.NOT_STARTED;
        this.callback = callback;
    }

    @Override
    public void run(){
        status = Status.RUNNING;
    }

    /**
     * Marks the task as completed and invokes the callback.
     */
    public void complete(){
        status = Status.COMPLETED;
        callback.accept(this);
    }
}
