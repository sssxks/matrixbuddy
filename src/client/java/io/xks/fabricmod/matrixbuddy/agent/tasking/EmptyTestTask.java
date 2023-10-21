package io.xks.fabricmod.matrixbuddy.agent.tasking;

public class EmptyTestTask implements Task {
    @Override
    public void run() {
        System.out.println("launch task");
    }
}
