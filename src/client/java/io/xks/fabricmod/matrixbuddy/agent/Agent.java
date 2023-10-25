package io.xks.fabricmod.matrixbuddy.agent;

import io.xks.fabricmod.matrixbuddy.agent.command.Commander;
import io.xks.fabricmod.matrixbuddy.agent.tasking.Task;
import io.xks.fabricmod.matrixbuddy.agent.tasking.TaskExecutor;

/**
 * Agent
 * attach logger
 * attach commander
 *
 */
public class Agent {
    Commander commander;
    TaskExecutor taskExecutor;

    public Agent() {
        this.commander = new Commander();
        this.taskExecutor = new TaskExecutor();
    }

//    public onPlayerDamage()


}
