package io.xks.fabricmod.matrixbuddy.agent.crafting;

import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * A task that runs multiple tasks in succession.
 */
public class CraftPipelineTask extends Task {
    LinkedList<Runnable> tasks;
    Queue<Task> queue;
    /**
     * Constructs a new pipeline task
     *
     * @param callback a callback to be invoked when the task completes
     */
    public CraftPipelineTask(LinkedList<Runnable> tasks, Consumer<Task> callback) {
        super(callback);

        this.tasks = tasks;
        this.queue = new LinkedList<>();

        for (Runnable task : tasks) {
            queue.add(new Task(timeSlicedTask -> {if (queue.isEmpty()){
                    this.complete();
                    return;
                }
                queue.poll().run();
            }) {

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
