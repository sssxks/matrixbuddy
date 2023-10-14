package io.xks.fabricmod.matrixbuddy.decision;

import java.util.function.Consumer;

/**
 * A cooperative task that needs an external tick signal to update its status.
 */
public abstract class PeriodicTimeSlicedTask extends TimeSlicedTask {
    /**
     * Constructs a new periodic cooperative task.
     *
     * @param callback a callback to be invoked when the task completes
     */
    public PeriodicTimeSlicedTask(Consumer<TimeSlicedTask> callback) {
        super(callback);
    }

    @Override
    public void run(){
        super.run();
        PeriodicTaskRunner.addTask(this);
    }

    @Override
    public void complete(){
        super.complete();
        PeriodicTaskRunner.removeTask(this);
    }

    /**
     * Updates the task's status. This method should be called at regular intervals.
     * // check if finished ...
     * if finished then complete()
     */
    public abstract void tick();


}
