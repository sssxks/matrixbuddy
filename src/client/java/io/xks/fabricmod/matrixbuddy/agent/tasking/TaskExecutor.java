package io.xks.fabricmod.matrixbuddy.agent.tasking;

import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.function.Consumer;

public class TaskExecutor {

    /**
     * wrapper class used in the task executor.
     */
    private static class PrioritizedTask extends Task{
        @Override
        public void run() {
            task.run();
        }

        @Override
        public void interrupt() {
            task.interrupt();
        }

        @Override
        public void resume() {
            task.resume();
        }

        @Override
        public void complete() {
            task.complete();
        }

        private final Task task;

        public int getPriority() {
            return priority;
        }

        private final int priority;

        public PrioritizedTask (Task task, int priority, Consumer<Task> callback) {
            super(null);
            this.task = task;
            this.priority = priority;
            Consumer<? super Task> originalCallback = task.callback;
            task.callback = (Consumer<Task>) task1 -> {
                originalCallback.accept(task1);
                callback.accept(this);
            };
        }
    }
    PriorityQueue<PrioritizedTask> queue;

    public TaskExecutor() {
        this.queue = new PriorityQueue<>(Comparator.comparing(PrioritizedTask::getPriority));
    }

    public void add(Task task, int priority){
        PrioritizedTask previous = queue.peek();
        queue.add(new PrioritizedTask(task, priority, this::onTaskComplete));

        if (previous != null && priority > previous.priority) {
            previous.interrupt();
            Objects.requireNonNull(queue.peek()).run();
        }

        if (previous == null){
            Objects.requireNonNull(queue.peek()).run();
        }
    }

//    public void remove(Task task){
//        queue.
//    }

    public void onTaskComplete(Task task){
        assert Objects.requireNonNull(queue.peek()).task == task;
        queue.poll();

        if (!queue.isEmpty()){
            queue.peek().run();
        }
    }
}
