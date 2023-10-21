package io.xks.fabricmod.matrixbuddy.agent.tasking;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * A task that runs multiple tasks in succession.//TODO: implement this.
 */
public class PipelineTask extends PeriodicTimeSlicedTask{
    ArrayList<Task> tasks;
    /**
     * Constructs a new pipeline task
     *
     * @param callback a callback to be invoked when the task completes
     */
    public PipelineTask(Consumer<TimeSlicedTask> callback) {
        super(callback);
    }

    @Override
    public void tick() {

    }
}
