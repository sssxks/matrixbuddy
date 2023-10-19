package io.xks.fabricmod.matrixbuddy.decision.tasking;

public class EmptyTestTask implements Task {
    @Override
    public void run() {
        System.out.println("launch task");
    }
}
