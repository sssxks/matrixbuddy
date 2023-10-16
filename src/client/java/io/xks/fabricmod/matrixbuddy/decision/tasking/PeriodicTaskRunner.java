package io.xks.fabricmod.matrixbuddy.decision.tasking;


import java.util.LinkedList;

/**
 * A runner for periodic cooperative tasks. It should be ticked at regular intervals to update the tasks' status.
 */
public class PeriodicTaskRunner {
    static final LinkedList<PeriodicTimeSlicedTask> tasks = new LinkedList<>();
    static final LinkedList<PeriodicTimeSlicedTask> tasksToAdd = new LinkedList<>();
    static final LinkedList<PeriodicTimeSlicedTask> tasksToRemove = new LinkedList<>();

    /**
     * Adds a task to the runner.
     *
     * @param task the task to add
     */
    static void addTask(PeriodicTimeSlicedTask task){
        tasksToAdd.add(task);
    }

    /**
     * Updates the status of all tasks. This method should be called at regular intervals.
     */
    public static void tick(){
        tasks.forEach(PeriodicTimeSlicedTask::tick);

        // Process tasks to be removed
        tasksToRemove.forEach(tasks::remove);
        tasksToRemove.clear();

        // Process tasks to be added
        tasks.addAll(tasksToAdd);
        tasksToAdd.clear();

    }

    /**
     * Removes a task from the runner.
     *
     * @param task the task to remove
     */
    static void removeTask(PeriodicTimeSlicedTask task){
        tasksToRemove.add(task);
    }
}
