package io.xks.fabricmod.matrixbuddy.agent.tasking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * A task that runs multiple tasks in succession.
 */
public class CraftPipelineTask extends TimeSlicedTask{
    LinkedList<Craft2by2Task> tasks;
    Queue<PeriodicTimeSlicedTask> queue;
    /**
     * Constructs a new pipeline task
     *
     * @param callback a callback to be invoked when the task completes
     */
    public CraftPipelineTask(LinkedList<Craft2by2Task> tasks, Consumer<TimeSlicedTask> callback) {
        super(callback);

        this.tasks = tasks;
        this.queue = new LinkedList<>();

        for (Craft2by2Task task : tasks) {
            queue.add(new PeriodicTimeSlicedTask(timeSlicedTask -> {if (queue.isEmpty()){
                    this.complete();
                    return;
                }
                queue.poll().run();
            }) {
                @Override
                public void tick() {
                    if (this.status == Status.RUNNING){complete();}
                }

                @Override
                public void run() {
                    super.run();
                    task.run();
                }
            });
        }
    }

    @Override
    public void run() {
        super.run();
        if (queue.isEmpty()){
            complete();
            return;
        }

        queue.poll().run();
    }
}
