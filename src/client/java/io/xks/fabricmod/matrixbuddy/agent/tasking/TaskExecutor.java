package io.xks.fabricmod.matrixbuddy.agent.tasking;

import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class TaskExecutor {

    private static class PrioritizedTask{
        Task task;

        public int getPriority() {
            return priority;
        }

        int priority;

        public PrioritizedTask(Task task, int priority) {
            this.task = task;
            this.priority = priority;
        }
    }
    PriorityQueue<PrioritizedTask> queue;

    public TaskExecutor() {
        this.queue = new PriorityQueue<>(Comparator.comparing(PrioritizedTask::getPriority));
    }

    public void add(Task task, int priority){
        if (!queue.isEmpty() && priority > queue.peek().priority) {
            queue.peek().task.interrupt();
        }

        queue.add(new PrioritizedTask(task, priority));
        queue.peek().task.run();
    }

//    public void remove(Task task){
//        queue.
//    }

    public void onTaskComplete(Task task){
        assert Objects.requireNonNull(queue.peek()).task == task;
        queue.poll();

        if (!queue.isEmpty()){
            queue.peek().task.run();
        }
    }
}
